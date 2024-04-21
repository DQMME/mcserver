package de.dqmme.mcserver.database

import de.dqmme.mcserver.config.impl.pluginConfig
import de.dqmme.mcserver.dataclass.Server
import org.litote.kmongo.and
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

object Database {
    private lateinit var client: CoroutineClient
    private lateinit var database: CoroutineDatabase
    private lateinit var serverCollection: CoroutineCollection<Server>

    operator fun invoke(): Boolean {
        val connection = pluginConfig.getMongoConnection() ?: return false
        val databaseName = pluginConfig.getMongoDatabase() ?: return false

        System.setProperty(
            "org.litote.mongo.test.mapping.service",
            "org.litote.kmongo.serialization.SerializationClassMappingTypeService"
        )

        client = KMongo.createClient(connection).coroutine
        database = client.getDatabase(databaseName)
        serverCollection = database.getCollection("servers")

        return true
    }

    suspend fun getServers() = serverCollection.find().toList()

    suspend fun getServer(id: Long) = serverCollection.findOne(Server::id eq id)

    suspend fun getNavigatorServers() = serverCollection.find(Server::isOnNavigator eq true).toList()

    suspend fun getPrivateServers() = serverCollection.find(Server::isPrivate eq true).toList()

    suspend fun getPrivateServers(uuid: String) =
        serverCollection.find(and((Server::invitedPlayers::contains)(uuid), Server::isPrivate eq true)).toList()

    suspend fun saveServer(server: Server) = serverCollection.save(server)

    suspend fun deleteServer(id: Long) = serverCollection.deleteOne(Server::id eq id)
}