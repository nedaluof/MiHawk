<img src="https://github.com/nedaluof/MiHawk/blob/master/art/mihawk_eye.jpg?raw=true" width="150">[![](https://jitpack.io/v/nedaluof/MiHawk.svg)](https://jitpack.io/#nedaluof/MiHawk)
# MiHawk
MiHawk 🦅👁️ is simple and secure 🔒 Android Library to store and retrieve pair of key-value data with encryption , internally it use jetpack DataStore Preferences 💽 to store data. 

Usage
-----

### Dependency

```kotlin DSL
  //In settings.gradle.kts
  pluginManagement {
    repositories {
      maven {
        url = uri("https://jitpack.io")
      }
    }
  }
  dependencyResolutionManagement {
    repositories {
      maven {
        url = uri("https://jitpack.io")
      }
    }
  }

  //Include the library in your build.gradle.YOUR_MODULE
  implementation("com.github.nedaluof:MiHawk:1.3.0")
  //Or by using version catalog
  [versions]
  mihawk = "1.3.0"
  [libraries]
  mihawk = { group = "com.github.nedaluof", name = "MiHawk", version.ref = "mihawk" }
  //then
  implementation(libs.mihawk)

```      

```groovy
//In build.gradle
allprojects {
  repositories {
     maven { url 'https://jitpack.io' }
  }
}

//Include the library in your build.gradle.YOUR_MODULE
dependencies {
   implementation 'com.github.nedaluof:MiHawk:1.3.0'
}

```
#### - Initialize MiHawk:
```kotlin
  /**
   * There are two ways to init MiHawk , in both MiHawk must initialized 
   * before any use of it else RuntimeException will be thrown
   * */
   MiHawk.init(context)
   //OR 
   MiHawk.Builder(context)
      .withPreferenceName("MI_HAWK_PREFS")
      .withLoggingEnabled(false)
      .withMiLogger(MiLogger Implementation)
      .withMiEncryption(MiEncryption Implementation)
      .withMiSerializer(MiSerializer Implementation)
      .build()
```      
#### - Put data to MiHawk:
```kotlin
  MiHawk.put(key: String, value: T)
```
#### - Get data from MiHawk:
 - over result block "Higher-order function"
```kotlin
  MiHawk.get<T>(key: String, result: (T?) -> Unit)
  //OR with default value:
  MiHawk.get<T>(key: String, defaultValue: T,  result: (T?) -> Unit)
``` 
 - direct result 
```kotlin
  MiHawk.get<T>(key: String) : T?
  //OR with default value:
  MiHawk.get<T>(key: String, defaultValue: T) : T? 
``` 
#### - Check if data exist in MiHawk by key:
```kotlin
  MiHawk.contains(key: String, result: (Boolean) -> Unit)
```
#### - Remove data from MiHawk:
```kotlin
  MiHawk.remove(key: String, result: (Boolean) -> Unit)
``` 
#### - Remove all saved data from MiHawk:
```kotlin
  MiHawk.deleteAll(result: (Boolean) -> Unit)
```

## Examples

### put/get Int or any number in MiHawk:
```kotlin
    val key = "my_int_key"
    val value = 2025
    //set key and value
    MiHawk.put(key, value)
    MiHawk.get<Int>(key) {
      it?.let {
        Log.e("MY_LOGGER", "my int $it")
      }
    }
    //Or
    val int = MiHawk.get<Int>(key)
    Log.e("MY_LOGGER", "my int $int")
```

### put/get String in MiHawk:
```kotlin
    val key = "my_string_key"
    val value = "Ny Awesome String"
    //set key and value
    MiHawk.put(key, value)
    //get stored value
    MiHawk.get<String>(key) {
      it?.let {
        Log.e("MY_LOGGER", "my string $it")
      }
    }
    //Or
    val string = MiHawk.get<String>(key)
    Log.e("MY_LOGGER", "my string $string")
```

### put/get String in MiHawk:
```kotlin
    val key = "my_string_list"
    val value = listOf("name","phone","email")
    //set key and value
    MiHawk.put(key, value)
    //get stored value
    MiHawk.get<List<String>>(key) {
      it?.let {
        Log.e("MY_LOGGER", "my strings $it")
      }
    }
    //Or
    val strings = MiHawk.get<List<String>>(key)
    Log.e("MY_LOGGER", "my strings $strings")
```

### put/get Object in MiHawk:
```kotlin
    data class User(val name: String = "Nedal", val email: String = "nidal.hassan.95@gmail.com")
    
    val key = "my_user_object"
    val value = User()
    //set key and value
    MiHawk.put(key, value)
    //get stored value
    MiHawk.get<User>(key) {
      it?.let {
        Log.e("MY_LOGGER", "my user $it")
      }
    }
    //Or
    val user = MiHawk.get<User>(key)
    Log.e("MY_LOGGER", "my user $user")
```

### put/get List of Objects in MiHawk:
```kotlin
    data class Item(
      val name: String = "Colombian Coffee",
      val expDate: String = "2030/05/04"
    )
    
    val key = "object-list"
    val value = listOf(
      Item(),
      Item("Matrix Cola Diet", "2026/05/04"),
      Item("Wholemeal Toast Bread", "2025/05/06")
    )
    //set key and value
    MiHawk.put(key, value)
    //get stored value
    MiHawk.get<List<Item>>(key) {
      it?.let {
        Log.e("MY_LOGGER", "my items $it")
      }
    }
    
    //Or
    val items = MiHawk.get<List<Item>>(key)
    Log.e("MY_LOGGER", "my items $items")
```

<br/>
<br/>

### License

```
Copyright 2021 Nedal Hasan ABDALLAH (NedaluOf)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an 
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
either express or implied. See the License for the specific 
language governing permissions and limitations under the License.

```


~ Inspired from [Hawk](https://github.com/orhanobut/hawk) Library. ~
