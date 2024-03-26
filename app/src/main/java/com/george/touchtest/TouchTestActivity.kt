@file:OptIn(ExperimentalComposeUiApi::class)

package com.george.touchtest

import android.graphics.PointF
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.core.view.WindowCompat
import com.george.touchtest.ui.theme.TouchTestTheme

class TouchTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            TouchTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    var point by remember {
                        mutableStateOf<PointF?>(null)
                    }

                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInteropFilter { motionEvent ->
                                    point = if (motionEvent.action != MotionEvent.ACTION_UP) {
                                        PointF(motionEvent.x, motionEvent.y)
                                    } else {
                                        null
                                    }
                                    true
                                }
                        ) {
                            drawRect(color = Color.Black, size = size)

                            point?.run {
                                drawLine(
                                    color = Color.White,
                                    start = Offset(x, 0f),
                                    end = Offset(x, size.height)
                                )
                                drawLine(
                                    color = Color.White,
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                )
                            }
                        }

                        val text = point?.run {
                            "x: $x : y: $y"
                        } ?: "No touch detected"

                        Text(
                            text = text,
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.LightGray
                        )
                    }
                }
            }
        }
    }
}
