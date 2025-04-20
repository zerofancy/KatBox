package top.ntutn.katbox

import android.app.Application
import top.ntutn.katbox.logger.LoggerFacade

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        LoggerFacade.init(LoggerFacade.VERBOSE)
    }
}