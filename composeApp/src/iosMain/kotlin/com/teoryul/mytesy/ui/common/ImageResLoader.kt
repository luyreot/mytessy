package com.teoryul.mytesy.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
    val name = getImageName(image)
    return remember(name) { getImageBitmap(name) }
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
    AppImage.ModecoIcon -> "Modeco"
    AppImage.AddApplianceImage -> "AddAppliance"
    AppImage.ConvectorsIcon -> "Convectors"
    AppImage.ConvertorConvEcoCN04Icon -> "ConvertorConvEcoCN04"
    AppImage.ConvertorFinEcoCN06Icon -> "ConvertorFinEcoCN06"
    AppImage.ConvertorFloorEcoCN052Icon -> "ConvertorFloorEcoCN052"
    AppImage.ConvertorHeatEcoCN03Icon -> "ConvertorHeatEcoCN03"
    AppImage.ConvertorHeatEcoCN031Icon -> "ConvertorHeatEcoCN031"
    AppImage.ConvertorLivEcoCN051Icon -> "ConvertorLivEcoCN051"
    AppImage.WaterHeatersIcon -> "WaterHeaters"
    AppImage.WaterHeaterBelliSlimoE31Icon -> "WaterHeaterBelliSlimoE31"
    AppImage.WaterHeaterBelliSlimoLiteCloudE32Icon -> "WaterHeaterBelliSlimoLiteCloudE32"
    AppImage.WaterHeaterBiLightCloudB15Icon -> "WaterHeaterBiLightCloudB15"
    AppImage.WaterHeaterModEcoC21Icon -> "WaterHeaterModEcoC21"
    AppImage.WaterHeaterModEcoC22Icon -> "WaterHeaterModEcoC22"
}