import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.serialization)

}



compose.resources {
    publicResClass = true
    generateResClass = always
}

kotlin {
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "21"
        }
    }



    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
//        useCommonJs()
        compilerOptions {
            freeCompilerArgs.add("-Xwasm-attach-js-exception")
        }
        outputModuleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            // mvikotlin
            implementation(libs.mvikotlin)
            implementation(libs.mvikotlin.main)
            implementation(libs.mvikotlin.logging)
            implementation(libs.mvikotlin.extensions.coroutines)
            // serialization
            implementation(libs.serialization)
            // navigation
            implementation(libs.navigation)
            // ktor
            implementation(libs.ktor.client.core)
            implementation("io.ktor:ktor-client-auth:3.1.2")
            implementation("io.ktor:ktor-client-logging:3.1.2")
            implementation("io.ktor:ktor-client-content-negotiation:3.1.2")
            implementation("io.ktor:ktor-serialization-kotlinx-json:3.1.2")
            // koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            // audio player
            implementation("eu.iamkonstantin.kotlin:gadulka:1.6.3")
            val fxSuffix = "win"
            implementation("org.openjfx:javafx-base:21:${fxSuffix}")
            implementation("org.openjfx:javafx-graphics:21:${fxSuffix}")
            implementation("org.openjfx:javafx-controls:21:${fxSuffix}")
            implementation("org.openjfx:javafx-swing:21:${fxSuffix}")
            implementation("org.openjfx:javafx-web:21:${fxSuffix}")
            implementation("org.openjfx:javafx-media:21:${fxSuffix}")

            // For QR codes
            implementation("io.github.alexzhirkevich:qrose:1.0.1")

        }


        wasmJsMain {
            dependencies {
                // ktor js engine
                implementation("io.ktor:ktor-client-js:3.1.2")
                // browser api (for printing)
                implementation("org.jetbrains.kotlinx:kotlinx-browser:0.3")
//                implementation(npm("is-odd", "3.0.1"))
                implementation(npm("jspdf", "3.0.1"))
            }
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            // for previews
            implementation("org.jetbrains.compose.ui:ui-tooling-preview-desktop:1.7.3")
            // ktor jvm engine
            implementation(libs.ktor.client.cio)
            // logging
            implementation("ch.qos.logback:logback-classic:1.5.18")
            // pdf editing
            implementation("org.apache.pdfbox:pdfbox:3.0.5")

        }


    }
}


compose.desktop {
    application {
        mainClass = "com.rtuitlab.assemble.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.rtuitlab.assemble"
            packageVersion = "1.0.0"
        }
    }
}

