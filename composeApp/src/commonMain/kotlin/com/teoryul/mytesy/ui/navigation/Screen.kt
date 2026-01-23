package com.teoryul.mytesy.ui.navigation

import androidx.compose.runtime.saveable.Saver
import com.teoryul.mytesy.domain.appliance.ApplianceEntity

sealed class Screen {
    data object Welcome : Screen()
    data object Login : Screen()

    data object Home : Screen()
    data object AddAppliance : Screen()
    data object Notifications : Screen()
    data object Settings : Screen()

    data object ComingSoon : Screen()

    data class ApplianceDetail(val appliance: ApplianceEntity) : Screen()

    data object AddApplianceChooseGroup : Screen()
    data object AddApplianceGroupConvectors : Screen()
    data object AddApplianceGroupWaterHeaters : Screen()

    // Convertors
    data object ConvertorFinEcoCN06 : Screen()
    data object ConvertorHeatEcoCN031 : Screen()
    data object ConvertorLivEcoCN051 : Screen()
    data object ConvertorFloorEcoCN052 : Screen()
    data object ConvertorHeatEcoCN03 : Screen()
    data object ConvertorConvEcoCN04 : Screen()

    // Water heaters
    data object WaterHeaterBiLightCloudB15 : Screen()
    data object WaterHeaterBelliSlimoLiteCloudE32 : Screen()
    data object WaterHeaterModEcoC21 : Screen()
    data object WaterHeaterModEcoC22 : Screen()
    data object WaterHeaterBelliSlimoE31 : Screen()
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