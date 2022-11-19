package com.llamalabb.simplepassword.simplepassword

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.util.Properties

object ConfigManager {
    private const val BASE_FILE_PATH = "./SimplePassword"
    private const val CONFIG_FILE_PATH = "$BASE_FILE_PATH/config.properties"
    private const val PASSWORD_KEY = "password"
    private const val DEFAULT_PASSWORD = "serverpw"

    private val config by lazy { initializeConfig() }

    private fun initializeConfig() : Properties {
        return if (File(CONFIG_FILE_PATH).exists()) {
            Properties().apply { load(FileInputStream(CONFIG_FILE_PATH)) }
        } else {
            Properties().apply {
                set(PASSWORD_KEY, DEFAULT_PASSWORD)
                store(OutputStreamWriter(FileOutputStream(File(CONFIG_FILE_PATH))), "")
            }
        }
    }

    fun getPassword() = config[PASSWORD_KEY]
}
