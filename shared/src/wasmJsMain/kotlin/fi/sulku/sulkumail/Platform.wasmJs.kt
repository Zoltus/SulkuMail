package fi.sulku.sulkumail

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
    override val isMobile: Boolean = false
    override val isJvm: Boolean = false
}

actual fun getPlatform(): Platform = WasmPlatform()