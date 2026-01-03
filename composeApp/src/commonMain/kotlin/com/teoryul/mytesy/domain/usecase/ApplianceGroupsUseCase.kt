package com.teoryul.mytesy.domain.usecase

import com.teoryul.mytesy.ui.common.AppImage

/**
 * Temporary quick solutions for defining contents for:
 * - common group screen (convertors + water heaters)
 * - convertors screen (list of convectors)
 * - water heater screen (list of water heaters)
 */
class ApplianceGroupsUseCase {
    val commonGroupScreen by lazy {
        listOf(ApplianceGroupItem.Convectors, ApplianceGroupItem.WaterHeaters)
    }
    val convectorScreen by lazy {
        listOf(
            ApplianceGroupItem.ConvertorFinEcoCN06,
            ApplianceGroupItem.ConvertorHeatEcoCN031,
            ApplianceGroupItem.ConvertorLivEcoCN051,
            ApplianceGroupItem.ConvertorFloorEcoCN052,
            ApplianceGroupItem.ConvertorHeatEcoCN03,
            ApplianceGroupItem.ConvertorConvEcoCN04
        )
    }
    val waterHeaterScreen by lazy {
        listOf(
            ApplianceGroupItem.WaterHeaterBiLightCloudB15,
            ApplianceGroupItem.WaterHeaterBelliSlimoLiteCloudE32,
            ApplianceGroupItem.WaterHeaterModEcoC21,
            ApplianceGroupItem.WaterHeaterModEcoC22,
            ApplianceGroupItem.WaterHeaterBelliSlimoE31
        )
    }
}

sealed class ApplianceGroupItem(
    val icon: AppImage,
    val name: String
) {
    data object Convectors : ApplianceGroupItem(
        AppImage.ConvectorsIcon,
        "Convectors"
    )

    data object ConvertorFinEcoCN06 : ApplianceGroupItem(
        AppImage.ConvertorFinEcoCN06Icon,
        "FinEco CN 06"
    )

    data object ConvertorHeatEcoCN031 : ApplianceGroupItem(
        AppImage.ConvertorHeatEcoCN031Icon,
        "HeatEco CN 031"
    )

    data object ConvertorLivEcoCN051 : ApplianceGroupItem(
        AppImage.ConvertorLivEcoCN051Icon,
        "LivEco CN 051"
    )

    data object ConvertorFloorEcoCN052 : ApplianceGroupItem(
        AppImage.ConvertorFloorEcoCN052Icon,
        "FloorEco CN 052"
    )

    data object ConvertorHeatEcoCN03 : ApplianceGroupItem(
        AppImage.ConvertorHeatEcoCN03Icon,
        "HeatEco CN 03"
    )

    data object ConvertorConvEcoCN04 : ApplianceGroupItem(
        AppImage.ConvertorConvEcoCN04Icon,
        "ConvEco CN 04"
    )

    data object WaterHeaters : ApplianceGroupItem(
        AppImage.WaterHeatersIcon,
        "Electric water heaters"
    )

    data object WaterHeaterBiLightCloudB15 : ApplianceGroupItem(
        AppImage.WaterHeaterBiLightCloudB15Icon,
        "BiLight Cloud B15"
    )

    data object WaterHeaterBelliSlimoLiteCloudE32 : ApplianceGroupItem(
        AppImage.WaterHeaterBelliSlimoLiteCloudE32Icon,
        "BelliSlimo Lite Cloud E32"
    )

    data object WaterHeaterModEcoC21 : ApplianceGroupItem(
        AppImage.WaterHeaterModEcoC21Icon,
        "ModEco C21"
    )

    data object WaterHeaterModEcoC22 : ApplianceGroupItem(
        AppImage.WaterHeaterModEcoC22Icon,
        "ModEco C22"
    )

    data object WaterHeaterBelliSlimoE31 : ApplianceGroupItem(
        AppImage.WaterHeaterBelliSlimoE31Icon,
        "BelliSlimo E31"
    )

    override fun equals(other: Any?): Boolean {
        if (other !is ApplianceGroupItem) return false
        return icon == other.icon && name == other.name
    }

    override fun hashCode(): Int {
        return icon.hashCode() xor name.hashCode()
    }
}