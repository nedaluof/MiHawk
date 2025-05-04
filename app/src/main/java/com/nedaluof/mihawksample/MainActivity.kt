package com.nedaluof.mihawksample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.nedaluof.mihawk.MiHawk

/**
 * Created by NedaluOf on 11/17/2021.
 */
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = stringResource(R.string.app_name),
          fontSize = 26.sp
        )
      }
    }
    processInt()
    processDouble()
    processString()
    processStringList()
    processObject()
    processObjectList()
    processDeleteOfAllEntries()
  }

  private fun processInt() {
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

  private fun processDouble() {
    val key = "double"
    val value = 1995.5
    MiHawk.put(key, value)
    MiHawk.get<Double>(key) {
      it?.let {
        Log.e("MY_LOGGER", "processDouble $it")
      }
    }
  }

  private fun processString() {
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

  private fun processStringList() {
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

  private fun processObject() {
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

  private fun processObjectList() {
    data class Item(
      val name: String = "Colombian Coffee",
      val expDate: String = "2030/05/04"
    )

    val key = "object-list"
    val value = listOf(
      Item(),
      Item("Matrix Cola Diet", "2026/05/04"),
      Item("Toast-Bread", "2025/05/04")
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

  private fun processDeleteOfAllEntries() {
    MiHawk.deleteAll {
      Log.e("MY_LOGGER", "delete result $it")
    }
  }
}