package com.nedaluof.mihawk.mipreferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.nedaluof.mihawk.milogger.MiUnitTestLoggerImpl
import com.nedaluof.mihawk.miutil.MiServiceLocator
import com.nedaluof.mihawk.miserializer.MiSerializerImpl
import com.nedaluof.mihawk.mipreparation.MiPreparationImpl
import com.nedaluof.mihawk.testutil.FakeDataStore
import com.nedaluof.mihawk.testutil.FakeMiEncryption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MiPreferencesImplTest {

  //region fixtures
  private data class User(val name: String = "Nedal", val age: Int = 29)

  private val dataStore = FakeDataStore()
  private val preferences = MiPreferencesImpl(
    dataStore,
    MiPreparationImpl(MiSerializerImpl(Gson()), FakeMiEncryption()),
    MiUnitTestLoggerImpl,
    Dispatchers.Unconfined
  )

  @Before
  fun setUp() {
    MiServiceLocator.isLoggerEnabled = false
  }

  @After
  fun tearDown() {
    MiServiceLocator.isLoggerEnabled = true
  }

  private fun <T> get(key: String, aClass: Class<T>): T? =
    runBlocking { preferences.getData(key, aClass).first() }

  private fun remove(key: String): Boolean {
    var removed = false
    runBlocking { preferences.removeData(key) { removed = it } }
    return removed
  }

  private fun contains(key: String): Boolean {
    var exists = false
    runBlocking { preferences.contains(key) { exists = it } }
    return exists
  }

  private fun deleteAll(): Boolean {
    var deleted = false
    runBlocking { preferences.deleteAll { deleted = it } }
    return deleted
  }
  //endregion

  //region put / get round trips
  @Test
  fun `put and get string`() {
    preferences.putData("user_name", "nedaluOf")
    assertEquals("nedaluOf", get("user_name", String::class.java))
  }

  @Test
  fun `put and get int`() {
    preferences.putData("age", 29)
    assertEquals(29, get("age", Int::class.java))
  }

  @Test
  fun `put and get boolean`() {
    preferences.putData("is_free", true)
    assertEquals(true, get("is_free", Boolean::class.java))
  }

  @Test
  fun `put and get double`() {
    preferences.putData("score", 1995.5)
    assertEquals(1995.5, get("score", Double::class.java))
  }

  @Test
  fun `put and get object`() {
    preferences.putData("user", User())
    assertEquals(User(), get("user", User::class.java))
  }

  @Test
  fun `put and get list`() {
    val names = listOf("name", "phone", "email")
    preferences.putData("names", names)
    val result = get("names", Array<String>::class.java)?.toList()
    assertEquals(names, result)
  }

  @Test
  fun `put overwrites previous value`() {
    preferences.putData("age", 29)
    preferences.putData("age", 30)
    assertEquals(30, get("age", Int::class.java))
  }
  //endregion

  //region raw values migrated from SharedPreferences
  @Test
  fun `get migrated raw string`() {
    dataStore.seed(stringPreferencesKey("user_name"), "NedaluOf")
    assertEquals("NedaluOf", get("user_name", String::class.java))
  }

  @Test
  fun `get migrated raw int`() {
    dataStore.seed(intPreferencesKey("age"), 29)
    assertEquals(29, get("age", Int::class.java))
  }

  @Test
  fun `get migrated raw boolean`() {
    dataStore.seed(booleanPreferencesKey("is_free"), true)
    assertEquals(true, get("is_free", Boolean::class.java))
  }

  @Test
  fun `put after migration replaces raw value`() {
    dataStore.seed(intPreferencesKey("age"), 29)
    preferences.putData("age", 30)
    assertEquals(30, get("age", Int::class.java))
  }
  //endregion

  //region missing keys
  @Test
  fun `get missing key returns null`() {
    assertNull(get("missing", String::class.java))
  }

  @Test
  fun `get missing key returns null for typed values`() {
    assertNull(get("missing", Int::class.java))
  }
  //endregion

  //region remove / contains / deleteAll
  @Test
  fun `remove deletes stored value`() {
    preferences.putData("user_name", "nedaluOf")
    assertTrue(remove("user_name"))
    assertNull(get("user_name", String::class.java))
  }

  @Test
  fun `contains detects stored key`() {
    preferences.putData("user_name", "nedaluOf")
    assertTrue(contains("user_name"))
    assertFalse(contains("missing"))
  }

  @Test
  fun `deleteAll clears every key`() {
    preferences.putData("user_name", "nedaluOf")
    preferences.putData("age", 29)
    assertTrue(deleteAll())
    assertNull(get("user_name", String::class.java))
    assertNull(get("age", Int::class.java))
  }
  //endregion
}
