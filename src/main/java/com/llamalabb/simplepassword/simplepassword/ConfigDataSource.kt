package com.llamalabb.simplepassword.simplepassword

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.util.Properties

object ConfigDataSource {

    const val ATTEMPTS_REPLACEMENT_KEY = "{attempts}"

    private const val MAIN_DIR = "plugins/SimplePassword"
    private const val CONFIG_FILE_PATH = "${MAIN_DIR}/config.properties"

    private const val PASSWORD_KEY = "password"
    private const val PASSWORD_ATTEMPTS_KEY = "password_attempts"
    private const val PASSWORD_PROMPT_TITLE_KEY = "password_prompt_title"
    private const val PASSWORD_PROMPT_SUBTITLE_KEY = "password_prompt_subtitle"
    private const val PASSWORD_INCORRECT_TITLE_KEY = "password_incorrect_title"
    private const val PASSWORD_INCORRECT_SUBTITLE_KEY = "password_incorrect_subtitle"
    private const val PASSWORD_SUCCESS_TITLE_KEY = "password_success_title"
    private const val PASSWORD_SUCCESS_SUBTITLE_KEY = "password_success_subtitle"
    private const val BAN_MESSAGE_KEY = "ban_message"

    private const val DEFAULT_PASSWORD = "serverpw"
    private const val DEFAULT_ATTEMPTS = 5
    private const val DEFAULT_PASSWORD_PROMPT_TITLE = "Please enter password"
    private const val DEFAULT_PASSWORD_PROMPT_SUBTITLE ="Enter the server password into chat"
    private const val DEFAULT_PASSWORD_INCORRECT_TITLE = "Incorrect Password"
    private const val DEFAULT_PASSWORD_INCORRECT_SUBTITLE = "$ATTEMPTS_REPLACEMENT_KEY attempts remaining"
    private const val DEFAULT_PASSWORD_SUCCESS_TITLE = "Success!"
    private const val DEFAULT_PASSWORD_SUCCESS_SUBTITLE = ""
    private const val DEFAULT_BAN_MESSAGE = "Too many incorrect password attempts"



    private val config = Properties()

    private fun initConfig() {
        if (File(CONFIG_FILE_PATH).exists()) {
            config.apply { load(FileInputStream(CONFIG_FILE_PATH)) }
        } else {
            createDefaultConfig()
        }
    }

    private fun createDefaultConfig() {
        val file = File(CONFIG_FILE_PATH).apply { createNewFile() }
        config.apply {
            setProperty(PASSWORD_KEY, DEFAULT_PASSWORD)
            setProperty(PASSWORD_ATTEMPTS_KEY, DEFAULT_ATTEMPTS.toString())
            setProperty(PASSWORD_PROMPT_TITLE_KEY, DEFAULT_PASSWORD_PROMPT_TITLE)
            setProperty(PASSWORD_PROMPT_SUBTITLE_KEY, DEFAULT_PASSWORD_PROMPT_SUBTITLE)
            setProperty(PASSWORD_INCORRECT_TITLE_KEY, DEFAULT_PASSWORD_INCORRECT_TITLE)
            setProperty(PASSWORD_INCORRECT_SUBTITLE_KEY, DEFAULT_PASSWORD_INCORRECT_SUBTITLE)
            setProperty(PASSWORD_SUCCESS_TITLE_KEY, DEFAULT_PASSWORD_SUCCESS_TITLE)
            setProperty(PASSWORD_SUCCESS_SUBTITLE_KEY, DEFAULT_PASSWORD_SUCCESS_SUBTITLE)
            setProperty(BAN_MESSAGE_KEY, DEFAULT_BAN_MESSAGE)
            store(OutputStreamWriter(FileOutputStream(file)), "")
        }
    }

   fun initLocalStorage() {
       val mainDir = File(MAIN_DIR)
       if (!mainDir.exists()) mainDir.mkdir()
       initConfig()
    }

    fun getPassword(): String = (config[PASSWORD_KEY] as? String)
        ?: DEFAULT_PASSWORD

    fun getMaximumAttempts(): Int = (config[PASSWORD_ATTEMPTS_KEY] as? String)?.toInt()
        ?: DEFAULT_ATTEMPTS

    fun getPasswordPromptTitle() = (config[PASSWORD_PROMPT_TITLE_KEY] as? String)
        ?: DEFAULT_PASSWORD_PROMPT_TITLE

    fun getPasswordPromptSubtitle() = (config[PASSWORD_PROMPT_SUBTITLE_KEY] as? String)
        ?: DEFAULT_PASSWORD_PROMPT_SUBTITLE

    fun getPasswordIncorrectTitle() = (config[PASSWORD_INCORRECT_TITLE_KEY] as? String)
        ?: DEFAULT_PASSWORD_INCORRECT_TITLE

    fun getPasswordIncorrectSubtitle() = (config[PASSWORD_INCORRECT_SUBTITLE_KEY] as? String)
        ?: DEFAULT_PASSWORD_INCORRECT_SUBTITLE

    fun getPasswordSuccessTitle() = (config[PASSWORD_SUCCESS_TITLE_KEY] as? String)
        ?: DEFAULT_PASSWORD_SUCCESS_TITLE

    fun getPasswordSuccessSubtitle() = (config[PASSWORD_SUCCESS_SUBTITLE_KEY] as? String)
        ?: DEFAULT_PASSWORD_SUCCESS_SUBTITLE

    fun getBanMessage() = (config[BAN_MESSAGE_KEY] as? String)
        ?: DEFAULT_BAN_MESSAGE
}
