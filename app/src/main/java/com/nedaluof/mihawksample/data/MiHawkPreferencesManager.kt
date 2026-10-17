package com.nedaluof.mihawksample.data

import android.util.Log
import com.nedaluof.mihawk.MiHawk

/**
 * Created By NedaluOf - 19/09/2025.
 */
class MiHawkPreferencesManager {

  //region logic
  fun getValuesFromOldPreferences(){
    Log.e("MY_LOGGER", """
      Values
      ${MiHawk.get<String>(PreferencesKeys.USER_NAME)}
      ${MiHawk.get<Int>(PreferencesKeys.USER_AGE)}
      ${MiHawk.get<Boolean>(PreferencesKeys.USER_IS_FREE)}
    """.trimIndent())
  }

  fun processInt() {
    val key = "int"
    val value = 1995
    MiHawk.put(key, value)
    MiHawk.get<Int>(key) {
      it?.let {
        Log.e("MY_LOGGER", "processInt $it")
      }
    }
    val int = MiHawk.get<Int>(key)
    Log.e("MY_LOGGER", "processInt $int")
  }

  fun processDouble() {
    val key = "double"
    val value = 1995.5
    MiHawk.put(key, value)
    MiHawk.get<Double>(key) {
      it?.let {
        Log.e("MY_LOGGER", "processDouble $it")
      }
    }
  }

  fun processString() {
    val key = "string"
    val value = "nedaluOf"
    //set key and value
    MiHawk.put(key, value)
    //get stored value
    MiHawk.get<String>(key) {
      it?.let {
        Log.e("MY_LOGGER", "processString $it")
      }
    }
  }

  fun processStringList() {
    val key = "my_strings_list"
    val value = listOf("name", "phone", "email")
    //set key and value
    MiHawk.put(key, value)
    //get stored value
    MiHawk.get<List<String>>(key) {
      it?.let {
        Log.e("MY_LOGGER", "my strings $it")
      }
    }
  }

  fun processObject() {
    data class User(val name: String = "Nedal", val email: String = "nidal.hassan.95@gmail.com")

    val key = "my_user"
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
  }

  fun processObjectList() {
    data class Item(
      val name: String = "Colombian Coffee", val expDate: String = "2030/05/04"
    )

    val key = "object-list"
    val value = listOf(
      Item(), Item("Matrix Cola Diet", "2026/05/04"), Item("Toast-Bread", "2025/05/04")
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
  }

  fun processDeleteOfAllEntries() {
    MiHawk.deleteAll {
      Log.e("MY_LOGGER", "delete result $it")
    }
  }
  //endregion
}