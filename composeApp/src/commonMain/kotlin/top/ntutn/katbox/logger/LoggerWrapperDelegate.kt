package top.ntutn.katbox.logger

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class LoggerWrapperDelegate(tag: String): ReadOnlyProperty<Any?, LoggerWrapper> {
    private val logger = LoggerWrapper(tag)

    override fun getValue(thisRef: Any?, property: KProperty<*>): LoggerWrapper {
        return logger
    }
}

fun loggerFacade(tag: String) = LoggerWrapperDelegate(tag)

