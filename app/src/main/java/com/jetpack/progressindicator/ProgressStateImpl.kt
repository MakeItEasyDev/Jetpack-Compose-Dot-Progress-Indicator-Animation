package com.jetpack.progressindicator

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.PI

class ProgressStateImpl: ProgressState {
    private val animationValues: List<MutableState<Float>> = List(NumDots) {
        mutableStateOf(0f)
    }

    override operator fun get(index: Int) = animationValues[index].value

    override fun start(scope: CoroutineScope) {
        repeat(NumDots) { index ->
            scope.launch {
                animate(
                    initialValue = 0f,
                    targetValue = (2f * PI).toFloat(),
                    animationSpec = infiniteRepeatable(
                        animation = keyframes {
                            durationMillis = AnimationDuration
                            0f at 0
                            (.5 * PI).toFloat() at 2 * AnimationSegment
                            PI.toFloat() at 3 * AnimationSegment
                            (1.5 * PI).toFloat() at 4 * AnimationSegment
                            (2f * PI).toFloat() at 6 * AnimationSegment
                        },
                        repeatMode = RepeatMode.Restart,
                        initialStartOffset = StartOffset(offsetMillis = 100 * index)
                    )
                ) { value, _ ->
                    animationValues[index].value = value
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProgressStateImpl

        if (animationValues != other.animationValues) return false

        return true
    }
}


















