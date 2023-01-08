package one.devsky.bettervanilla.listeners

import com.destroystokyo.paper.ParticleBuilder
import one.devsky.bettervanilla.BetterVanilla
import one.devsky.bettervanilla.events.TimberEvent
import one.devsky.bettervanilla.extensions.*
import org.bukkit.*
import org.bukkit.Tag.LEAVES
import org.bukkit.Tag.LOGS
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.system.measureTimeMillis

class TimberListener : Listener {

//    @EventHandler
//    fun onTimberBreak(event: BlockBreakEvent) {
//        val player = event.player
//        val logBlock: Block = event.block
//        if (logBlock.type in LOGS.values) {
//            Bukkit.getPluginManager()
//                .callEvent(TimberEvent(player, logBlock, player.inventory.itemInMainHand, event))
//        }
//    }

    @EventHandler
    fun onTimber(event: TimberEvent) {
        timberMagic(event.player, event.brokenLog.location, event.usedAxe)
    }

    private fun timberMagic(player: Player, location: Location, handStack: ItemStack) {
        var blocks: LinkedList<Block>? = LinkedList()
        var yValue = location.blockY
        while (yValue < location.world.getHighestBlockYAt(location.blockX, location.blockZ)) {
            val nextLocation: Location = location.add(0.0, 1.0, 0.0)
            val block = nextLocation.block
            if (block.type in LOGS.values || block.type in LEAVES.values) {
                blocks!!.add(nextLocation.block)
                yValue++
            } else {
                break
            }
        }
        var shouldContinue = true
        for ((index, block) in blocks!!.withIndex()) {
            if (shouldContinue.not()) {
                break
            }
            task(index * 5L, plugin = BetterVanilla.instance) {
                if (block.breakNaturally(handStack)) {
                    handStack.durability = (handStack.durability + 1).toShort()
                    if (handStack.type.maxDurability == handStack.durability) {
                        handStack.type = Material.AIR
                        shouldContinue = false
                        return@task
                    }

                    val time = measureTimeMillis {
                        val leaves = testForLeavesSurroundings(block.location).distinct()
                        for (leave in leaves) {
                            leave.breakNaturally()
                        }
                        println("Broke ${leaves.size} leaves")
                    }
                    println("Leave destruction took $time ms")

                    taskAsync(plugin = BetterVanilla.instance) {
                        player.soundExecution()
                        ParticleBuilder(Particle.REDSTONE)
                            .data(Particle.DustOptions(Color.fromRGB(138, 64, 0), 2F))
                            .count(2)
                            .location(block.location.clone().add(.5, .5, .5))
                            .offset(.25, .25, .25)
                            .spawn()
                    }
                }
            }
        }
        blocks = null
    }

    // Test for leaves with a max distance of 6
    private fun testForLeavesSurroundings(location: Location, distance: Int = 6): MutableList<Block> {
        val leaveBlocks: MutableList<Block> = mutableListOf()
        val testLocation = location.clone()
        val surroundings = testLocation.surroundings()
        for (surround in surroundings) {
            if (surround.block.type in LEAVES.values) {
                leaveBlocks.add(surround.block)
                if (distance > 1) {
                    leaveBlocks.addAll(testForLeavesSurroundings(surround.block.location, distance - 1))
                }
            }
        }
        return leaveBlocks
    }
}