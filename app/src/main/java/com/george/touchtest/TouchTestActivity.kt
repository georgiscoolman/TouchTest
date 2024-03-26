@file:OptIn(ExperimentalComposeUiApi::class)

package com.george.touchtest

import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
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

                    val pointers = remember {
                        mutableStateMapOf<Int, PointF>()
                    }

                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        val points = pointers.values

                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInteropFilter { motionEvent ->
                                    Log.d(TAG, "$motionEvent")

                                    when (motionEvent.action) {
                                        MotionEvent.ACTION_UP -> {
                                            pointers.clear()
                                        }

                                        MotionEvent.ACTION_POINTER_UP -> {
                                            val cnt = motionEvent.pointerCount
                                            pointers.remove(cnt - 1)
                                        }

                                        else -> {
                                            val cnt = motionEvent.pointerCount
                                            for (i in 0 until cnt) {
                                                val point = PointF(
                                                    motionEvent.getX(i),
                                                    motionEvent.getY(i)
                                                )
                                                pointers[i] = point
                                            }
                                        }
                                    }
                                    true
                                }
                        ) {
                            drawRect(color = Color.Black, size = size)

                            points.map {
                                drawLine(
                                    color = Color.White,
                                    start = Offset(it.x, 0f),
                                    end = Offset(it.x, size.height)
                                )
                                drawLine(
                                    color = Color.White,
                                    start = Offset(0f, it.y),
                                    end = Offset(size.width, it.y),
                                )
                            }
                        }

                        val text = if (points.isNotEmpty()) {
                            val stringBuilder = StringBuilder()
                            points.forEach {
                                stringBuilder.append("x: ${it.x} : y: ${it.y}")
                                stringBuilder.appendLine()
                            }
                            stringBuilder.toString()
                        } else "No touch detected"

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
