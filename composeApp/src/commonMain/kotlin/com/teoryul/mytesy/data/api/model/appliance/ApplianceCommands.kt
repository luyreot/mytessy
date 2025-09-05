package com.teoryul.mytesy.data.api.model.appliance

enum class ApplianceCommand(val commandName: String) {
    POWER("pwr")
}

enum class CommandPowerValues(val commandValue: String) {
    TURN_OFF("0"), TURN_ON("1")
}