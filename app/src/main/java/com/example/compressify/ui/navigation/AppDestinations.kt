package com.example.compressify.ui.navigation


object AppDestinations {
    const val SCREEN_HOME_ROUTE = "screenHome"
    const val SCREEN_EDIT_ROUTE = "screenEdit"
    const val SCREEN_SHOW_ROUTE = "screenShow"

    // For routes with arguments
    const val SCREEN_EDIT_ARG_DATA_FROM_HOME = "dataFromHome"
    const val SCREEN_SHOW_ARG_ORIGINAL_URI = "originalImageUri"
    const val SCREEN_SHOW_ARG_QUALITY = "compressionQuality"
    const val SCREEN_SHOW_ARG_SIZE_OF_IMAGE = "sizeOfImage"

    const val SCREEN_SHOW_NAV_ROUTE = "$SCREEN_SHOW_ROUTE/{$SCREEN_SHOW_ARG_ORIGINAL_URI}/{$SCREEN_SHOW_ARG_QUALITY}/{$SCREEN_SHOW_ARG_SIZE_OF_IMAGE}"
}