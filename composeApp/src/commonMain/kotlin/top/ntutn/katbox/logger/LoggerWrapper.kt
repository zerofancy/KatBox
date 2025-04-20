package top.ntutn.katbox.logger

class LoggerWrapper(tag: String) {
    private val verboseLogger = NapierLoggerImpl(tag, LoggerFacade.VERBOSE)
    private val debugLogger = NapierLoggerImpl(tag, LoggerFacade.DEBUG)
    private val infoLogger = NapierLoggerImpl(tag, LoggerFacade.INFO)
    private val warnLogger = NapierLoggerImpl(tag, LoggerFacade.WARN)
    private val errorLogger = NapierLoggerImpl(tag, LoggerFacade.ERROR)

    fun verbose(block: ILogger.() -> Unit) {
        if (LoggerFacade.level <= LoggerFacade.VERBOSE) {
            verboseLogger.block()
        }
    }

    fun debug(block: ILogger.() -> Unit) {
        if (LoggerFacade.level <= LoggerFacade.DEBUG) {
            debugLogger.block()
        }
    }

    fun info(block: ILogger.() -> Unit) {
        if (LoggerFacade.level <= LoggerFacade.INFO) {
            infoLogger.block()
        }
    }

    fun warn(block: ILogger.() -> Unit) {
        if (LoggerFacade.level <= LoggerFacade.WARN) {
            warnLogger.block()
        }
    }

    fun error(block: ILogger.() -> Unit) {
        if (LoggerFacade.level <= LoggerFacade.ERROR) {
            errorLogger.block()
        }
    }
}