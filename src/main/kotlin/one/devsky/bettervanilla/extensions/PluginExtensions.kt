package one.devsky.bettervanilla.extensions

import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

fun Plugin.registerEvent(vararg listeners: Listener) =
    listeners.forEach { Bukkit.getPluginManager().registerEvents(it, this) }

fun Plugin.info(message: String) = logger.info(message)
fun Plugin.warn(message: String) = logger.warning(message)
fun Plugin.severe(message: String) = logger.severe(message)
fun Plugin.fine(message: String) = logger.fine(message)

fun WithPlugin<*>.info(message: String) = plugin.info(message)
fun WithPlugin<*>.warn(message: String) = plugin.warn(message)
fun WithPlugin<*>.severe(message: String) = plugin.severe(message)
fun WithPlugin<*>.fine(message: String) = plugin.fine(message)

interface WithPlugin<T: Plugin> {val plugin: T}