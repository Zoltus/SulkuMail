import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.buildConfig)
}

buildConfig {
    // https://github.com/gmazzo/gradle-buildconfig-plugin#usage-in-kts
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())
    buildConfigField("GOOGLE_CLIENT_ID", properties.getProperty("GOOGLE_CLIENT_ID"))
    buildConfigField("GOOGLE_REDIRECT_URL", properties.getProperty("GOOGLE_REDIRECT_URL"))
    buildConfigField("BACKEND_URL", properties.getProperty("BACKEND_URL"))
    useKotlinOutput { internalVisibility = false }   // adds `internal` modifier to all declarations
    /*
    useKotlinOutput()                               // forces the outputType to 'kotlin', generating an `object`
    useKotlinOutput { topLevelConstants = true }    // forces the outputType to 'kotlin', generating top-level declarations
     */
}

subprojects {
    repositories {
        google()
        mavenCentral()
    }
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.bundles.common)
            implementation(libs.bundles.shared)
        }
        jvmMain.dependencies {}
    }
}

android {
    namespace = "fi.sulku.sulkumail.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
