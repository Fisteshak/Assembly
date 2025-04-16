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
    // packageOfResClass = "org.sample.library.resources"
    generateResClass = always
}

kotlin {
    jvm("desktop")


    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
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
          //  implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            // mvikotlin
            implementation(libs.mvikotlin)
            implementation(libs.mvikotlin.main)
            implementation(libs.mvikotlin.extensions.coroutines)
            // serialization
            implementation(libs.serialization)
            //navigation
            implementation(libs.navigation)

        }

        wasmJsMain.dependencies {


        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            // for previews
            implementation("org.jetbrains.compose.ui:ui-tooling-preview-desktop:1.7.3")
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

