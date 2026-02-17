plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}
android {
    namespace = "com.example.myapplication"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 34
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8" // Phải tương thích với phiên bản Kotlin bạn dùng
    }
}
dependencies {
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.compose.material:material-icons-extended:1.6.0")
    implementation(libs.androidx.runtime)
    // 1. Compose BOM - Quản lý phiên bản tập trung
    val composeBom = platform("androidx.compose:compose-bom:2024.02.00") // Bạn có thể cập nhật bản mới nhất
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // 2. Core Libraries cho Compose
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3") // Sử dụng Material Design 3
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // 3. Integration với Activity và ViewModel
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")



    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation("com.google.dagger:hilt-android:2.57.1")
    ksp("com.google.dagger:hilt-android-compiler:2.57.1")

    // Media3 - Playback & Session
    val media3Version = "1.2.1" // Cập nhật bản mới nhất nếu có
    implementation("androidx.media3:media3-exoplayer:$media3Version")
    implementation("androidx.media3:media3-session:$media3Version")
    implementation("androidx.media3:media3-ui:$media3Version")

    // Jetpack Compose (Dành cho giao diện hiện đại)
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}