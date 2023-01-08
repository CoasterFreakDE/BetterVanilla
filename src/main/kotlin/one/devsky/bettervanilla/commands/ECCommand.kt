package one.devsky.bettervanilla.commands

import one.devsky.bettervanilla.annotations.RegisterCommand
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@RegisterCommand(
    name = "ec",
    description = "Opens your enderchest",
    requiredPermission = "",
    availableAliases = ["enderchest"]
)
class ECCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender is Player) {
            sender.openInventory(sender.enderChest)
        }

        return true
    }
}