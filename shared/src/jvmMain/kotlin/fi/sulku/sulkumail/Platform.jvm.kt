package fi.sulku.sulkumail

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val isMobile: Boolean = false
    override val isJvm: Boolean = true
}

actual fun getPlatform(): Platform = JVMPlatform()
