package com.llamalabb.simplepassword.simplepassword

import org.bukkit.entity.Player
import java.util.UUID

object PlayerSessionRepo {
    private val uuidToAttemptsRemaining = HashMap<UUID, Int>()

    private fun setNewPlayer(player: Player) {
        uuidToAttemptsRemaining[player.uniqueId] = ConfigRepo.getAttempts()
    }

    fun getAttemptsRemaining(player: Player): Int {
        return uuidToAttemptsRemaining[player.uniqueId] ?: run {
            setNewPlayer(player)
            ConfigRepo.getAttempts()
        }
    }

    fun decrementAttempt(player: Player) {
        uuidToAttemptsRemaining[player.uniqueId] =
            uuidToAttemptsRemaining[player.uniqueId]?.dec() ?: (ConfigRepo.getAttempts() - 1)
    }
}