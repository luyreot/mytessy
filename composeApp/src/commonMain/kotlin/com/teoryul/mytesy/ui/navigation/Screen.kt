package com.teoryul.mytesy.ui.navigation

import androidx.compose.runtime.saveable.Saver

sealed class Screen {
    object Welcome : Screen()
    object Login : Screen()

    object Home : Screen()
    object AddAppliance : Screen()
    object Notifications : Screen()
    object Settings : Screen()

    object ComingSoon : Screen()

    object AddApplianceChooseGroup : Screen()
    object AddApplianceGroupConvectors : Screen()
    object AddApplianceGroupWaterHeaters : Screen()

    // Convertors
    object ConvertorFinEcoCN06 : Screen()
    object ConvertorHeatEcoCN031 : Screen()
    object ConvertorLivEcoCN051 : Screen()
    object ConvertorFloorEcoCN052 : Screen()
    object ConvertorHeatEcoCN03 : Screen()
    object ConvertorConvEcoCN04 : Screen()

    // Water heaters
    object WaterHeaterBiLightCloudB15 : Screen()
    object WaterHeaterBelliSlimoLiteCloudE32 : Screen()
    object WaterHeaterModEcoC21 : Screen()
    object WaterHeaterModEcoC22 : Screen()
    object WaterHeaterBelliSlimoE31 : Screen()
}

val ScreenBackStackSaver: Saver<List<Screen>, List<String>> = Saver(
    save = { screenList ->
        screenList.mapNotNull { it::class.simpleName }
    },
    restore = { savedList ->
        savedList.map {
            when (it) {
                "Welcome" -> Screen.Welcome
                "Login" -> Screen.Login

                "Home" -> Screen.Home
                "AddAppliance" -> Screen.AddAppliance
                "Notifications" -> Screen.Notifications
                "Settings" -> Screen.Settings

                "ComingSoon" -> Screen.ComingSoon

                "AddApplianceChooseGroup" -> Screen.AddApplianceChooseGroup
                "AddApplianceGroupConvectors" -> Screen.AddApplianceGroupConvectors
                "AddApplianceGroupWaterHeaters" -> Screen.AddApplianceGroupWaterHeaters

                "ConvertorFinEcoCN06" -> Screen.ConvertorFinEcoCN06
                "ConvertorHeatEcoCN031" -> Screen.ConvertorHeatEcoCN031
                "ConvertorLivEcoCN051" -> Screen.ConvertorLivEcoCN051
                "ConvertorFloorEcoCN052" -> Screen.ConvertorFloorEcoCN052
                "ConvertorHeatEcoCN03" -> Screen.ConvertorHeatEcoCN03
                "ConvertorConvEcoCN04" -> Screen.ConvertorConvEcoCN04

                "WaterHeaterBiLightCloudB15" -> Screen.WaterHeaterBiLightCloudB15
                "WaterHeaterBelliSlimoLiteCloudE32" -> Screen.WaterHeaterBelliSlimoLiteCloudE32
                "WaterHeaterModEcoC21" -> Screen.WaterHeaterModEcoC21
                "WaterHeaterModEcoC22" -> Screen.WaterHeaterModEcoC22
                "WaterHeaterBelliSlimoE31" -> Screen.WaterHeaterBelliSlimoE31

                else -> Screen.Welcome
            }
        }
    }
)