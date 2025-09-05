package com.teoryul.mytesy.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

@Composable
expect fun LoadImageBitmap(image: AppImage): ImageBitmap?

enum class AppImage(val contentDescription: String) {
    WelcomeLogo("Logo Text"),
    WelcomeImage("Logo"),
    ModecoIcon("Modeco")
}