package com.teoryul.mytesy.ui.helper

import com.teoryul.mytesy.domain.appliance.ApplianceEntity

enum class ApplianceProgramMode(
    val labelLong: String,
    val labelShort: String,
) {
    Undefined("Undefined", "Undefined"),
    Manual("Manual", "Manual"),
    P1("Program P1", "P1"),
    P2("Program P2", "P2"),
    P3("Program P3", "P3")
}

fun getProgram(appliance: ApplianceEntity?): ApplianceProgramMode {
    return when (appliance?.statusMode) {
        "0" -> ApplianceProgramMode.Manual
        "1" -> ApplianceProgramMode.P1
        "2" -> ApplianceProgramMode.P2
        "3" -> ApplianceProgramMode.P3
        else -> ApplianceProgramMode.Undefined
    }
}