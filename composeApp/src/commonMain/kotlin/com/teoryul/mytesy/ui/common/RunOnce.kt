package com.teoryul.mytesy.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun RunOnce(key: String, block: suspend () -> Unit) {
    var ran by rememberSaveable(key) { mutableStateOf(false) }
    LaunchedEffect(key, ran) {
        if (ran) return@LaunchedEffect
        ran = true
        block()
    }
}