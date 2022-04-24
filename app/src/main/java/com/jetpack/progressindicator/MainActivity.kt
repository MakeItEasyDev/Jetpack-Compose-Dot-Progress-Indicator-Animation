package com.jetpack.progressindicator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jetpack.progressindicator.ui.theme.ProgressIndicatorTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.*

const val NumDots = 5 //dots count
const val AnimationDuration = 2000
const val AnimationSegment = AnimationDuration / 10
private val MainDotSize = 24.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProgressIndicatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "Dot Progress Indicator",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            )
                        }
                    ) {
                        ProgressIndicator(modifier = Modifier.padding(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
) {
    val state = rememberProgressState()
    LaunchedEffect(key1 = Unit) {
        state.start(this)
    }

    Layout(
        content = {
            val minFactor = .3f
            val step = minFactor / NumDots
            repeat(NumDots) { index ->
                val size = MainDotSize * (1f - step * index)
                Dot(
                    color = color,
                    modifier = Modifier
                        .requiredSize(size)
                        .graphicsLayer {
                            alpha = state[index].alphaFromRadians
                        }
                )
            }
        },
        modifier = modifier
    ) { measurables, constraints ->
        val looseConstraints = constraints.copy(
            minWidth = 0,
            maxHeight = 0
        )
        val placeables = measurables.map { measurable ->
            measurable.measure(looseConstraints)
        }

        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight
        ) {
            val radius = min(constraints.maxWidth, constraints.maxHeight) / 2f
            placeables.forEachIndexed { index, placeable ->
                val animatedValue = state[index]
                val x = (radius + radius * sin(animatedValue)).roundToInt()
                val y = (radius - radius * cos(animatedValue)).roundToInt()
                placeable.placeRelative(
                    x = x,
                    y = y
                )
            }
        }
    }
}

private val Float.alphaFromRadians: Float
    get() {
        val normalized = (this / 2f * PI).toFloat()
        return .5f + (normalized - .5f).absoluteValue
    }

@Composable
fun Dot(
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(color)
    )
}

@Composable
fun rememberProgressState(): ProgressState = remember {
    ProgressStateImpl()
}
























