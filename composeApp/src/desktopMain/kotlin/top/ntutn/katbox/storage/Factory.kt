package top.ntutn.katbox.storage

import org.jetbrains.skiko.hostOs
import java.io.File

actual class Factory  {
    actual fun createConnectionDataStore(): ConnectionDataStore {
        return ConnectionDataStore {
            val configFile = getPlatformConfigFile()
            configFile.absolutePath
        }
    }

    private fun getPlatformConfigFile(): File {
        val configDir = when  {
            hostOs.isWindows -> getWindowsConfigDir()
            hostOs.isMacOS -> getMacConfigDir()
            hostOs.isLinux -> getLinuxConfigDir()
            else -> getFallbackConfigDir()
        }.apply { mkdirs() }

        return File(configDir, "connection.json")
    }

    private fun getWindowsConfigDir(): File {
        return File(
            System.getenv("APPDATA") ?: System.getProperty("user.home"),
            "KatBox"  // Windows推荐使用应用首字母大写
        )
    }

    private fun getMacConfigDir(): File {
        return File(
            System.getProperty("user.home"),
            "Library/Application Support/katbox"  // macOS标准应用支持目录
        )
    }

    private fun getLinuxConfigDir(): File {
        val baseDir = System.getenv("XDG_CONFIG_HOME")
            ?: File(System.getProperty("user.home"), ".config").path
        return File(baseDir, "katbox")
    }

    private fun getFallbackConfigDir(): File {
        return File(System.getProperty("user.home"), ".katbox")
    }

}