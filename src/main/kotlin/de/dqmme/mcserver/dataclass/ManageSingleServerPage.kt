package de.dqmme.mcserver.dataclass

enum class ManageSingleServerPage(val defaultNumber: Int) {
    START(1),
    PLUGIN_MENU(2),
    DOWNLOAD_PREDEFINED_PLUGINS(3),
    DELETE_PLUGINS(4),
    MANAGE_PLAYERS(5);

    companion object {
        val default = hashMapOf(
            START to START.defaultNumber,
            PLUGIN_MENU to PLUGIN_MENU.defaultNumber,
            DOWNLOAD_PREDEFINED_PLUGINS to DOWNLOAD_PREDEFINED_PLUGINS.defaultNumber,
            DELETE_PLUGINS to DELETE_PLUGINS.defaultNumber,
            MANAGE_PLAYERS to MANAGE_PLAYERS.defaultNumber
        )
    }
}