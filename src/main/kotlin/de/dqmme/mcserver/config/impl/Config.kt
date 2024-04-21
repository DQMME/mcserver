package de.dqmme.mcserver.config.impl

import de.dqmme.mcserver.config.AbstractConfig
import org.bukkit.Location
import java.util.UUID

lateinit var pluginConfig: Config

class Config : AbstractConfig("config.yml") {
    fun getPterodactylHost() = yamlConfiguration.getString("pterodactyl.host")

    fun getPterodactylApplicationToken() = yamlConfiguration.getString("pterodactyl.application_token")

    fun getPterodactylClientToken() = yamlConfiguration.getString("pterodactyl.client_token")

    fun getPterodactylServerOwnerId() = yamlConfiguration.getLong("pterodactyl.server_owner_id")

    fun getPterodactylNodeId() = yamlConfiguration.getLong("pterodactyl.node_id")

    fun getPterodactylNestId() = yamlConfiguration.getLong("pterodactyl.nest_id")

    fun getPterodactylEggId() = yamlConfiguration.getLong("pterodactyl.egg_id")

    fun getPterodactylLocationId() = yamlConfiguration.getLong("pterodactyl.location_id")

    fun getPterodactylAllocationIp() = yamlConfiguration.getString("pterodactyl.allocation_ip")

    fun getMongoConnection() = yamlConfiguration.getString("mongo.connection")

    fun getMongoDatabase() = yamlConfiguration.getString("mongo.database")

    fun getDefaultProfileUUID(): UUID {
        val uuidValue = yamlConfiguration.getString("default_profile_uuid") ?: return UUID.randomUUID()

        return UUID.fromString(uuidValue)
    }

    fun getSubserverPluginName() = yamlConfiguration.getString("subserver_plugin_name")

    fun getServerDescription() = yamlConfiguration.getString("server_description") ?: "Minecraft Server"

    fun getVelocityServerIdentifier() = yamlConfiguration.getString("velocity_server_identifier")

    fun getVelocitySecret() = yamlConfiguration.getString("velocity_secret")

    fun getSpawnLocation() = yamlConfiguration.getLocation("spawn_location")

    fun setSpawnLocation(location: Location) {
        yamlConfiguration.set("spawn_location", location)
        save()
    }
}