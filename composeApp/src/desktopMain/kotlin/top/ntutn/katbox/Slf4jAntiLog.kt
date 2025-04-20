package top.ntutn.katbox

import io.github.aakira.napier.Antilog
import io.github.aakira.napier.LogLevel
import org.slf4j.LoggerFactory

class Slf4jAntiLog: Antilog() {
    override fun performLog(
        priority: LogLevel,
        tag: String?,
        throwable: Throwable?,
        message: String?
    ) {
        val logger = LoggerFactory.getLogger(tag)
        when(priority) {
            LogLevel.VERBOSE -> logger.trace(message, throwable)
            LogLevel.DEBUG -> logger.debug(message, throwable)
            LogLevel.INFO -> logger.info(message, throwable)
            LogLevel.WARNING -> logger.warn(message, throwable)
            LogLevel.ERROR -> logger.error(message, throwable)
            else -> logger.error(message, throwable)
        }
    }
}