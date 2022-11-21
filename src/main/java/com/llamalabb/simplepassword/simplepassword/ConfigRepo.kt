package com.llamalabb.simplepassword.simplepassword

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.util.Properties

object ConfigRepo {
    private const val MAIN_DIR = "plugins/SimplePassword"
    private const val CONFIG_FILE_PATH = "${MAIN_DIR}/config.properties"
    private const val PASSWORD_KEY = "password"
    private const val PASSWORD_ATTEMPTS_KEY = "password_attempts"
    private const val DEFAULT_PASSWORD = "serverpw"
    private const val DEFAULT_ATTEMPTS = 5

    private val config = Properties()

    private fun initConfig(): Properties {
        return if (File(CONFIG_FILE_PATH).exists()) {
            config.apply { load(FileInputStream(CONFIG_FILE_PATH)) }
        } else {
            val file = File(CONFIG_FILE_PATH).apply { createNewFile() }
            config.apply {
                setProperty(PASSWORD_KEY, DEFAULT_PASSWORD)
                setProperty(PASSWORD_ATTEMPTS_KEY, DEFAULT_ATTEMPTS.toString())
                store(OutputStreamWriter(FileOutputStream(file)), "")
            }
        }
    }

   fun initLocalStorage() {
       val mainDir = File(MAIN_DIR)
       if (!mainDir.exists()) mainDir.mkdir()
       initConfig()
    }

    fun getPassword() = config[PASSWORD_KEY]

    fun getAttempts(): Int = (config[PASSWORD_ATTEMPTS_KEY] as? String)?.toInt() ?: DEFAULT_ATTEMPTS
}
