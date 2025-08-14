package com.example.compressify.ui.viewmodel.theme

import android.app.Application // Import Application
import androidx.lifecycle.AndroidViewModel // Import AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.compressify.ui.theme.ThemeSettings // Assuming ThemePreferences is in this package or accessible
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Change ViewModel to AndroidViewModel and constructor to take Application
class ThemeViewModel(application: Application) : AndroidViewModel(application) {

    private val _theme = MutableStateFlow(ThemeSettings.SYSTEM)
    val theme: StateFlow<ThemeSettings> = _theme

    // Access the application context when needed
    private val appContext = getApplication<Application>()

    init {
        viewModelScope.launch {
            // Use appContext here
            ThemePreferences.getTheme(appContext).collect {
                _theme.value = it
            }
        }
    }

    fun updateTheme(newTheme: ThemeSettings) {
        viewModelScope.launch {
            // Use appContext here
            ThemePreferences.saveTheme(appContext, newTheme)
        }
    }
}
