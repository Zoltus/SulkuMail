package fi.sulku.sulkumail

interface Platform {
    val name: String
    val isMobile : Boolean
    val isJvm : Boolean
}

expect fun getPlatform(): Platform