plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
}

android {
  namespace = "com.nedaluof.mihawksample"
  compileSdk = Integer.valueOf(libs.versions.target.sdk.get())

  defaultConfig {
    applicationId  = "com.nedaluof.mihawksample"
    minSdk = Integer.valueOf(libs.versions.min.sdk.get())
    targetSdk = Integer.valueOf(libs.versions.target.sdk.get())
    versionCode = Integer.valueOf(libs.versions.version.number.get())
    versionName = libs.versions.version.name.get()
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
  buildFeatures {
    compose = true
  }
}

dependencies {
  //mihawk
  api(project(":mihawk"))
  //core ktx
  implementation(libs.androidx.core.ktx)
  //compose
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.viewmodel)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)
  //testing
  testImplementation (libs.junit)
  androidTestImplementation (libs.androidx.test.runner)
  debugImplementation(libs.androidx.ui.test.manifest)
}