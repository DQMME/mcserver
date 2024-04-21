package de.dqmme.mcserver.api

import com.mattmalec.pterodactyl4j.DataType
import com.mattmalec.pterodactyl4j.EnvironmentValue
import com.mattmalec.pterodactyl4j.PteroBuilder
import com.mattmalec.pterodactyl4j.application.entities.ApplicationAllocation
import com.mattmalec.pterodactyl4j.application.entities.ApplicationEgg
import com.mattmalec.pterodactyl4j.application.entities.ApplicationServer
import com.mattmalec.pterodactyl4j.application.entities.ApplicationUser
import com.mattmalec.pterodactyl4j.application.entities.Location
import com.mattmalec.pterodactyl4j.application.entities.Nest
import com.mattmalec.pterodactyl4j.application.entities.Node
import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import com.mattmalec.pterodactyl4j.client.entities.Directory
import com.mattmalec.pterodactyl4j.client.entities.GenericFile
import com.mattmalec.pterodactyl4j.client.entities.Utilization
import de.dqmme.mcserver.config.impl.pluginConfig
import de.dqmme.mcserver.dataclass.Server
import de.dqmme.mcserver.util.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.axay.kspigot.main.KSpigotMainInstance
import java.util.logging.Level

object PterodactylAPI {
    private val application = PteroBuilder.createApplication(
        pluginConfig.getPterodactylHost(), pluginConfig.getPterodactylApplicationToken()
    )

    private val client = PteroBuilder.createClient(
        pluginConfig.getPterodactylHost(), pluginConfig.getPterodactylClientToken()
    )

    suspend fun deleteFile(clientServer: ClientServer, file: GenericFile): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                clientServer.fileManager.delete().addFile(file).execute()
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun getDirectory(clientServer: ClientServer, directoryName: String): Directory? {
        return withContext(Dispatchers.IO) {
            try {
                clientServer.retrieveDirectory(directoryName).execute()
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun startServer(identifier: String): Boolean {
        val clientServer = getClientServer(identifier) ?: return false

        return withContext(Dispatchers.IO) {
            try {
                clientServer.start().execute()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun restartServer(identifier: String): Boolean {
        val clientServer = getClientServer(identifier) ?: return false

        return withContext(Dispatchers.IO) {
            try {
                clientServer.restart().execute()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun stopServer(identifier: String): Boolean {
        val clientServer = getClientServer(identifier) ?: return false

        return withContext(Dispatchers.IO) {
            try {
                clientServer.stop().execute()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun createServer(
        name: String,
        port: Int,
        cpuPercentage: Long = 50L,
        storage: Pair<Long, DataType> = 5L to DataType.GB,
        memory: Pair<Long, DataType> = 4L to DataType.GB,
        isPrivate: Boolean = false,
        description: String = pluginConfig.getServerDescription(),
        eggId: Long = pluginConfig.getPterodactylEggId(),
        locationId: Long = pluginConfig.getPterodactylLocationId(),
        nestId: Long = pluginConfig.getPterodactylNestId(),
        nodeId: Long = pluginConfig.getPterodactylNodeId(),
        ownerId: Long = pluginConfig.getPterodactylServerOwnerId(),
        dockerImage: String = "ghcr.io/pterodactyl/yolks:java_17",
        maxAllocations: Long = 1L,
        maxDatabases: Long = 0L,
        jarFile: String = "server.jar",
        version: String = "1.20.4",
        velocitySecret: String? = pluginConfig.getVelocitySecret()
    ): Pair<ApplicationServer?, String?> {
        val nest = getNest(nestId)

        if (nest == null) {
            KSpigotMainInstance.logger.log(Level.WARNING, "Server creation failed - Nest not found")
            return null to "nest_not_found"
        }

        val egg = getEgg(nest, eggId)

        if (egg == null) {
            KSpigotMainInstance.logger.log(Level.WARNING, "Server creation failed - Egg not found")
            return null to "egg_not_found"
        }

        val location = getLocation(locationId)

        if (location == null) {
            KSpigotMainInstance.logger.log(Level.WARNING, "Server creation failed - Location not found")
            return null to "location_not_found"
        }

        val owner = getUser(ownerId)

        if (owner == null) {
            KSpigotMainInstance.logger.log(Level.WARNING, "Server creation failed - Owner user not found")
            return null to "owner_not_found"
        }

        val node = getNode(nodeId)

        if (node == null) {
            KSpigotMainInstance.logger.log(Level.WARNING, "Server creation failed - Node not found")
            return null to "node_not_found"
        }

        var allocation = getAllocation(port)

        if (allocation != null && allocation.isAssigned) {
            KSpigotMainInstance.logger.log(Level.WARNING, "Server creation failed - Port already used")
            return null to "port_used"
        }

        if (allocation == null) {
            if (!createAllocation(node.idLong, port, pluginConfig.getPterodactylAllocationIp())) {
                KSpigotMainInstance.logger.log(Level.WARNING, "Server creation failed - Could not create allocation")
                return null to "could_not_create_allocation"
            }

            allocation = getAllocation(port)
        }

        if (allocation == null) {
            KSpigotMainInstance.logger.log(Level.WARNING, "Server creation failed - Could not find allocation")
            return null to "allocation_not_found"
        }

        if (velocitySecret == null) {
            KSpigotMainInstance.logger.log(Level.WARNING, "Server creation failed - Velocity secret not found")
            return null to "velocity_secret_not_found"
        }

        val environmentVariables = mapOf(
            "SERVER_JARFILE" to EnvironmentValue.of(jarFile),
            "MINECRAFT_VERSION" to EnvironmentValue.of(version),
            "VELOCITY_SECRET" to EnvironmentValue.of(velocitySecret)
        )

        return withContext(Dispatchers.IO) {
            try {
                val server = application.createServer()
                    .setName(name)
                    .setDescription(description)
                    .setOwner(owner)
                    .setEgg(egg)
                    .setAllocation(allocation)
                    .setDatabases(maxDatabases)
                    .setAllocations(maxAllocations)
                    .setCPU(cpuPercentage)
                    .setDisk(storage.first, storage.second)
                    .setMemory(memory.first, memory.second)
                    .setDockerImage(dockerImage)
                    .startOnCompletion(true)
                    .skipScripts(false)
                    .setEnvironment(environmentVariables)
                    .execute()

                Database.saveServer(Server(server.idLong, server.name, server.uuid.toString(), listOf(), isPrivate))

                server to null
            } catch (e: Exception) {
                e.printStackTrace()
                null to "other"
            }
        }
    }

    suspend fun sendCommand(identifier: String, command: String): Boolean {
        val clientServer = getClientServer(identifier) ?: return false

        return withContext(Dispatchers.IO) {
            try {
                clientServer.sendCommand(command).execute()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun addProxyServer(id: Long, port: Int) {
        val velocityServerIdentifier = pluginConfig.getVelocityServerIdentifier() ?: return
        val serverIp = pluginConfig.getPterodactylAllocationIp()!!

        sendCommand(velocityServerIdentifier, "addserver $id $serverIp $port")
    }

    suspend fun removeProxyServer(id: Long) {
        val velocityServerIdentifier = pluginConfig.getVelocityServerIdentifier() ?: return

        sendCommand(velocityServerIdentifier, "removeserver $id")
    }

    suspend fun editServerSpecs(clientServer: ClientServer, memory: Long, cpu: Long, disk: Long): Boolean {
        val applicationServer = getApplicationServer(clientServer.internalIdLong) ?: return false

        return withContext(Dispatchers.IO) {
            try {
                applicationServer.buildManager
                    .setMemory(memory, DataType.GB)
                    .setCPU(cpu)
                    .setDisk(disk, DataType.GB)
                    .execute()
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun deleteServer(clientServer: ClientServer): Boolean {
        val applicationServer = getApplicationServer(clientServer.internalIdLong) ?: return false

        return withContext(Dispatchers.IO) {
            try {
                applicationServer.controller.delete(false).execute()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun checkPortAvailable(port: Int): Boolean {
        val allocation = getAllocation(port)

        return allocation == null || !allocation.isAssigned
    }

    suspend fun changeServerName(clientServer: ClientServer, name: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                clientServer.manager.setName(name).execute()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    private suspend fun createAllocation(nodeId: Long, port: Int, ip: String?): Boolean {
        return withContext(Dispatchers.IO) {
            if (ip == null) return@withContext false

            val node = getNode(nodeId) ?: return@withContext false

            try {
                node.allocationManager.createAllocation()
                    .setPorts("$port")
                    .setIP(ip)
                    .execute()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    private suspend fun getAllocation(port: Int): ApplicationAllocation? {
        return withContext(Dispatchers.IO) {
            try {
                application.retrieveAllocations().execute().find { it.portInt == port }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private suspend fun getNode(id: Long): Node? {
        return withContext(Dispatchers.IO) {
            try {
                application.retrieveNodeById(id).execute()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private suspend fun getUser(id: Long): ApplicationUser? {
        return withContext(Dispatchers.IO) {
            try {
                application.retrieveUserById(id).execute()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private suspend fun getLocation(id: Long): Location? {
        return withContext(Dispatchers.IO) {
            try {
                application.retrieveLocationById(id).execute()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private suspend fun getEgg(nest: Nest, id: Long): ApplicationEgg? {
        return withContext(Dispatchers.IO) {
            try {
                application.retrieveEggById(nest, id).execute()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private suspend fun getNest(id: Long): Nest? {
        return withContext(Dispatchers.IO) {
            try {
                application.retrieveNestById(id).execute()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getUtilization(clientServer: ClientServer): Utilization? {
        return withContext(Dispatchers.IO) {
            try {
                clientServer.retrieveUtilization().execute()
            } catch (e: Exception) {
                //e.printStackTrace()
                null
            }
        }
    }

    private suspend fun getApplicationServer(id: Long): ApplicationServer? {
        return withContext(Dispatchers.IO) {
            try {
                application.retrieveServerById(id).execute()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getClientServer(identifier: String): ClientServer? {
        return withContext(Dispatchers.IO) {
            try {
                client.retrieveServerByIdentifier(identifier).execute()
            } catch (e: Exception) {
                //e.printStackTrace()
                null
            }
        }
    }
}
