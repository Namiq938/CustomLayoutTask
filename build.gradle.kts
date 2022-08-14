// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application")  version "7.2.1" apply  false
    id("com.android.library")  version "7.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.7.10" apply false
}

buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${ApplicationVersions.navigationVersion}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.38.1")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}