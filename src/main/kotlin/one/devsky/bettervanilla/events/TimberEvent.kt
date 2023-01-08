package one.devsky.bettervanilla.events

import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack

class TimberEvent(
    var player: Player,
    var brokenLog: Block,
    var usedAxe: ItemStack,
    blockBreakEvent: BlockBreakEvent
) : Event(), Cancellable {

    init {
        blockBreakEvent.isDropItems = false
    }

    private var cancelEvent = false

    companion object {
        private var HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList() = HANDLERS
    }

    override fun isCancelled(): Boolean = cancelEvent

    override fun setCancelled(cancel: Boolean) {
        cancelEvent = cancel
    }

    override fun getHandlers(): HandlerList = HANDLERS
}
