package com.george.touchtest

import android.app.ActivityOptions
import android.content.Intent
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.george.touchtest.ui.theme.TouchTestTheme
import kotlin.math.max
import kotlin.math.min


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val dm = applicationContext.getSystemService(DISPLAY_SERVICE) as DisplayManager
        val displayIds = dm.displays.map { it.displayId }

        Log.d("TouchTest", "available screen Ids $displayIds")

        setContent {
            TouchTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (displayIds.isEmpty()) {
                        Text(
                            text = "No Screen Ids detected",
                            style = MaterialTheme.typography.headlineLarge
                        )
                    } else {
                        DisplayIdSelector(displayIds = displayIds) { screenIdIndex ->
                            val displayID = displayIds[screenIdIndex]
                            launchTouchTest(displayID)
                        }
                    }
                }
            }
        }
    }

    private fun launchTouchTest(screenId: Int) {
        val options = ActivityOptions.makeBasic().apply {
            launchDisplayId = screenId
        }
        val intent = Intent(this, TouchTestActivity::class.java)
        startActivity(intent, options.toBundle())
    }
}

@Composable
private fun DisplayIdSelector(
    modifier: Modifier = Modifier,
    displayIds: List<Int>, onLaunchClick: (Int) -> Unit
) {
    var screenIdIndex by rememberSaveable {
        mutableStateOf(0)
    }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {

            Text(text = "ScreenId")

            Text(text = displayIds[screenIdIndex].toString())

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = {
                    screenIdIndex = min(screenIdIndex++, displayIds.lastIndex)
                }) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowUp,
                        contentDescription = null
                    )
                }
                Button(onClick = {
                    screenIdIndex = max(screenIdIndex--, 0)
                }) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
            }
        }
        Button(
            modifier = Modifier.padding(top = 64.dp),
            onClick = {
                onLaunchClick(screenIdIndex)
            }
        ) {
            Text(
                text = "Launch Touch Test",
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}