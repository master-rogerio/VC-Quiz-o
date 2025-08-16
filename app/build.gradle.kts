configurations.all {
    // Exclui globalmente a biblioteca de anotações antiga e problemática
    exclude(group = "com.intellij", module = "annotations")
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.vcquizo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.vcquizo"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
  //  composeOptions {
  //      kotlinCompilerExtensionVersion = "1.5.8"
  //  }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime.android)
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Firebase (as versões serão geridas pelo BOM)
    implementation(platform(libs.firebase.bom))
    // implementation("com.google.firebase:firebase-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")      // Para autenticação
    implementation("com.google.firebase:firebase-database-ktx")  // Para os Quizzes
    implementation("com.google.firebase:firebase-firestore-ktx") // Para o Ranking e Utilizadores

    // DataStore (Preferences)
    implementation ("androidx.datastore:datastore-preferences:1.1.0")

    // Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    // Lifecycle (ViewModel/LiveData)
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    //UI
    implementation(libs.androidx.material.icons.extended)
    implementation("androidx.compose.foundation:foundation:1.8.3")

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    annotationProcessor("androidx.room:room-compiler:2.7.2") {
        exclude(group = "com.intellij", module = "annotations")
    }
}