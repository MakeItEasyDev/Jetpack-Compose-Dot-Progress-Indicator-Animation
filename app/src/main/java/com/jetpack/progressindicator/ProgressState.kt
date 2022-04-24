package com.jetpack.progressindicator

import androidx.compose.runtime.Stable
import kotlinx.coroutines.CoroutineScope

@Stable
interface ProgressState {
    fun start(scope: CoroutineScope)
    operator fun get(index: Int): Float
}