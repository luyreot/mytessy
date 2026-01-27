package com.teoryul.mytesy.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

@Composable
expect fun LoadImageBitmap(image: AppImage): ImageBitmap?

enum class AppImage(val contentDescription: String) {
    WelcomeLogo("Logo Text"),
    WelcomeImage("Logo"),
    ModecoIcon("Modeco"),
    AddApplianceImage("Add appliance"),
    ConvectorsIcon("Convectors"),
    ConvertorConvEcoCN04Icon("ConvertorConvEcoCN04"),
    ConvertorFinEcoCN06Icon("ConvertorFinEcoCN06"),
    ConvertorFloorEcoCN052Icon("ConvertorFloorEcoCN052"),
    ConvertorHeatEcoCN03Icon("ConvertorHeatEcoCN03"),
    ConvertorHeatEcoCN031Icon("ConvertorHeatEcoCN031"),
    ConvertorLivEcoCN051Icon("ConvertorLivEcoCN051"),
    WaterHeatersIcon("Water Heaters"),
    WaterHeaterBelliSlimoE31Icon("WaterHeaterBelliSlimoE31"),
    WaterHeaterBelliSlimoLiteCloudE32Icon("WaterHeaterBelliSlimoLiteCloudE32"),
    WaterHeaterBiLightCloudB15Icon("WaterHeaterBiLightCloudB15"),
    WaterHeaterModEcoC21Icon("WaterHeaterModEcoC21"),
    WaterHeaterModEcoC22Icon("WaterHeaterModEcoC22"),

    HeatIcon("HeatIcon"),
    LeafIcon("LeafIcon"),
    TwoLeavesIcon("TwoLeavesIcon"),
    ThreeLeavesIcon("ThreeLeavesIcon"),
    PalmTreesIcon("PalmTreesIcon")
}