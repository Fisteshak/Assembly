import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

compose.resources {
    publicResClass = true
    // packageOfResClass = "org.sample.library.resources"
    generateResClass = always
}

kotlin {
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

        commonMain.dependencies {

//            implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.20")
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation("com.arkivanov.mvikotlin:mvikotlin:4.3.0")
            implementation("com.arkivanov.mvikotlin:mvikotlin-main:4.3.0")
            implementation("com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:4.3.0")

        }

        wasmJsMain.dependencies {

            implementation(compose.material3)

        }


    }
}


