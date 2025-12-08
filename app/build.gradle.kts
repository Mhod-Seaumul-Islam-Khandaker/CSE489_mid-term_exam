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
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp") // Version from BOM (4.12.0)
    implementation("com.squareup.okhttp3:logging-interceptor") // Version from BOM

    // Gson (Keep this as it's separate from the BOM)
    implementation("com.google.code.gson:gson:2.10.1")

    // UI Components
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("io.coil-kt:coil:2.6.0")

    // Google Services
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}