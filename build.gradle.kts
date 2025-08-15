// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    //alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}

subprojects {
    configurations.all {
        resolutionStrategy {
            force ("org.jetbrains:annotations:23.0.0")
        }
    }
    // força também em processadores de anotação
    configurations.matching { it.name.contains("kapt") || it.name.contains("annotationProcessor") }.all {
        resolutionStrategy {
            force ("org.jetbrains:annotations:23.0.0")
        }
    }
}

