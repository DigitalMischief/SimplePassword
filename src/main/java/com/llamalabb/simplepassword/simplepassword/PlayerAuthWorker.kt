package com.llamalabb.simplepassword.simplepassword

import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.NamespacedKey
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import java.time.Duration


class PlayerAuthWorker : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (!isPlayerAuthed(event.player)) {
            freezePlayer(event.player)
        } else {
            unfreezePlayer(event.player)
        }
    }

    @EventHandler
    fun onPlayerChat(event: AsyncChatEvent) {
        if (!isPlayerAuthed(event.player)) {
            // if they aren't authed all of their chat messages are cancelled
            // this is so password attempts aren't logged but also so that they can't chat
            event.isCancelled = true

            // get the content of the chat message
            val message = (event.originalMessage() as TextComponent).content()

            // check if the content of the chat message is the password and kick / auth accordingly
            if (message == ConfigRepo.getPassword()) {
                setPlayerAuth(event.player, true)
                Bukkit.getScheduler().runTask(Main.plugin, Runnable {
                    unfreezePlayer(event.player)
                    showPasswordCorrect(event.player)
                })
            } else {
                setPlayerAuth(event.player, false)
                Bukkit.getScheduler().runTask(Main.plugin, Runnable { event.player.kick() })
            }
        }
    }

    @EventHandler
    fun onPlayerToggleSneak(event: PlayerToggleSneakEvent) {
        // this is a bit of a hacky way to ensure a user cannot leave their spectateTarget
        if (!isPlayerAuthed(event.player)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerTeleport(event: PlayerTeleportEvent) {
        if (!isPlayerAuthed(event.player)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerCommand(event: PlayerCommandPreprocessEvent) {
        if (!isPlayerAuthed(event.player)) {
            event.isCancelled = true
        }
    }

    private fun freezePlayer(player: Player) {
        player.gameMode = GameMode.SPECTATOR
        val armorStand = createArmorStand(player)
        player.spectatorTarget = armorStand

        // send a password prompt to the player
        showPasswordPrompt(player)

        // kick player after 60 seconds if they haven't authenticated
        object : BukkitRunnable() {
            override fun run() { if (!isPlayerAuthed(player)) player.kick() }
        }.runTaskLater(Main.plugin, 20*60)

        // remove the spawned armor stand after 75 seconds
        object : BukkitRunnable() {
            override fun run() { armorStand.remove() }
        }.runTaskLater(Main.plugin, 20*75)
    }

    private fun unfreezePlayer(player: Player) {
        player.gameMode = GameMode.SURVIVAL
    }

    private fun showPasswordPrompt(player: Player) {

        val titleStyle = Style.style()
            .color(NamedTextColor.DARK_RED)
            .decorate(TextDecoration.UNDERLINED)
            .build()

        val subtitleStyle = Style.style()
            .color(NamedTextColor.YELLOW)
            .build()

        val titleComponent = Component
            .text("Please enter password")
            .style(titleStyle)

        val subtitleComponent = Component
            .text("Enter the server password into chat")
            .style(subtitleStyle)

        val title = Title.title(
            titleComponent,
            subtitleComponent,
            Title.Times.times(
                Duration.ZERO,
                Duration.ofSeconds(65),
                Duration.ZERO
            )
        )

        player.showTitle(title)
    }

    private fun showPasswordCorrect(player: Player) {

        val titleStyle = Style.style()
            .color(NamedTextColor.GREEN)
            .build()

        val titleComponent = Component
            .text("Success!")
            .style(titleStyle)

        val title = Title.title(
            titleComponent,
            Component.text(""),
            Title.Times.times(
                Duration.ZERO,
                Duration.ofSeconds(3),
                Duration.ZERO
            )
        )

        player.showTitle(title)
    }

    private fun createArmorStand(player: Player): ArmorStand {
        // Spawns an ArmorStand at player location and forces player to spectate the ArmorStand
        return (player.world.spawnEntity(player.location, EntityType.ARMOR_STAND) as ArmorStand).apply {
            isInvisible = true
            isCollidable = false
            isInvulnerable = true
            isVisible = false
            setGravity(false)
        }
    }

    companion object {
        private val PLAYER_DATA_AUTH_KEY = NamespacedKey(Main.plugin, "player_data_auth_key")

        fun isPlayerAuthed(player: Player): Boolean {
            return player.persistentDataContainer
                .getOrDefault(PLAYER_DATA_AUTH_KEY, PersistentDataType.INTEGER, 0)
                .let { if(it == 1) return@let true else false }
        }

        fun setPlayerAuth(player: Player, isAuth: Boolean) {
            player.persistentDataContainer
                .set(PLAYER_DATA_AUTH_KEY, PersistentDataType.INTEGER, if(isAuth) 1 else 0)
        }
    }
}