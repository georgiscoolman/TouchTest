@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class)

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

    private val lightColors =
        listOf(Color.Cyan, Color.Yellow, Color.Magenta, Color.Red, Color.Green)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            TouchTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    var pointers by remember {
                        mutableStateOf<List<PointF>>(emptyList())
                    }

                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInteropFilter { motionEvent ->
                                    val action = motionEvent.action
                                    val pointerCount = motionEvent.pointerCount

                                    Log.d(TAG, "pointerCount $pointerCount $motionEvent")

                                    val visibleTouch: List<PointF> =
                                        if (action == MotionEvent.ACTION_UP) {
                                            emptyList()
                                        } else {
                                            val maskedAction = action and MotionEvent.ACTION_MASK
                                            if (maskedAction == MotionEvent.ACTION_POINTER_UP) {
                                                val droppedIndex =
                                                    action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT

                                                val visibleTouches = mutableListOf<PointF>()

                                                for (i in 0 until pointerCount) {
                                                    if (i != droppedIndex) {
                                                        visibleTouches.add(
                                                            PointF(
                                                                motionEvent.getX(i),
                                                                motionEvent.getY(i)
                                                            )
                                                        )
                                                    }
                                                }

                                                visibleTouches
                                            } else {
                                                List(pointerCount) { i ->
                                                    PointF(
                                                        motionEvent.getX(i),
                                                        motionEvent.getY(i)
                                                    )
                                                }
                                            }
                                        }

                                    Log.d(TAG, "visibleTouch $visibleTouch")
                                    pointers = visibleTouch
                                    true
                                }
                        ) {
                            drawRect(color = Color.Black, size = size)

                            pointers.mapIndexed { index, pointF ->
                                val color = getColorByIndex(index)
                                drawLine(
                                    color = color,
                                    start = Offset(pointF.x, 0f),
                                    end = Offset(pointF.x, size.height)
                                )
                                drawLine(
                                    color = color,
                                    start = Offset(0f, pointF.y),
                                    end = Offset(size.width, pointF.y),
                                )
                            }
                        }

                        val text = if (pointers.isNotEmpty()) {
                            val stringBuilder = StringBuilder()
                            pointers.forEach {
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

    private fun getColorByIndex(index: Int): Color {
        val rem = index % lightColors.size
        return lightColors[rem]
    }
}
