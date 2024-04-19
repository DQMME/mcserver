package de.dqmme.mcserver.api

import com.google.gson.JsonParser
import de.dqmme.mcserver.dataclass.PlayerTexture
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID

object MojangAPI {
    //Using JavaURLConnection because Ktor Engine dependency is not quite working, and I'm lazy bro
    //Actually I'm just using this because the shit owningPlayer is just not working (why spigot??)
    fun getPlayerTexture(uuid: UUID): PlayerTexture? {
        val strippedUUID = uuid.toString().replace("-", "")
        val url = URL("https://sessionserver.mojang.com/session/minecraft/profile/${strippedUUID}?unsigned=false")
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"

        if(connection.responseCode != HttpURLConnection.HTTP_OK) return null

        val response = BufferedReader(InputStreamReader(connection.inputStream)).readText()

        try {
            val jsonObject = JsonParser.parseString(response).asJsonObject
            val propertyObject = jsonObject.get("properties").asJsonArray[0].asJsonObject
            val value = propertyObject.get("value").asString
            val signature = propertyObject.get("signature").asString
            return PlayerTexture(value, signature)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}