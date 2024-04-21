package de.dqmme.addserver.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class Server(
    val id: Long,
    val ip: String,
    val port: Int
)
