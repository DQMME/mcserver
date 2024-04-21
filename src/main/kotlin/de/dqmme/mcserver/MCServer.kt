package de.dqmme.mcserver

import de.dqmme.mcserver.api.PluginMessaging
import de.dqmme.mcserver.command.registerSetValueCommand
import de.dqmme.mcserver.config.impl.Config
import de.dqmme.mcserver.config.impl.LanguageConfig
import de.dqmme.mcserver.config.impl.NavigatorConfig
import de.dqmme.mcserver.config.impl.RankConfig
import de.dqmme.mcserver.config.impl.ServerPluginsConfig
import de.dqmme.mcserver.config.impl.SkullConfig
import de.dqmme.mcserver.config.impl.languageConfig
import de.dqmme.mcserver.config.impl.navigatorConfig
import de.dqmme.mcserver.config.impl.pluginConfig
import de.dqmme.mcserver.config.impl.rankConfig
import de.dqmme.mcserver.config.impl.serverPluginsConfig
import de.dqmme.mcserver.config.impl.skullConfig
import de.dqmme.mcserver.event.registerAsyncChatEvent
import de.dqmme.mcserver.event.registerBlockBreakEvent
import de.dqmme.mcserver.event.registerBlockPlaceEvent
import de.dqmme.mcserver.event.registerDamageEvent
import de.dqmme.mcserver.event.registerFoodLevelChangeEvent
import de.dqmme.mcserver.event.registerInventoryClickEvent
import de.dqmme.mcserver.event.registerPlayerDropItemEvent
import de.dqmme.mcserver.event.registerPlayerInteractEvent
import de.dqmme.mcserver.event.registerPlayerJoinEvent
import de.dqmme.mcserver.event.registerPlayerQuitEvent
import de.dqmme.mcserver.database.Database
import de.dqmme.mcserver.rank.RankManager
import de.dqmme.mcserver.util.prepareWorlds
import net.axay.kspigot.extensions.pluginManager
import net.axay.kspigot.main.KSpigot
import java.util.logging.Level

class MCServer : KSpigot() {
    override fun startup() {
        //Register configs
        pluginConfig = Config()
        languageConfig = LanguageConfig()
        navigatorConfig = NavigatorConfig()
        rankConfig = RankConfig()
        serverPluginsConfig = ServerPluginsConfig()
        skullConfig = SkullConfig()

        //Connect to database
        if (!Database()) {
            logger.log(Level.SEVERE, "Failed to connect to Database, disabling plugin.")
            pluginManager.disablePlugin(this)
            return
        }

        //Register commands
        registerSetValueCommand()

        //Register events
        registerAsyncChatEvent()
        registerBlockBreakEvent()
        registerBlockPlaceEvent()
        registerDamageEvent()
        registerFoodLevelChangeEvent()
        registerInventoryClickEvent()
        registerPlayerDropItemEvent()
        registerPlayerInteractEvent()
        registerPlayerJoinEvent()
        registerPlayerQuitEvent()

        //Prepare worlds
        prepareWorlds()

        //Register Plugin Messaging Channel
        PluginMessaging.registerMessageChannel()

        //Register rank teams
        RankManager.createTeams()
    }

    override fun shutdown() {
        //Unregister Plugin Messaging Channel
        PluginMessaging.unregisterMessageChannel()
    }
}