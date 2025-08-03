package com.teoryul.mytesy.util

object AppLogger {

    var isEnabled = true // TODO disable for release builds

    fun d(message: String) {
        if (isEnabled) logDebug(">>>", message)
    }

    fun d(tag: String, message: String) {
        if (isEnabled) logDebug(tag, message)
    }

    fun i(message: String) {
        if (isEnabled) logInfo(">>>", message)
    }

    fun i(tag: String, message: String) {
        if (isEnabled) logInfo(tag, message)
    }

    fun e(throwable: Throwable?) {
        if (isEnabled) logError(">>>", "", throwable)
    }

    fun e(message: String, throwable: Throwable? = null) {
        if (isEnabled) logError(">>>", message, throwable)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (isEnabled) logError(tag, message, throwable)
    }

}

expect fun logDebug(tag: String, message: String)
expect fun logInfo(tag: String, message: String)
expect fun logError(tag: String, message: String, throwable: Throwable? = null)