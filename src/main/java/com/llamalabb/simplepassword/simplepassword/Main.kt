package com.llamalabb.simplepassword.simplepassword

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    companion object {
        lateinit var plugin: Main private set
    }

    override fun onEnable() {
        plugin = this
        ConfigDataSource.initLocalStorage()
        val pm = Bukkit.getPluginManager()
        pm.registerEvents(PlayerAuthWorker(), this)
        getCommand(Commands.DEAUTH_COMMAND)?.setExecutor(Commands())
    }

    override fun onDisable() {

    }
}