package one.devsky.bettervanilla

import one.devsky.bettervanilla.annotations.RegisterCommand
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.command.PluginCommand
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.reflections8.Reflections
import kotlin.system.measureTimeMillis

class BetterVanilla : JavaPlugin() {

    companion object {
        lateinit var instance: BetterVanilla
            private set
    }

    init {
        instance = this
    }

    override fun onEnable() {
        // Plugin startup logic
        val time = measureTimeMillis {
            registerAll()
        }
        println("Plugin enabled in $time ms")
        println("BetterVanilla is now tweaking your vanilla behavior!")
    }

    private fun registerAll() {
        val reflections = Reflections("one.devsky.bettervanilla")

        val timeListeners = measureTimeMillis {
            for (clazz in reflections.getSubTypesOf(Listener::class.java)) {
                try {
                    val constructor = clazz.getDeclaredConstructor()

                    constructor.isAccessible = true

                    val event = constructor.newInstance() as Listener

                    Bukkit.getPluginManager().registerEvents(event, this)
                    Bukkit.getConsoleSender()
                        .sendMessage("Listener ${event.javaClass.simpleName} registered")
                } catch (exception: InstantiationError) {
                    exception.printStackTrace()
                } catch (exception: IllegalAccessException) {
                    exception.printStackTrace()
                }
            }
        }
        println("Registered listeners in $timeListeners ms")

        val timeCommands = measureTimeMillis {
            for (clazz in reflections.getTypesAnnotatedWith(RegisterCommand::class.java)) {
                try {
                    val annotation: RegisterCommand = clazz.getAnnotation(RegisterCommand::class.java)

                    val pluginClass: Class<PluginCommand> = PluginCommand::class.java
                    val constructor = pluginClass.getDeclaredConstructor(String::class.java, Plugin::class.java)

                    constructor.isAccessible = true

                    val command: PluginCommand = constructor.newInstance(annotation.name, this)

                    command.aliases = annotation.availableAliases.toList()
                    command.description = annotation.description
                    command.permission = annotation.requiredPermission
                    command.setExecutor(clazz.getDeclaredConstructor().newInstance() as CommandExecutor)

                    Bukkit.getCommandMap().register("bettervanilla", command)
                    Bukkit.getConsoleSender().sendMessage("Command ${command.name} registered")

                } catch (exception: InstantiationError) {
                    exception.printStackTrace()
                } catch (exception: IllegalAccessException) {
                    exception.printStackTrace()
                }
            }
        }
        println("Registered commands in $timeCommands ms")
    }

}