package com.example.compressify.ui.viewmodel.theme

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.compressify.ui.theme.ThemeSettings
import kotlinx.coroutines.flow.map

val Context.themeDataStore by preferencesDataStore("theme_prefs")

object ThemePreferences {
    private val THEME_KEY = stringPreferencesKey("theme_setting")

    suspend fun saveTheme(context: Context, theme: ThemeSettings) {
        context.themeDataStore.edit { prefs ->
            prefs[THEME_KEY] = theme.name
        }
    }

    fun getTheme(context: Context) = context.themeDataStore.data.map { prefs ->
        prefs[THEME_KEY]?.let { ThemeSettings.valueOf(it) } ?: ThemeSettings.SYSTEM
    }
}