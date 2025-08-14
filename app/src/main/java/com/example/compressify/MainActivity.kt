package com.example.compressify

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.compressify.ui.navigation.AppNavigation
import com.example.compressify.ui.theme.MyAppTheme
import com.example.compressify.ui.viewmodel.theme.ThemeViewModel
import kotlinx.coroutines.delay
import androidx.compose.runtime.remember
import com.example.compressify.ui.theme.ThemeSettings

class MainActivity : ComponentActivity() {
    private var keepSplashOnScreen by mutableStateOf(true)
    private val splashDisplayDuration = 1500L

    val themeViewModel: ThemeViewModel by viewModels()

// painterResource(R.drawable.setting)




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Splash Screen
         * */
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }
        splashScreen.setOnExitAnimationListener{splashScreenViewProvider ->
            val splashView = splashScreenViewProvider.view
            val iconView = splashScreenViewProvider.iconView


            // Animator for the entire splash screen view (e.g., fade out)
            val splashViewFadeOut = ObjectAnimator.ofFloat(
                splashView, View.ALPHA, 1f, 0f
            ).apply {
                interpolator = DecelerateInterpolator()
                duration = 500L // Duration for splash screen to fade out
                doOnEnd {
                    splashScreenViewProvider.remove() // Remove splash screen when done
                }
            }

            if (iconView != null) {
                // Animation for the icon (Scale In -> Scale Out & Fade Out)

                // 1. Initial Scale (if needed, e.g. to ensure it starts at 1.0 before custom animation)
                //    iconView.scaleX = 1.0f
                //    iconView.scaleY = 1.0f

                // 2. Scale In phase (shrink a bit)
                val scaleInX = ObjectAnimator.ofFloat(iconView, View.SCALE_X, 1.0f, 0.8f)
                val scaleInY = ObjectAnimator.ofFloat(iconView, View.SCALE_Y, 1.0f, 0.8f)
                val scaleInSet = AnimatorSet().apply {
                    playTogether(scaleInX, scaleInY)
                    duration = 300L // Duration for shrinking
                    interpolator = AccelerateDecelerateInterpolator()
                }

                // 3. Scale Out and Fade Out phase (grow larger and disappear)
                val scaleOutX = ObjectAnimator.ofFloat(iconView, View.SCALE_X, 0.8f, 1.5f)
                val scaleOutY = ObjectAnimator.ofFloat(iconView, View.SCALE_Y, 0.8f, 1.5f)
                val fadeOutIcon = ObjectAnimator.ofFloat(iconView, View.ALPHA, 1.0f, 0.0f)
                val scaleOutFadeOutSet = AnimatorSet().apply {
                    playTogether(scaleOutX, scaleOutY, fadeOutIcon)
                    duration = 400L // Duration for growing and fading
                    interpolator = AccelerateDecelerateInterpolator()
                }

                // Chain the animations: Scale In, then Scale Out & Fade Out
                val iconAnimationSequence = AnimatorSet()
                iconAnimationSequence.play(scaleInSet).before(scaleOutFadeOutSet)
                //Delay the start of the icon animation slightly if needed to sync with splash view fade
                //iconAnimationSequence.startDelay = 100L

                // Start all animations
                iconAnimationSequence.start()
                splashViewFadeOut.start() // Start splash background fade simultaneously or slightly delayed

            } else {
                // If no icon, just fade out the splash view
                splashViewFadeOut.start()
            }
        }





        enableEdgeToEdge()
        setContent {
            val currentTheme by themeViewModel.theme.collectAsState()

            Log.d("ThemeCheck", "MainActivity VM hash: ${themeViewModel.hashCode()}, Theme for MyAppTheme: $currentTheme")
            LaunchedEffect(Unit) {
                delay(splashDisplayDuration)
                keepSplashOnScreen = false
            }

            MyAppTheme (themeSetting = currentTheme) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

