package acr.browser.lightning.preference

import android.app.Application
import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Persists user-defined custom CSS and JavaScript strings that are injected
 * into every page loaded by the browser.
 */
@Singleton
class UserScriptsPreferenceManager @Inject constructor(
    application: Application
) {

    private val prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getCustomCss(): String = prefs.getString(KEY_CUSTOM_CSS, "") ?: ""

    fun setCustomCss(css: String) {
        prefs.edit().putString(KEY_CUSTOM_CSS, css).apply()
    }

    fun getCustomJs(): String = prefs.getString(KEY_CUSTOM_JS, "") ?: ""

    fun setCustomJs(js: String) {
        prefs.edit().putString(KEY_CUSTOM_JS, js).apply()
    }

    companion object {
        private const val PREFS_NAME = "nexus_user_scripts_prefs"
        private const val KEY_CUSTOM_CSS = "custom_css"
        private const val KEY_CUSTOM_JS = "custom_js"
    }
}
