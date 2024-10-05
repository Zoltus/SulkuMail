plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

group = "fi.sulku.sulkumail"
version = "1.0.0"
application {
    mainClass.set("fi.sulku.sulkumail.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.client.core)
    //todo cio or some other and serializations ect
    /*
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.contentnegotiation)
    implementation(libs.dotenv.kotlin)
     */
    //testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)
}