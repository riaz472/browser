package acr.browser.lightning.preference

import android.app.Application
import android.content.Context
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Persists the user's chosen homepage wallpaper using SharedPreferences.
 * The image is copied into internal storage so access never expires.
 */
@Singleton
class WallpaperPreferenceManager @Inject constructor(
    private val application: Application
) {

    private val prefs by lazy {
        application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /** Persists the absolute path to the copied wallpaper file. */
    fun saveWallpaperPath(path: String) {
        prefs.edit().putString(KEY_WALLPAPER_PATH, path).apply()
    }

    /** Returns the saved path, or null if no wallpaper has been set. */
    fun getWallpaperPath(): String? = prefs.getString(KEY_WALLPAPER_PATH, null)

    /** Deletes the stored image and removes the preference entry. */
    fun clearWallpaper() {
        getWallpaperPath()?.let { File(it).delete() }
        prefs.edit().remove(KEY_WALLPAPER_PATH).apply()
    }

    /** Returns the canonical destination file for the copied wallpaper image. */
    fun getWallpaperFile(): File = File(application.filesDir, WALLPAPER_FILE_NAME)

    companion object {
        private const val PREFS_NAME = "nexus_wallpaper_prefs"
        private const val KEY_WALLPAPER_PATH = "wallpaper_path"
        private const val WALLPAPER_FILE_NAME = "nexus_wallpaper.jpg"
    }
}
