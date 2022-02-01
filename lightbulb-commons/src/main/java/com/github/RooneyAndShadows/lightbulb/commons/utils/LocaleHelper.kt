package com.github.rooneyandshadows.lightbulb.commons.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.os.Build
import java.util.*

class LocaleHelper {
    companion object {
        private const val SELECTED_LANGUAGE_KEY = "Locale.Helper.Selected.Language"

        @JvmStatic
        fun onAttach(context: Context): Context {
            val lang =
                PreferenceUtils.getString(
                    context,
                    SELECTED_LANGUAGE_KEY,
                    Locale.getDefault().language
                )
            return setLocale(context, lang)
        }

        @JvmStatic
        fun onAttach(context: Context, defaultLanguage: String): Context {
            val lang = PreferenceUtils.getString(context, SELECTED_LANGUAGE_KEY, defaultLanguage)
            return setLocale(context, lang)
        }

        @JvmStatic
        fun getLanguage(context: Context): String {
            return PreferenceUtils.getString(
                context,
                SELECTED_LANGUAGE_KEY,
                Locale.getDefault().language
            )
        }

        @JvmStatic
        fun setLocaleToSystemDefault(context: Context): Context {
            val systemDefLanguage = Resources.getSystem().configuration.locales[0].language;
            PreferenceUtils.saveString(context, SELECTED_LANGUAGE_KEY, systemDefLanguage)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                updateResources(context, systemDefLanguage)
            } else updateResourcesLegacy(context, systemDefLanguage)
        }

        @JvmStatic
        fun setLocale(context: Context, language: String): Context {
            PreferenceUtils.saveString(context, SELECTED_LANGUAGE_KEY, language)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                updateResources(context, language)
            } else updateResourcesLegacy(context, language)
        }

        @TargetApi(Build.VERSION_CODES.N)
        private fun updateResources(context: Context, language: String): Context {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val configuration = context.resources.configuration
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)
            return context.createConfigurationContext(configuration)
        }

        @Suppress("DEPRECATION")
        private fun updateResourcesLegacy(context: Context, language: String): Context {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val resources = context.resources
            val configuration = resources.configuration
            configuration.locale = locale
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLayoutDirection(locale)
            }
            resources.updateConfiguration(
                configuration,
                resources.displayMetrics
            )
            return context
        }
    }
}