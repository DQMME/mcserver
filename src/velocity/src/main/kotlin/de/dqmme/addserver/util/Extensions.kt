package de.dqmme.addserver.util

import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.server.ServerInfo
import de.dqmme.addserver.dataclass.Server
import java.net.InetSocketAddress

fun ProxyServer.registerServer(server: Server) {
    registerServer(ServerInfo(server.id.toString(), InetSocketAddress(server.ip, server.port)))
}

fun ProxyServer.unregisterServer(server: Server) {
    unregisterServer(ServerInfo(server.id.toString(), InetSocketAddress(server.ip, server.port)))
}