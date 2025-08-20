package com.teoryul.mytesy.infra

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object AppLogger : AppLoggerDefinition, KoinComponent {

    private val logger: AppLoggerDefinition by inject()

    override fun d(message: String) {
        logger.d(message)
    }

    override fun d(tag: String, message: String) {
        logger.d(tag, message)
    }

    override fun i(message: String) {
        logger.i(message)
    }

    override fun i(tag: String, message: String) {
        logger.i(tag, message)
    }

    override fun e(throwable: Throwable?) {
        logger.e(throwable)
    }

    override fun e(message: String, throwable: Throwable?) {
        logger.e(message, throwable)
    }

    override fun e(tag: String, message: String, throwable: Throwable?) {
        logger.e(tag, message, throwable)
    }
}

interface AppLoggerDefinition {
    fun d(message: String)
    fun d(tag: String, message: String)
    fun i(message: String)
    fun i(tag: String, message: String)
    fun e(throwable: Throwable?)
    fun e(message: String, throwable: Throwable? = null)
    fun e(tag: String, message: String, throwable: Throwable? = null)
}

class AppLoggerDebug : AppLoggerDefinition {

    override fun d(message: String) {
        logDebug(">>>", message)
    }

    override fun d(tag: String, message: String) {
        logDebug(tag, message)
    }

    override fun i(message: String) {
        logInfo(">>>", message)
    }

    override fun i(tag: String, message: String) {
        logInfo(tag, message)
    }

    override fun e(throwable: Throwable?) {
        logError(">>>", "", throwable)
    }

    override fun e(message: String, throwable: Throwable?) {
        logError(">>>", message, throwable)
    }

    override fun e(tag: String, message: String, throwable: Throwable?) {
        logError(tag, message, throwable)
    }
}

class AppLoggerRelease : AppLoggerDefinition {
    override fun d(message: String) {}
    override fun d(tag: String, message: String) {}
    override fun i(message: String) {}
    override fun i(tag: String, message: String) {}
    override fun e(throwable: Throwable?) {}
    override fun e(message: String, throwable: Throwable?) {}
    override fun e(tag: String, message: String, throwable: Throwable?) {}
}

expect fun logDebug(tag: String, message: String)
expect fun logInfo(tag: String, message: String)
expect fun logError(tag: String, message: String, throwable: Throwable? = null)