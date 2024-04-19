package de.dqmme.mcserver.dataclass

import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import de.dqmme.mcserver.api.PterodactylAPI
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Server(
    @SerialName("_id") val id: Long,
    val name: String,
    val uuid: String,
    val invitedPlayers: List<String>,
    @SerialName("is_private") val isPrivate: Boolean,
    @SerialName("is_on_navigator") val isOnNavigator: Boolean = false
) {
    val identifier = uuid.split("-")[0]

    suspend fun start() = PterodactylAPI.startServer(identifier)

    suspend fun restart() = PterodactylAPI.restartServer(identifier)

    suspend fun stop() = PterodactylAPI.stopServer(identifier)

    private suspend fun sendCommand(command: String) = PterodactylAPI.sendCommand(identifier, command)

    suspend fun installPlugin(link: String, name: String) = sendCommand("installsubserverplugin $link $name")

    suspend fun getClientServer(): ClientServer? {
        return PterodactylAPI.getClientServer(identifier)
    }
}