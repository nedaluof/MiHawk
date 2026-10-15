package com.nedaluof.mihawk.mipreparation

import com.google.gson.Gson
import com.nedaluof.mihawk.miserializer.MiSerializerImpl
import com.nedaluof.mihawk.testutil.FakeMiEncryption
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MiPreparationImplTest {

  //region fixtures
  private data class User(val name: String = "Nedal", val age: Int = 29)

  private fun preparation(isEncryptionOn: Boolean = true) = MiPreparationImpl(
    MiSerializerImpl(Gson()),
    FakeMiEncryption(isInitialized = isEncryptionOn)
  )
  //endregion

  //region values written by MiHawk (spec-wrapped)
  @Test
  fun `round trip string`() {
    val sut = preparation()
    val stored = sut.prepareDataToSet("user_name", "nedaluOf")
    assertEquals("nedaluOf", sut.prepareDataToGet(stored as Any, String::class.java))
  }

  @Test
  fun `round trip int`() {
    val sut = preparation()
    val stored = sut.prepareDataToSet("age", 29)
    assertEquals(29, sut.prepareDataToGet(stored as Any, Int::class.java))
  }

  @Test
  fun `round trip boolean`() {
    val sut = preparation()
    val stored = sut.prepareDataToSet("is_free", true)
    assertEquals(true, sut.prepareDataToGet(stored as Any, Boolean::class.java))
  }

  @Test
  fun `round trip object`() {
    val sut = preparation()
    val stored = sut.prepareDataToSet("user", User())
    assertEquals(User(), sut.prepareDataToGet(stored as Any, User::class.java))
  }

  @Test
  fun `round trip list`() {
    val sut = preparation()
    val names = listOf("name", "phone", "email")
    val stored = sut.prepareDataToSet("names", names)
    val result = sut.prepareDataToGet(stored as Any, Array<String>::class.java)?.toList()
    assertEquals(names, result)
  }
  //endregion

  //region raw values migrated from SharedPreferences (typed, no serialization)
  @Test
  fun `get migrated raw string`() {
    val sut = preparation()
    assertEquals("NedaluOf", sut.prepareDataToGet("NedaluOf" as Any, String::class.java))
  }

  @Test
  fun `get migrated raw int`() {
    val sut = preparation()
    assertEquals(29, sut.prepareDataToGet(29 as Any, Int::class.java))
  }

  @Test
  fun `get migrated raw boolean`() {
    val sut = preparation()
    assertEquals(true, sut.prepareDataToGet(true as Any, Boolean::class.java))
  }

  @Test
  fun `get migrated raw int as string`() {
    val sut = preparation()
    assertEquals("29", sut.prepareDataToGet(29 as Any, String::class.java))
  }

  @Test
  fun `get raw number string as int`() {
    val sut = preparation()
    assertEquals(29, sut.prepareDataToGet("29" as Any, Int::class.java))
  }
  //endregion

  //region serialized values without spec wrap (encryption disabled on read)
  @Test
  fun `get serialized string without spec wrap`() {
    val sut = preparation(isEncryptionOn = false)
    val stored = sut.prepareDataToSet("user_name", "nedaluOf")
    assertEquals("nedaluOf", sut.prepareDataToGet(stored as Any, String::class.java))
  }

  @Test
  fun `get serialized object without spec wrap`() {
    val sut = preparation(isEncryptionOn = false)
    val stored = sut.prepareDataToSet("user", User())
    assertEquals(User(), sut.prepareDataToGet(stored as Any, User::class.java))
  }
  //endregion

  //region unparseable values
  @Test
  fun `get incompatible raw string as int returns null`() {
    val sut = preparation(isEncryptionOn = false)
    assertNull(sut.prepareDataToGet("hello" as Any, Int::class.java))
  }
  //endregion
}
