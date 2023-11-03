// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.1" apply false
<<<<<<< HEAD
    id ("com.android.library") version "7.4.2" apply false

    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
=======
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version  "4.4.0" apply false
>>>>>>> 9df6c260fbe896d42a275eaae091c2da26bfb349
}
buildscript {
    dependencies {
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
<<<<<<< HEAD
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.0")
    }
}
=======
        classpath("com.google.gms:google-services:4.4.0")
    }
}
>>>>>>> 9df6c260fbe896d42a275eaae091c2da26bfb349
