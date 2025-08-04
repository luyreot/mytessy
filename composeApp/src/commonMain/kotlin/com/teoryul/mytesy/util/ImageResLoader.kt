package com.teoryul.mytesy.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

@Composable
expect fun loadImageBitmap(image: AppImage): ImageBitmap?

enum class AppImage {
    WelcomeLogo,
    WelcomeImage
}