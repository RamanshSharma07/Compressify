package com.example.compressify.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException

class ImageCompressor(
    private val context: Context,
) {
    suspend fun compressImageToThreshold(
            imageUri: Uri,
            thresholdSizeBytes: Long,
            initialQuality: Int = 90,
            minQuality: Int = 5, // Go quite low if necessary
            defaultFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG // JPEG is good for aggressive lossy
        ): ByteArray? {
            Log.e("ImageCompressor", "The threashold size is $thresholdSizeBytes")
            if (thresholdSizeBytes <= 0) {
                Log.e("ImageCompressor", "Threshold size must be positive.")
                return null
            }

            return withContext(Dispatchers.IO) {
                var inputBytes: ByteArray? = null
                try {
                    context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                        inputBytes = inputStream.readBytes()
                    }
                } catch (e: IOException) {
                    Log.e("ImageCompressor", "IOException reading Uri: ${imageUri}", e)
                    return@withContext null
                } catch (e: SecurityException) {
                    Log.e("ImageCompressor", "SecurityException reading Uri: ${imageUri}", e)
                    return@withContext null
                }

                if (inputBytes == null || inputBytes!!.isEmpty()) {
                    Log.e("ImageCompressor", "Failed to read image bytes or image is empty from Uri: $imageUri")
                    return@withContext null
                }

                // If already below threshold, return original bytes
                if (inputBytes!!.size <= thresholdSizeBytes) {
                    Log.i("ImageCompressor", "Image is already within threshold. Original size: ${inputBytes!!.size / 1024}KB")
                    return@withContext inputBytes
                }

                val determinedMimeType = context.contentResolver.getType(imageUri)
                Log.d("ImageCompressor", "Determined MIME type: $determinedMimeType")

                val compressFormatToUse = when (determinedMimeType) {
                    "image/jpeg" -> Bitmap.CompressFormat.JPEG
                    "image/png" -> Bitmap.CompressFormat.PNG
                    "image/webp" -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        Bitmap.CompressFormat.WEBP_LOSSY // Prefer lossy WEBP for size reduction
                    } else {
                        Bitmap.CompressFormat.WEBP // Older WEBP is lossy by default when quality < 100
                    }
                    else -> {
                        Log.w("ImageCompressor", "Unknown MIME type ($determinedMimeType), using defaultFormat: $defaultFormat")
                        defaultFormat
                    }
                }

                // If the determined format is PNG and the goal is aggressive size reduction,
                // and it's over the threshold, it's often better to convert to JPEG.
                // However, the function signature uses `defaultFormat` if type is unknown,
                // let's stick to chosen compressFormatToUse based on original type for now,
                // unless defaultFormat is explicitly preferred by caller for ALL types (by changing logic)

                withContext(Dispatchers.Default) { // Switch to Default dispatcher for bitmap operations
                    val bitmapOptions = BitmapFactory.Options().apply { inJustDecodeBounds = false }
                    val bitmap = BitmapFactory.decodeByteArray(inputBytes, 0, inputBytes!!.size, bitmapOptions)
                        ?: run {
                            Log.e("ImageCompressor", "Failed to decode bitmap from inputBytes.")
                            return@withContext null // inputBytes won't be null here due to earlier check
                        }

                    var currentQuality = initialQuality.coerceIn(minQuality, 100)
                    var compressedBytes: ByteArray? = null
                    var attempts = 0
                    val maxAttempts = 20 // Max attempts to prevent infinite loops

                    do {
                        ensureActive() // Check for coroutine cancellation

                        ByteArrayOutputStream().use { outputStream ->
                            Log.d("ImageCompressor", "Attempt #${attempts + 1}: Compressing with format '$compressFormatToUse', quality: $currentQuality")
                            bitmap.compress(compressFormatToUse, currentQuality, outputStream)
                            compressedBytes = outputStream.toByteArray()
                        }
                        Log.d("ImageCompressor", "Attempt #${attempts + 1}: Compressed size: ${compressedBytes!!.size / 1024}KB (Threshold: ${thresholdSizeBytes / 1024}KB)")

                        if (compressedBytes.size <= thresholdSizeBytes) {
                            Log.i("ImageCompressor", "Threshold met. Final size: ${compressedBytes.size / 1024}KB, Quality: $currentQuality, Format: $compressFormatToUse")
                            return@withContext compressedBytes
                        }

                        // For PNG, quality reduction won't help further if still above threshold.
                        if (compressFormatToUse == Bitmap.CompressFormat.PNG) {
                            Log.w("ImageCompressor", "PNG format compressed once. Size (${compressedBytes.size / 1024}KB) is still above threshold (${thresholdSizeBytes / 1024}KB). PNG quality adjustments do not reduce file size like JPEG/WEBP.")
                            return@withContext compressedBytes // Return the single attempt for PNG
                        }

                        if (currentQuality <= minQuality) {
                            Log.w("ImageCompressor", "Minimum quality ($minQuality) reached. Could not meet threshold. Final size: ${compressedBytes.size / 1024}KB, Format: $compressFormatToUse")
                            return@withContext compressedBytes // Return best effort
                        }

                        // More aggressive quality drop
                        currentQuality = (currentQuality - 10).coerceAtLeast(minQuality)
                        attempts++

                    } while (attempts < maxAttempts && isActive)

                    if (!isActive) {
                        Log.w("ImageCompressor", "Compression was cancelled.")
                        return@withContext null
                    }

                    // If loop finished due to max attempts
                    Log.w("ImageCompressor", "Max attempts reached. Could not meet threshold. Final size: ${compressedBytes.size / 1024}KB, Quality: $currentQuality, Format: $compressFormatToUse")
                    compressedBytes // Return the last attempt
                }
            }
        }
    }






