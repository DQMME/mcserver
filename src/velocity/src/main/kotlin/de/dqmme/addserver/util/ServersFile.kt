package de.dqmme.addserver.util

import com.velocitypowered.api.proxy.ProxyServer
import de.dqmme.addserver.dataclass.Server
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Path

object ServersFile {
    private lateinit var file: File
    private lateinit var servers: MutableList<Server>

    operator fun invoke(dataFolderPath: Path) {
        val dataFolder = dataFolderPath.toFile()

        if (!dataFolder.exists()) dataFolder.mkdir()

        file = File(dataFolder, "servers.json")

        if (!file.exists()) {
            file.createNewFile()
            file.writeText("[]")
        }

        servers = Json.decodeFromString<List<Server>>(file.readText()).toMutableList()
    }

    fun getServers() = servers.toMutableList()

    fun saveServer(proxyServer: ProxyServer, server: Server) {
        servers.add(server)
        saveFile()
        proxyServer.registerServer(server)
    }

    fun removeServer(proxyServer: ProxyServer, id: Long) {
        val server = servers.find { it.id == id } ?: return

        servers.remove(server)
        saveFile()
        proxyServer.unregisterServer(server)
    }

    private fun saveFile() {
        file.writeText(Json.encodeToString(servers))
    }
}