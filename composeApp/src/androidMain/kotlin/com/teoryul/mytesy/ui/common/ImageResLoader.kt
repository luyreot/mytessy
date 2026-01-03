package com.teoryul.mytesy.ui.common

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.createBitmap
import com.teoryul.mytesy.R
import com.teoryul.mytesy.infra.AppLogger.e

@Composable
actual fun LoadImageBitmap(image: AppImage): ImageBitmap? {
    val context = LocalContext.current
    val resId = getImageResId(image)
    return remember(resId) { getImageBitmap(context, resId) }
}

private fun getImageBitmap(context: Context, resId: Int): ImageBitmap? {
    try {
        return BitmapFactory.decodeResource(context.resources, resId)?.asImageBitmap()
            ?: getVectorDrawableAsBitmap(context, resId)
    } catch (e: Exception) {
        e(e)
        return null
    }
}

private fun getVectorDrawableAsBitmap(context: Context, resId: Int): ImageBitmap? {
    val drawable =
        ResourcesCompat.getDrawable(context.resources, resId, context.theme) ?: return null

    val width = drawable.intrinsicWidth.takeIf { it > 0 } ?: 256
    val height = drawable.intrinsicHeight.takeIf { it > 0 } ?: 256

    val bitmap = createBitmap(width, height)
    val canvas = Canvas(bitmap)

    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return bitmap.asImageBitmap()
}

private fun getImageResId(image: AppImage): Int = when (image) {
    AppImage.WelcomeLogo -> R.drawable.logo_tesy_welcome
    AppImage.WelcomeImage -> R.drawable.img_welcome
    AppImage.ModecoIcon -> R.drawable.ic_modeco
    AppImage.AddApplianceImage -> R.drawable.img_add_appliance
    AppImage.ConvectorsIcon -> R.drawable.ic_convectors
    AppImage.ConvertorConvEcoCN04Icon -> R.drawable.ic_conv_eco_cn04
    AppImage.ConvertorFinEcoCN06Icon -> R.drawable.ic_conv_fin_eco_cn06
    AppImage.ConvertorFloorEcoCN052Icon -> R.drawable.ic_conv_floor_eco_cn052
    AppImage.ConvertorHeatEcoCN03Icon -> R.drawable.ic_conv_heat_eco_cn03
    AppImage.ConvertorHeatEcoCN031Icon -> R.drawable.ic_conv_heat_eco_cn031
    AppImage.ConvertorLivEcoCN051Icon -> R.drawable.ic_conv_liv_eco_cn051
    AppImage.WaterHeatersIcon -> R.drawable.ic_water_heaters
    AppImage.WaterHeaterBelliSlimoE31Icon -> R.drawable.ic_waterh_belli_slimo_e31
    AppImage.WaterHeaterBelliSlimoLiteCloudE32Icon -> R.drawable.ic_waterh_belli_slimo_lite_cloud_e32
    AppImage.WaterHeaterBiLightCloudB15Icon -> R.drawable.ic_waterh_bi_light_cloud_b15
    AppImage.WaterHeaterModEcoC21Icon -> R.drawable.ic_waterh_mod_eco_c21
    AppImage.WaterHeaterModEcoC22Icon -> R.drawable.ic_waterh_mod_eco_c22
}