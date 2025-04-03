plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.eventease"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.eventease"
        minSdk = 24
        targetSdk = 35
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
    //to avoid conflict between android-mail and android-activation libraries both include the same file (META-INF/NOTICE.md)
    packagingOptions {
        exclude ("META-INF/NOTICE.md");
        exclude ("META-INF/LICENSE.md")

        exclude ("META-INF/DEPENDENCIES")
        exclude ("META-INF/LICENSE")
        exclude ("META-INF/LICENSE.txt")
        exclude ("META-INF/NOTICE")
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
    implementation("com.github.bumptech.glide:glide:4.12.0") //for Glide
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0") //for Glide

    // Added dependency for RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.1")


// SendGrid Java SDK - version 4.7.0
    implementation("com.sendgrid:sendgrid-java:4.7.0") {
        exclude(group = "org.apache.httpcomponents", module = "httpclient") // Exclude the HttpClient module from SendGrid to avoid conflicts
    }

    // Apache HttpClient dependencies - latest versions
//    implementation("org.apache.httpcomponents:httpclient:4.5.13")  // Make sure this is compatible with your project needs
//    implementation("org.apache.httpcomponents:httpcore:4.4.13")    // Required for HttpClient

    // javax.mail for sending emails
    implementation("com.sun.mail:javax.mail:1.6.2") // Optional: For email handling (optional based on your use case)



}