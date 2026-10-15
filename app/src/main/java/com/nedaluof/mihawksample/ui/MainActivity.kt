package com.nedaluof.mihawksample.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nedaluof.mihawksample.data.MiHawkPreferencesManager
import com.nedaluof.mihawksample.data.SharePreferencesManager

/**
 * Created by NedaluOf on 11/17/2021.
 */
class MainActivity : ComponentActivity() {

  //region variables
  private val miHawkPreferencesManager by lazy { MiHawkPreferencesManager() }
  private val sharePreferencesManager by lazy { SharePreferencesManager(applicationContext) }
  //endregion

  //region logic
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      AppContent()
    }
  }


  @Composable
  fun AppContent(modifier: Modifier = Modifier) {
    Box(
      modifier = modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Button(
          onClick = {
            sharePreferencesManager.setValuesToSharedPreferences()
          }
        ) {
          Text("Set Data To Shared Preferences")
        }

        Button(
          onClick = {
            miHawkPreferencesManager.getValuesFromOldPreferences()
          }
        ) {
          Text("Get Data From MiHawk")
        }
      }
    }
  }
  //endregion
}