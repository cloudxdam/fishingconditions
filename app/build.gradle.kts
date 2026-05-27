plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.pachedev.fishingconditions"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.pachedev.fishingconditions"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        val mareaApiKey = providers.gradleProperty("MAREA_API_KEY").orNull ?: ""

        buildConfigField(
            "String",
            "MAREA_API_KEY",
            "\"$mareaApiKey\""
            )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson)
    implementation(libs.logging.interceptor)
}
