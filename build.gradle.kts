buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.5")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    //Google FIrebase 초기 설정
    id("com.google.gms.google-services") version "4.3.5" apply false
}