package com.llamalabb.simplepassword.simplepassword

import org.bukkit.entity.Player
import java.util.UUID
import kotlin.math.max

object PlayerSessionRepo {
    private val uuidToAttemptsRemaining = HashMap<UUID, Int>()

    private fun setNewPlayer(player: Player) {
        uuidToAttemptsRemaining[player.uniqueId] = ConfigRepo.getAttempts()
    }

    fun resetPlayerAttempts(player: Player) {
        uuidToAttemptsRemaining.remove(player.uniqueId)
    }

    fun getAttemptsRemaining(player: Player): Int {
        return uuidToAttemptsRemaining[player.uniqueId] ?: run {
            setNewPlayer(player)
            ConfigRepo.getAttempts()
        }
    }

    fun decrementAttempt(player: Player) {
        val newValue = max(0, uuidToAttemptsRemaining[player.uniqueId]?.dec() ?: (ConfigRepo.getAttempts() - 1))
        uuidToAttemptsRemaining[player.uniqueId] = newValue
    }
}