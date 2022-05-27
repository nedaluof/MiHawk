package com.nedaluof.mihawksample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.nedaluof.mihawk.MiHawk
import com.nedaluof.mihawk.milogger.MiLogger

/**
 * Created by NedaluOf on 11/17/2021.
 */
class MainActivity : AppCompatActivity() {

  /*Todo: UI needed to make some test that depend on the inputs from user*/
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    //MiHawk.init(this)
    initMiHawk()
    processInt()
    processDouble()
    processString()
    processStringList()
    processObject()
    processObjectList()
    processDeleteOfAllEntries()
  }

  private fun initMiHawk() {
    MiHawk.Builder()
      .withContext(this)
      .withPreferenceName("NEDAL_PREFS")
      .withMiLogger(object : MiLogger {
        override fun info(message: String) {
          Log.i("MY_LOGGER" ,message )
        }

        override fun error(message: String) {
          Log.e("MY_LOGGER" ,message )
        }

        override fun debug(message: String) {
          Log.d("MY_LOGGER" ,message )
        }
      })
      .build()
  }

  private fun processInt() {
    val key = "int"
    val value = 1995
    MiHawk.put(key, value)
    MiHawk.get<Int>(key) {
      it?.let {
        Log.d("MY_LOGGER" ,"processInt $it")
      }
    }
  }

  private fun processDouble() {
    val key = "double"
    val value = 1995.5
    MiHawk.put(key, value)
    MiHawk.get<Double>(key) {
      it?.let {
        Log.d("MY_LOGGER" ,"processDouble $it")
      }
    }
  }

  private fun processString() {
    val key = "string"
    val value = "nedaluOf"
    MiHawk.put(key, value)
    MiHawk.get<String>(key) {
      it?.let {
        Log.d("MY_LOGGER" ,"processString $it")
      }
    }
  }

  private fun processStringList() {
    val key = "string-list"
    val value = listOf("nedaluOf", "AhmaduOf", "MohammaduOf")
    MiHawk.put(key, value)
    MiHawk.get<List<String>>(key) {
      it?.let {
        Log.d("MY_LOGGER" , "processStringList $it")
      }
    }
  }

  private fun processObject() {
    data class Test(val test: String = "test", val age: Int = 26)

    val key = "object"
    val value = Test()
    MiHawk.put(key, value)
    MiHawk.get<Test>(key) {
      it?.let {
        Log.d("MY_LOGGER" ,"processObject $it")
      }
    }
  }

  private fun processObjectList() {
    data class Test(val test: String = "test", val age: Int = 26)

    val key = "object-list"
    val value = listOf(Test("nedaluOf"), Test("AhmaduOf", 18), Test("MohammaduOf", 24))
    MiHawk.put(key, value)
    MiHawk.get<List<Test>>(key) {
      it?.let {
        Log.d("MY_LOGGER" ,"processObjectList $it")
      }
    }
  }

  private fun processDeleteOfAllEntries() {
    MiHawk.deleteAll {
      Log.d("MY_LOGGER" ,"delete result $it")
    }
  }
}