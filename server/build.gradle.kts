import java.util.Properties

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinxSerialization)
    application
    alias(libs.plugins.buildConfig)
}

buildConfig {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())
    buildConfigField("GOOGLE_API_SECRET", properties.getProperty("GOOGLE_API_SECRET"))
}

group = "fi.sulku.sulkumail"
version = "1.0.0"
application {
    mainClass.set("fi.sulku.sulkumail.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.bundles.common)
    implementation(libs.bundles.server)
}