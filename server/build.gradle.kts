plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinxSerialization)
    application
}

group = "fi.sulku.sulkumail"
version = "1.0.0"
application {
    mainClass.set("fi.sulku.sulkumail.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared) //todo dont work?
    implementation(libs.bundles.backend)
    implementation(libs.bundles.shared)
}