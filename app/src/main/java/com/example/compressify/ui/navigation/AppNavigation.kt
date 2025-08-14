package com.example.compressify.ui.navigation

import android.net.Uri
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.compressify.ui.view.EditScreen
import com.example.compressify.ui.view.HomeScreen
import com.example.compressify.ui.view.ShowScreen

@Composable
fun AppNavigation(modifier: Modifier){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestinations.SCREEN_HOME_ROUTE,
    ){
        // Screen A
        composable(
            route = AppDestinations.SCREEN_HOME_ROUTE
        ){
            HomeScreen(navController = navController, modifier = modifier)
        }

        // Screen B - Receives data from Screen A
        composable(
            route = "${AppDestinations.SCREEN_EDIT_ROUTE}/{${AppDestinations.SCREEN_EDIT_ARG_DATA_FROM_HOME}}",
            arguments = listOf(
                navArgument(AppDestinations.SCREEN_EDIT_ARG_DATA_FROM_HOME) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val dataFromA = backStackEntry.arguments?.getString(AppDestinations.SCREEN_EDIT_ARG_DATA_FROM_HOME)
            EditScreen(navController = navController, dataFromA = dataFromA, modifier = modifier)
        }


        // Screen C - Receives data from Screen B
        composable(
            route = AppDestinations.SCREEN_SHOW_NAV_ROUTE,
            arguments = listOf(
                navArgument(AppDestinations.SCREEN_SHOW_ARG_ORIGINAL_URI) { type = NavType.StringType },
                navArgument(AppDestinations.SCREEN_SHOW_ARG_QUALITY) { type = NavType.FloatType },
                navArgument(AppDestinations.SCREEN_SHOW_ARG_SIZE_OF_IMAGE) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val originalImageUriString = backStackEntry.arguments?.getString(AppDestinations.SCREEN_SHOW_ARG_ORIGINAL_URI)
            val compressionQuality = backStackEntry.arguments?.getFloat(AppDestinations.SCREEN_SHOW_ARG_QUALITY)
            val sizeOfImage = backStackEntry.arguments?.getInt(AppDestinations.SCREEN_SHOW_ARG_SIZE_OF_IMAGE)

            val decodedOriginalUri = originalImageUriString?.let { Uri.decode(it) }?.let { Uri.parse(it) }

            if (decodedOriginalUri != null && compressionQuality != null && sizeOfImage != null) {
                ShowScreen(
                    navController = navController,
                    originalImageUri = decodedOriginalUri,
                    compressionQuality = compressionQuality.toInt(),
                    sizeOfImage = sizeOfImage,
                    modifier = modifier
                )
            } else {
                Text("Error: Missing image URI or quality for ScreenShow.")
                LaunchedEffect(Unit) { navController.popBackStack() }
            }
        }
    }
}