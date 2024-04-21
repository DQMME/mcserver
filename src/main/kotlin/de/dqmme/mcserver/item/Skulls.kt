package de.dqmme.mcserver.item

import com.destroystokyo.paper.profile.ProfileProperty
import de.dqmme.mcserver.api.MojangAPI
import de.dqmme.mcserver.config.impl.pluginConfig
import de.dqmme.mcserver.config.impl.skullConfig
import de.dqmme.mcserver.util.deserializeMini
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.main.KSpigotMainInstance
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

object Skulls {
    private val defaultProfile = KSpigotMainInstance.server.createProfile(pluginConfig.getDefaultProfileUUID())

    val arrowUp = configItem("arrow_up")
    val arrowDown = configItem("arrow_down")
    val arrowLeft = configItem("arrow_left")
    val limeArrowDown = configItem("lime_arrow_down")
    val grayArrowDown = configItem("gray_arrow_down")
    val yellowArrowDown = configItem("yellow_arrow_down")
    val plus = configItem("plus")
    val grayX = configItem("gray_x")
    val redX = configItem("red_x")

    private fun configItem(key: String): ItemStack {
        return createItem(skullConfig.getSkullBase64(key))
    }

    private fun createItem(base64: String): ItemStack {
        defaultProfile.setProperty(ProfileProperty("textures", base64))

        return itemStack(Material.PLAYER_HEAD) {
            meta<SkullMeta> {
                playerProfile = defaultProfile
            }
        }
    }

    fun getPlayerHead(offlinePlayer: OfflinePlayer): ItemStack {
        val playerTexture =
            MojangAPI.getPlayerTexture(offlinePlayer.uniqueId) ?: return itemStack(Material.PLAYER_HEAD) {
                meta {
                    name = "<yellow>$offlinePlayer.name".deserializeMini()
                }
            }

        defaultProfile.setProperty(ProfileProperty("textures", playerTexture.value, playerTexture.signature))

        return itemStack(Material.PLAYER_HEAD) {
            meta<SkullMeta> {
                name = "<yellow>${offlinePlayer.name}".deserializeMini()

                playerProfile = defaultProfile
            }
        }
    }
}