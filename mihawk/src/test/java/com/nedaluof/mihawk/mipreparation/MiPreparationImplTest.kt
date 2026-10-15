package com.nedaluof.mihawk.mipreparation

import com.google.gson.Gson
import com.nedaluof.mihawk.miencryption.MiEncryption
import com.nedaluof.mihawk.miencryption.model.MiEncryptionSpec
import com.nedaluof.mihawk.miserializer.MiSerializerImpl
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MiPreparationImplTest {

  //region fixtures
  private data class User(val name: String = "Nedal", val age: Int = 29)

  private class FakeEncryption(
    private val isInitialized: Boolean = true
  ) : MiEncryption {
    override fun initialized() = isInitialized

    override fun encrypt(key: String, plainText: String): MiEncryptionSpec =
      MiEncryptionSpec(cipherText = plainText.reversed(), key = key)

    override fun decrypt(miEncryptionSpec: MiEncryptionSpec): String =
      miEncryptionSpec.cipherText.reversed()
  }

  private fun preparation(encryption: MiEncryption = FakeEncryption()) =
    MiPreparationImpl(MiSerializerImpl(Gson()), encryption)
  //endregion

  //region put/get round trips through the raw-value overload (production path)
  @Test
  fun `round trip string through raw-value overload with encryption`() {
    val sut = preparation()
    val stored = sut.prepareDataToSet("user_name", "nedaluOf")
    assertEquals("nedaluOf", sut.prepareDataToGet(stored as Any, String::class.java))
  }

  @Test
  fun `round trip int through raw-value overload with encryption`() {
    val sut = preparation()
    val stored = sut.prepareDataToSet("age", 29)
    assertEquals(29, sut.prepareDataToGet(stored as Any, Int::class.java))
  }

  @Test
  fun `round trip boolean through raw-value overload with encryption`() {
    val sut = preparation()
    val stored = sut.prepareDataToSet("is_free", true)
    assertEquals(true, sut.prepareDataToGet(stored as Any, Boolean::class.java))
  }

  @Test
  fun `round trip object through raw-value overload with encryption`() {
    val sut = preparation()
    val stored = sut.prepareDataToSet("user", User())
    assertEquals(User(), sut.prepareDataToGet(stored as Any, User::class.java))
  }
  //endregion

  //region migrated raw values (SharedPreferencesMigration preserves types)
  @Test
  fun `get migrated raw string without encryption wrap`() {
    val sut = preparation(FakeEncryption(isInitialized = false))
    assertEquals("NedaluOf", sut.prepareDataToGet("NedaluOf" as Any, String::class.java))
  }

  @Test
  fun `get migrated raw integer`() {
    val sut = preparation()
    assertEquals(29, sut.prepareDataToGet(29 as Any, Int::class.java))
  }

  @Test
  fun `get migrated raw boolean`() {
    val sut = preparation()
    assertEquals(true, sut.prepareDataToGet(true as Any, Boolean::class.java))
  }

  @Test
  fun `get migrated raw integer as string`() {
    val sut = preparation()
    assertEquals("29", sut.prepareDataToGet(29 as Any, String::class.java))
  }

  @Test
  fun `get raw number string as integer`() {
    val sut = preparation(FakeEncryption(isInitialized = false))
    assertEquals(29, sut.prepareDataToGet("29" as Any, Int::class.java))
  }
  //endregion

  //region gson serialized values without spec wrap (encryption disabled on read)
  @Test
  fun `get gson serialized string without spec wrap`() {
    val sut = preparation(FakeEncryption(isInitialized = false))
    val stored = sut.prepareDataToSet("user_name", "nedaluOf")
    assertEquals("nedaluOf", sut.prepareDataToGet(stored as Any, String::class.java))
  }

  @Test
  fun `get gson serialized object without spec wrap`() {
    val sut = preparation(FakeEncryption(isInitialized = false))
    val stored = sut.prepareDataToSet("user", User())
    assertEquals(User(), sut.prepareDataToGet(stored as Any, User::class.java))
  }
  //endregion

  //region unparseable values
  @Test
  fun `get incompatible raw string as integer returns null`() {
    val sut = preparation(FakeEncryption(isInitialized = false))
    assertNull(sut.prepareDataToGet("hello" as Any, Int::class.java))
  }
  //endregion
}
