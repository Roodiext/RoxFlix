package com.viona.roxflix.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LanguageManager {

    private const val PREF_NAME = "app_language"
    private const val KEY_LANG = "lang"

    fun saveLanguage(context: Context, lang: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANG, lang)
            .apply()
    }

    fun getLanguage(context: Context): String {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LANG, "en") ?: "en"
    }

    fun applyLocale(context: Context): Context {
        val lang = getLanguage(context)
        val locale = Locale(lang)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }
}
