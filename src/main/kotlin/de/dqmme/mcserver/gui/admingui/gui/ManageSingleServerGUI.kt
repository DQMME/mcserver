package de.dqmme.mcserver.gui.admingui.gui

import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import com.mattmalec.pterodactyl4j.client.entities.GenericFile
import com.mattmalec.pterodactyl4j.client.entities.Utilization
import de.dqmme.mcserver.api.PterodactylAPI
import de.dqmme.mcserver.dataclass.ManageSingleServerPage
import de.dqmme.mcserver.dataclass.Server
import de.dqmme.mcserver.gui.admingui.openAdminGUI
import de.dqmme.mcserver.gui.admingui.page.manageserver.deletePluginsPage
import de.dqmme.mcserver.gui.admingui.page.manageserver.downloadPredefinedPluginsPage
import de.dqmme.mcserver.gui.admingui.page.manageserver.managePlayersPage
import de.dqmme.mcserver.gui.admingui.page.manageserver.pluginMenuPage
import de.dqmme.mcserver.gui.admingui.page.manageserver.startPage
import de.dqmme.mcserver.gui.openWaitGUI
import de.dqmme.mcserver.database.Database
import de.dqmme.mcserver.util.deserializeMini
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.kSpigotGUI
import net.axay.kspigot.gui.openGUI
import net.axay.kspigot.runnables.task
import org.bukkit.entity.Player

fun Player.openManageSingleServerGUI(
    server: Server,
    serverInfo: ClientServer,
    utilization: Utilization?,
    plugins: List<GenericFile>,
    pageToOpen: ManageSingleServerPage = ManageSingleServerPage.START
) = task(true) {
    openGUI(kSpigotGUI(GUIType.FIVE_BY_NINE) {
        title = "<green>Server <gold>${serverInfo.name} <green>bearbeiten".deserializeMini()

        startPage(server, serverInfo, utilization, pageToOpen)

        pluginMenuPage(server, serverInfo, utilization, pageToOpen)

        downloadPredefinedPluginsPage(server, serverInfo, utilization, plugins, pageToOpen)

        deletePluginsPage(server, serverInfo, utilization, plugins, pageToOpen)

        managePlayersPage(server, serverInfo, utilization, pageToOpen)
    })
}

suspend fun Player.openReloadedManageSingleServerGUI(
    server: Server,
    serverInfo: ClientServer,
    openWaitGUI: Boolean = false,
    pageToOpen: ManageSingleServerPage = ManageSingleServerPage.START
) {
    if (openWaitGUI) {
        openWaitGUI()
    }

    val newServer = Database.getServer(server.id)
    val newServerInfo = PterodactylAPI.getClientServer(serverInfo.identifier)

    if (newServer == null || newServerInfo == null) {
        openAdminGUI(true)
        return
    }

    val newPlugins = PterodactylAPI.getDirectory(newServerInfo, "plugins")?.files

    if (newPlugins == null) {
        openAdminGUI(true)
        return
    }

    val utilization = PterodactylAPI.getUtilization(newServerInfo)

    task(true) {
        openManageSingleServerGUI(newServer, newServerInfo, utilization, newPlugins, pageToOpen)
    }
}