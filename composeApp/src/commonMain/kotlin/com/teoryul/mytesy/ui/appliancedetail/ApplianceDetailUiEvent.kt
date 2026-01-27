package com.teoryul.mytesy.ui.appliancedetail

sealed interface UiEvent {
    data class Toast(val message: String) : UiEvent
}