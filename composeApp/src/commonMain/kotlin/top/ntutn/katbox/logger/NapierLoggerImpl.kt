package top.ntutn.katbox.logger

import io.github.aakira.napier.LogLevel
import io.github.aakira.napier.Napier

class NapierLoggerImpl(private val tag: String, level: Int) : ILogger {
    private val logLevel = when (level) {
        LoggerFacade.VERBOSE -> LogLevel.VERBOSE
        LoggerFacade.DEBUG -> LogLevel.DEBUG
        LoggerFacade.INFO -> LogLevel.INFO
        LoggerFacade.WARN -> LogLevel.WARNING
        LoggerFacade.ERROR -> LogLevel.ERROR
        else -> LogLevel.ASSERT
    }

    override fun log(message: String, throwable: Throwable?) {
        Napier.log(logLevel, tag, throwable, message)
    }
}