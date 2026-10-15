package com.nedaluof.mihawk.miserializer

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

class MiSerializerImplTest {

  //region fixtures
  private data class User(val name: String = "Nedal", val age: Int = 29)

  private val serializer = MiSerializerImpl(Gson())
  //endregion

  //region primitives
  @Test
  fun `round trip int`() {
    assertEquals(29, serializer.fromString(serializer.toString(29), Int::class.java))
  }

  @Test
  fun `round trip boolean`() {
    assertEquals(true, serializer.fromString(serializer.toString(true), Boolean::class.java))
  }

  @Test
  fun `round trip double`() {
    assertEquals(1995.5, serializer.fromString(serializer.toString(1995.5), Double::class.java))
  }

  @Test
  fun `string values are json quoted`() {
    assertEquals("\"nedaluOf\"", serializer.toString("nedaluOf"))
  }
  //endregion

  //region structured values
  @Test
  fun `round trip object`() {
    val user = User()
    assertEquals(user, serializer.fromString(serializer.toString(user), User::class.java))
  }

  @Test
  fun `round trip list`() {
    val names = listOf("name", "phone", "email")
    val json = serializer.toString(names)
    val result = serializer.fromString(json, Array<String>::class.java)?.toList()
    assertEquals(names, result)
  }
  //endregion
}
