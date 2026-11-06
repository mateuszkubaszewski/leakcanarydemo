plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.leakcanarydemo"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.leakcanarydemo"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)

    // Android Studio Profiler integration for LeakCanary (pulls com.squareup.leakcanary:leakcanary-android transitively).
    debugImplementation(libs.leakcanary)
}
