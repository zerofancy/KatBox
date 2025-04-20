package top.ntutn.katbox.logger

interface ILogger {
    fun log(message: String, throwable: Throwable? = null)
}
