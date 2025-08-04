package com.teoryul.mytesy.util

import android.content.res.Resources
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.teoryul.mytesy.R
import com.teoryul.mytesy.util.AppLogger.e

@Composable
actual fun LoadImageBitmap(image: AppImage): ImageBitmap? {
    val context = LocalContext.current
    val resId = getImageResId(image)
    return getImageBitmap(context.resources, resId)
}

private fun getImageBitmap(resources: Resources, resId: Int): ImageBitmap? {
    try {
        return BitmapFactory.decodeResource(resources, resId)?.asImageBitmap()
    } catch (e: Exception) {
        e(e)
        return null
    }
}

private fun getImageResId(image: AppImage): Int = when (image) {
    AppImage.WelcomeLogo -> R.drawable.welcome_logo
    AppImage.WelcomeImage -> R.drawable.welcome_image
}