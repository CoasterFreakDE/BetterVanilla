package one.devsky.bettervanilla.listeners

import one.devsky.bettervanilla.types.Link
import org.bukkit.CropState
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.data.Ageable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class RightClickCropListener : Listener {

    @EventHandler
    fun onRightClickCrop(event: PlayerInteractEvent) = with(event) {
        if(hand != EquipmentSlot.HAND) return@with
        if(item != null) return@with

        val block = clickedBlock ?: return@with
        val material = block.type

        // Now we know that the player is right-clicking a block
        if(material.name in CropType.values().map { it.name }) {
            val cropType = CropType.valueOf(material.name)
            // And that the block is a crop
            // So we can now do something with it
            val crop = block.blockData as Ageable
            if(crop.age == crop.maximumAge) {
                block.type = Material.AIR
                block.state.update()
                block.type = material
                block.state.update()
                val location = block.location
                val world = location.world
                for(drop in cropType.drops) {
                    if (drop.value > Math.random()) {
                        world.dropItemNaturally(location, ItemStack(drop.key))
                        world.playSound(location, Sound.ENTITY_SHEEP_SHEAR, 0.05f, 1.3f)
                    }
                }
            }
        }
    }
}

enum class CropType(val drops: List<Link<Material, Double>>) {
    WHEAT(listOf(Link(Material.WHEAT, 1.0), Link(Material.WHEAT_SEEDS, 0.5), Link(Material.WHEAT_SEEDS, 0.3))),
    CARROTS(listOf(Link(Material.CARROT, 1.0), Link(Material.CARROT, 0.7), Link(Material.CARROT, 0.3), Link(Material.CARROT, 0.1))),
    POTATOES(listOf(Link(Material.POTATO, 1.0), Link(Material.POTATO, 0.7), Link(Material.POTATO, 0.3), Link(Material.POTATO, 0.1), Link(Material.POISONOUS_POTATO, 0.02))),
    BEETROOTS(listOf(Link(Material.BEETROOT, 1.0), Link(Material.BEETROOT_SEEDS, 0.7), Link(Material.BEETROOT_SEEDS, 0.3), Link(Material.BEETROOT_SEEDS, 0.1))),
}