package com.llamalabb.simplepassword.simplepassword

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Commands : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) { return true }
        if (cmd.name.lowercase() == DEAUTH_COMMAND) {
            PlayerAuthWorker.setPlayerAuth(sender, false)
            sender.kick()
        }
        return true
    }

    companion object {
        const val DEAUTH_COMMAND = "deauthme"
    }

}