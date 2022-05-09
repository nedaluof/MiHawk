package com.nedaluof.mihawksample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.nedaluof.mihawk.MiHawk
import com.nedaluof.mihawk.miencryption.MiEncryptionImpl
import com.nedaluof.mihawk.miencryption.MiNoEncryption
import com.nedaluof.mihawk.miutil.MiServiceLocator

/**
 * Created by NedaluOf on 11/17/2021.
 */
class MainActivity : AppCompatActivity() {
  private val logger = MiServiceLocator.provideMiLogger()
 /*Todo: UI needed to make some test that depend on the inputs from user*/
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    MiHawk.init(this)
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
        logger.error("processInt $it")
      }
    }
  }

  private fun processDouble() {
    val key = "double"
    val value = 1995.5
    MiHawk.put(key, value)
    MiHawk.get<Double>(key) {
      it?.let {
        logger.error("processDouble $it")
      }
    }
  }

  private fun processString() {
    val key = "string"
    val value = "nedaluOf"
    MiHawk.put(key, value)
    MiHawk.get<String>(key) {
      it?.let {
        logger.error("processString $it")
      }
    }
  }

  private fun processStringList() {
    val key = "string-list"
    val value = listOf("nedaluOf", "AhmaduOf", "MohammaduOf")
    MiHawk.put(key, value)
    MiHawk.get<List<String>>(key) {
      it?.let {
        logger.error("processStringList $it")
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
        logger.error("processObject $it")
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
        logger.error("processObjectList $it")
      }
    }
  }

  private fun processDeleteOfAllEntries() {
    MiHawk.deleteAll {
      logger.error("delete result $it")
    }
  }
}