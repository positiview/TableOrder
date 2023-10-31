plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")

    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}



android {
    namespace = "com.example.tableorder"
    compileSdk = 34
    viewBinding { enable = true }

    defaultConfig {
        applicationId = "com.example.tableorder"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    //구글디펜던시추가
    implementation("com.google.android.gms:play-services-maps:18.0.1")
    implementation("com.google.android.gms:play-services-location:19.0.1")

    //Navigation
    implementation ("androidx.navigation:navigation-fragment-ktx:2.5.0")
    implementation ("androidx.navigation:navigation-ui-ktx:2.5.0")
    //circle image view
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //hiltdagger
    implementation ("com.google.dagger:hilt-android:2.44")
//    kapt ("com.google.dagger:hilt-android-compiler:2.40.5")
    //Glide
    implementation ("com.github.bumptech.glide:glide:4.13.0")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
}