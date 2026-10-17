package com.nedaluof.mihawk.mimigration

/**
 * Created By NedaluOf - 19/09/2025.
 */
class DefaultMiMigration : MiMigration {

  //region logic
  override fun oldSharedPreferencesFileName(): String = ""

  override fun oldKeysToMigrate(): Set<String> = emptySet()
  //endregion
}