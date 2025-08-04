package com.teoryul.mytesy.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.teoryul.mytesy.util.AppLogger.d
import com.teoryul.mytesy.util.AppLogger.e
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation

@Composable
actual fun loadImageBitmap(image: AppImage): ImageBitmap? {
    return getImageBitmap(getImageName(image))
}

@OptIn(ExperimentalForeignApi::class)
private fun getImageBitmap(name: String): ImageBitmap? {
    try {
        val uiImage: UIImage? = UIImage.imageNamed(name)
        if (uiImage == null) {
            d("uiImage is null - ($name)")
            return null
        }

        val nsData: NSData? = UIImagePNGRepresentation(uiImage)
        if (nsData == null) {
            d("nsData is null - ($name)")
            return null
        }

        val byteArray: ByteArray = ByteArray(nsData.length.toInt()).apply {
            usePinned { pinned ->
                platform.posix.memcpy(
                    pinned.addressOf(0),
                    nsData.bytes,
                    nsData.length
                )
            }
        }

        return Image.makeFromEncoded(byteArray).toComposeImageBitmap()
    } catch (e: Exception) {
        e(e)
        return null
    }
}

private fun getImageName(image: AppImage): String = when (image) {
    AppImage.WelcomeLogo -> "WelcomeLogo"
    AppImage.WelcomeImage -> "WelcomeImage"
}