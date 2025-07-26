import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
}

repositories {
    maven("https://jogamp.org/deployment/maven") // For compose webview https://github.com/KevinnZou/compose-webview-multiplatform/blob/main/README.desktop.md#dependencies
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    // todo linuxX64() //linuxArm64()?

    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared) //todo?
            implementation(libs.bundles.common)
            implementation(libs.bundles.frontend)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)
            api(libs.koin.core)
        }
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.bundles.android)
        }
        appleMain.dependencies {
            implementation(libs.bundles.ios)
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.bundles.desktop)
            }
        }
    }
}


room { schemaDirectory("$projectDir/schemas") }

dependencies {
    ksp(libs.room.compiler)
    /*    add("kspDesktop", libs.room.compiler)
        add("kspAndroid", libs.room.compiler)
        add("kspIosX64", libs.room.compiler)
        add("kspIosSimulatorArm64", libs.room.compiler)
        add("kspIosArm64", libs.room.compiler)*/
}

android {
    namespace = "fi.sulku.sulkumail"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "fi.sulku.sulkumail"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

compose.desktop {
    application {
        mainClass = "fi.sulku.sulkumail.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "fi.sulku.sulkumail"
            packageVersion = "1.0.0"
        }

        // For compose webview https://github.com/KevinnZou/compose-webview-multiplatform/blob/main/README.desktop.md#dependencies
        jvmArgs("--add-opens", "java.desktop/sun.awt=ALL-UNNAMED")
        jvmArgs("--add-opens", "java.desktop/java.awt.peer=ALL-UNNAMED")

        if (System.getProperty("os.name").contains("Mac")) {
            jvmArgs("--add-opens", "java.desktop/sun.lwawt=ALL-UNNAMED")
            jvmArgs("--add-opens", "java.desktop/sun.lwawt.macosx=ALL-UNNAMED")
        }
    }
}
