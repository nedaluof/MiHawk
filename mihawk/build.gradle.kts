plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
}


android {
  namespace = "com.nedaluof.mihawk"
  compileSdk = Integer.valueOf(libs.versions.target.sdk.get())

  defaultConfig {
    minSdk = Integer.valueOf(libs.versions.min.sdk.get())
    lint.targetSdk = Integer.valueOf(libs.versions.target.sdk.get())
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
  }
}

dependencies {
  //core ktx
  implementation(libs.androidx.core.ktx)
  //GSON
  implementation (libs.gson)
  //coroutines
  implementation (libs.kotlinx.coroutines.android)
  //datastore-preferences
  implementation (libs.androidx.datastore.preferences)
  //testing
  testImplementation (libs.junit)
  androidTestImplementation (libs.androidx.junit.ext)
  testImplementation (libs.androidx.test.core)
}