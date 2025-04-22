package top.ntutn.katbox

import android.app.Application
import top.ntutn.katbox.logger.LoggerFacade
import top.ntutn.katbox.storage.Factory

class MainApplication: Application() {
    companion object {
        lateinit var factory: Factory
    }

    override fun onCreate() {
        super.onCreate()
        LoggerFacade.init(LoggerFacade.VERBOSE)
        factory = Factory(this)
    }
}