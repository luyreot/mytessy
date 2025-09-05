package com.teoryul.mytesy.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.teoryul.mytesy.ui.main.AppMain
import com.teoryul.mytesy.ui.lifecycle.WithScreenViewModelStore

@Composable
fun App() {
    MaterialTheme {
        WithScreenViewModelStore(key = "appMain") {
            AppMain()
        }
    }
}