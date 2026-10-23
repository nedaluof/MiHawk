import org.gradle.api.publish.maven.MavenPublication

plugins {
  alias(libs.plugins.android.library)
  `maven-publish`
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

  publishing {
    singleVariant("release") {
      withSourcesJar()
    }
  }
}

afterEvaluate {
  publishing {
    publications {
      register<MavenPublication>("release") {
        from(components["release"])
        groupId = "com.github.nedaluof"
        artifactId = "MiHawk"
        version = (findProperty("version") as String?) ?: libs.versions.version.name.get()
      }
    }
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