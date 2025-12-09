plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.test"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.test"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Square library BOM for unified version management
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))

    // Kotlin/AndroidX Libraries (from version catalog)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Retrofit & Networking (NO individual versions needed)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp) // Version from BOM (4.12.0)
    implementation(libs.logging.interceptor) // Version from BOM

    // Gson (Keep this as it's separate from the BOM)
    implementation(libs.gson)

    // UI Components
    implementation(libs.androidx.recyclerview)
    implementation(libs.coil)

    // Google Services
    implementation(libs.play.services.maps)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}