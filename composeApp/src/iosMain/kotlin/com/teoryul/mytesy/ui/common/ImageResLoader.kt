package com.teoryul.mytesy.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.teoryul.mytesy.infra.AppLogger.d
import com.teoryul.mytesy.infra.AppLogger.e
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation
import platform.posix.memcpy

@Composable
actual fun LoadImageBitmap(image: AppImage): ImageBitmap? {
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
                memcpy(
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