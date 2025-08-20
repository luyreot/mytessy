package com.teoryul.mytesy.infra

import platform.Foundation.NSLog

actual fun logDebug(tag: String, message: String) {
    NSLog("DEBUG: $tag - $message")
}

actual fun logInfo(tag: String, message: String) {
    NSLog("INFO: $tag - $message")
}

actual fun logError(tag: String, message: String, throwable: Throwable?) {
    NSLog("ERROR: $tag - $message ${throwable?.message}")
}