package top.ntutn.katbox.logger

import top.ntutn.katbox.getPlatform

object LoggerFacade {
    const val VERBOSE = 2
    const val DEBUG = 3
    const val INFO = 4
    const val WARN = 5
    const val ERROR = 6

    var level: Int = VERBOSE
        private set

    fun init(level: Int) {
        this.level = level
        getPlatform().initLogger()
    }
}