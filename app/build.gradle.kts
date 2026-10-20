import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")

if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use {
        localProperties.load(it)
    }
}

android {
    namespace = "com.pachedev.fishingconditions"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.pachedev.fishingconditions"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions += "distribution"

    productFlavors {
        create("full") {
            dimension = "distribution"
            buildConfigField(
                "String",
                "TIDECHECK_API_KEY",
                "\"${localProperties.getProperty("TIDECHECK_API_KEY") ?: ""}\""
            )
            buildConfigField("boolean", "DEMO_TIDES_DISABLED", "false")
        }
        create("demo") {
            dimension = "distribution"
            applicationIdSuffix = ".demo"
            buildConfigField("String", "TIDECHECK_API_KEY", "\"\"")
            buildConfigField("boolean", "DEMO_TIDES_DISABLED", "true")
        }
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
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    testImplementation(libs.arch.core.testing)
}
