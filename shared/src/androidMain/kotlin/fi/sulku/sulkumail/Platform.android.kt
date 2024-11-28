package fi.sulku.sulkumail

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val isMobile: Boolean = true
    override val isJvm: Boolean = true
}

actual fun getPlatform(): Platform = AndroidPlatform()