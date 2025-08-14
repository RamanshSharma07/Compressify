package com.example.compressify.ui.view

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compressify.R
import com.example.compressify.data.SelectableOption
import com.example.compressify.ui.theme.MyAppTheme
import com.example.compressify.ui.theme.ThemeSettings
import com.example.compressify.ui.viewmodel.theme.ThemeViewModel

@Composable
fun Settings(
    themeViewModel: ThemeViewModel = viewModel(),
    onBack: () -> Unit
) {
    val currentThemeSetting by themeViewModel.theme.collectAsStateWithLifecycle(initialValue = ThemeSettings.SYSTEM)

    // Define the options for the boxes
    val options = listOf(
        SelectableOption(
            id = ThemeSettings.LIGHT.id,
            label = "Light",
            defaultColor = MaterialTheme.colorScheme.surfaceVariant,
            selectedColor = MaterialTheme.colorScheme.primary,
            defaultContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            selectedContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        SelectableOption(
            id = ThemeSettings.SYSTEM.id,
            label = "System",
            defaultColor = MaterialTheme.colorScheme.surfaceVariant,
            selectedColor = MaterialTheme.colorScheme.primary,
            defaultContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            selectedContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        SelectableOption(
            id = ThemeSettings.DARK.id,
            label = "Dark",
            defaultColor = MaterialTheme.colorScheme.surfaceVariant,
            selectedColor = MaterialTheme.colorScheme.primary,
            defaultContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            selectedContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )

    var selectedOptionId by remember(currentThemeSetting) { // Key the remember to currentThemeSetting
        mutableIntStateOf(
            options.find { it.id == currentThemeSetting.id }?.id ?: ThemeSettings.SYSTEM.id
        )
    }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            SettingsTopAppBar(
                onBack = onBack,
                title = "Settings"
            )

//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ){
//                Text("Coming soon", fontSize = 20.sp)
//            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Theme",
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
//                val boxSize = 80.dp // Define a common size for the boxes
//                val cornerRadius = 16.dp // Define the corner radius
//
//                CurvedBox(
//                    modifier = Modifier.size(boxSize),
//                    color = MaterialTheme.colorScheme.primary,
//                    cornerRadius = cornerRadius,
//                    content = "Box 1"
//                )
//
//                CurvedBox(
//                    modifier = Modifier.size(boxSize),
//                    color = MaterialTheme.colorScheme.secondary,
//                    cornerRadius = cornerRadius,
//                    content = "Box 2"
//                )
//
//                CurvedBox(
//                    modifier = Modifier.size(boxSize),
//                    color = MaterialTheme.colorScheme.tertiary,
//                    cornerRadius = cornerRadius,
//                    content = "Box 3"
//                )

                options.forEach { option ->
                    val isSelected = selectedOptionId == option.id
                    SelectableCurvedBox(
                        modifier = Modifier
                            .weight(1f) // Each box takes equal width
                            .padding(horizontal = 4.dp), // Add some space between boxes
                        option = option,
                        isSelected = isSelected,
                        onSelected = { selectedId ->
                            Log.d("ThemeCheck", "SelectableCurvedBox onSelected called with ID: $selectedId")
                            selectedOptionId = selectedId
                            val newTheme = ThemeSettings.entries.find { it.id == selectedId } ?: ThemeSettings.SYSTEM
                            Log.d("ThemeCheck", "Settings VM hash: ${themeViewModel.hashCode()}, Updating to: $newTheme")
                            themeViewModel.updateTheme(newTheme)
                        },
                        cornerRadius = 16.dp,
                        boxHeight = 70.dp
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Support",
                fontSize = 20.sp
            )

            SupportItems("Share app", R.drawable.share_app)
            SupportItems("Watch adds to support us", R.drawable.watch_adds)
            SupportItems("Remove adds", R.drawable.remove_adds)

        }
    }


}

@Preview
@Composable
fun PreviewSettings () {
    MyAppTheme {
//        Settings {
//
//        }
    }
}
