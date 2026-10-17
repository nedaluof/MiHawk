package com.nedaluof.mihawk.mifacade

import com.nedaluof.mihawk.mipreferences.MiPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MiHawkFacadeImplTest {

  //region fixtures
  private class FakeMiPreferences : MiPreferences {
    val putCalls = mutableListOf<Pair<String, Any?>>()
    var removedKey: String? = null
    var deleted = false

    override fun <T> putData(key: String, value: T) {
      putCalls += key to value
    }

    override fun <T> getData(key: String, aClass: Class<T>): Flow<T?> = flowOf(null)

    override suspend fun removeData(key: String, result: (Boolean) -> Unit) {
      removedKey = key
      result(true)
    }

    override fun deleteAll(result: (Boolean) -> Unit) {
      deleted = true
      result(true)
    }

    override fun contains(key: String, result: (Boolean) -> Unit) {
      result(key == "existing")
    }
  }

  private val fakePreferences = FakeMiPreferences()
  private val facade = MiHawkFacadeImpl(fakePreferences)
  //endregion

  //region delegation
  @Test
  fun `putData delegates key and value`() {
    facade.putData("user_name", "nedaluOf")
    assertEquals(listOf("user_name" to "nedaluOf"), fakePreferences.putCalls)
  }

  @Test
  fun `getData delegates to preferences`() {
    val result = runBlocking { facade.getData("user_name", String::class.java).first() }
    assertEquals(null, result)
  }

  @Test
  fun `removeData invokes callback`() {
    var removed = false
    facade.removeData("user_name") { removed = it }
    assertEquals("user_name", fakePreferences.removedKey)
    assertTrue(removed)
  }

  @Test
  fun `contains reports key existence`() {
    var exists = false
    facade.contains("existing") { exists = it }
    assertTrue(exists)

    facade.contains("missing") { exists = it }
    assertFalse(exists)
  }

  @Test
  fun `deleteAll invokes callback`() {
    var deleted = false
    facade.deleteAll { deleted = it }
    assertTrue(fakePreferences.deleted)
    assertTrue(deleted)
  }
  //endregion
}
