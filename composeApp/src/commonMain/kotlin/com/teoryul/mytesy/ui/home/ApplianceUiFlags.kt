package com.teoryul.mytesy.ui.home

/**
 * Flags intended for the UI layer only.
 * State should not be persisted in the DB.
 */
data class ApplianceUiFlags(
    val showPowerButtonSpinner: Boolean = false,
    val errorMessage: String? = null
)
