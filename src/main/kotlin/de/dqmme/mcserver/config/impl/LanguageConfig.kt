package de.dqmme.mcserver.config.impl

import de.dqmme.mcserver.config.AbstractConfig
import de.dqmme.mcserver.util.deserializeMini
import net.kyori.adventure.text.Component

lateinit var languageConfig: LanguageConfig

class LanguageConfig : AbstractConfig("language.yml") {
    fun getMessageValue(key: String) = yamlConfiguration.getString(key)
}

fun getLanguage(
    key: String,
    variables: HashMap<String, String>? = null,
    keyVariables: HashMap<String, String>? = null
): Component {
    var message = languageConfig.getMessageValue(key) ?: "<red>Nachricht nicht gefunden."
    val prefix = languageConfig.getMessageValue("prefix")

    if (prefix != null) message = message.replace("[PREFIX]", prefix)

    variables?.forEach {
        message = message.replace(it.key, it.value)
    }

    keyVariables?.forEach {
        val variableValue = languageConfig.getMessageValue(it.value)

        if (variableValue != null) message = message.replace(it.key, variableValue)
    }

    return message.deserializeMini()
}