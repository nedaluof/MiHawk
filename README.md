<img src="https://github.com/nedaluof/MiHawk/blob/master/art/mihawk_eye.jpg?raw=true" width="150">[![](https://jitpack.io/v/nedaluof/MiHawk.svg)](https://jitpack.io/#nedaluof/MiHawk)
# MiHawk
MiHawk 🦅👁️ is simple and secure 🔒 Android Library to store and retrieve pair of key-value data with encryption , internally it use jetpack DataStore Preferences 💽 to store data. 

Usage
-----

### Dependency

```groovy
//In build.gradle
allprojects {
  repositories {
     maven { url 'https://jitpack.io' }
  }
}

//Include the library in your build.gradle.YOUR_MODULE
dependencies {
   implementation 'com.github.nedaluof:MiHawk:1.1.2'
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

<br/>
<br/>

### - Coming channges / Todos
-----
- [ ] Replace [Facebook Conceal](https://github.com/facebookarchive/conceal) with alternative encryption algorithm.
- [ ] Provide heavy unit test.
- [ ] Provide simple UI that simulate the inputs to test.



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
