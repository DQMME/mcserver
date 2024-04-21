package de.dqmme.mcserver.dataclass

data class Rank(
    val id: String,
    val priority: Int,
    val permission: String?,
    val prefix: String?,
    val suffix: String?
)
