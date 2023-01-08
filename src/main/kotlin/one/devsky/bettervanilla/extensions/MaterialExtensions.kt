package one.devsky.bettervanilla.extensions

import com.destroystokyo.paper.MaterialTags
import com.google.common.collect.Multimap
import org.bukkit.Material
import org.bukkit.Tag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.material.MaterialData
import java.util.*

inline fun item(
    material: Material,
    amount: Int = 1,
    data: Short = 0,
    meta: ItemMeta.() -> Unit = {},
): ItemStack = ItemStack(material, amount, data).meta(meta)

inline fun <reified T : ItemMeta> metadataItem(
    material: Material,
    amount: Int = 1,
    data: Short = 0,
    meta: T.() -> Unit,
): ItemStack = ItemStack(material, amount, data).meta(meta)

inline fun <reified T : ItemMeta> ItemStack.meta(
    block: T.() -> Unit,
): ItemStack = apply {
    itemMeta = (itemMeta as? T)?.apply(block) ?: itemMeta
}

fun ItemStack.displayName(displayName: String): ItemStack = meta<ItemMeta> {
    displayName(displayName)
}

fun ItemStack.lore(lore: List<String>): ItemStack = meta<ItemMeta> {
    this.lore(lore.convert { this.adventureText().asComponent() }.toMutableList())
}

inline fun Material.asItemStack(
    amount: Int = 1,
    data: Short = 0,
    meta: ItemMeta.() -> Unit = {},
): ItemStack = item(this, amount, data, meta)

fun Material.asMaterialData(
    data: Byte = 0,
) = MaterialData(this, data)

fun MaterialData.toItemStack(
    amount: Int = 1,
    meta: ItemMeta.() -> Unit = {},
) = toItemStack(amount).meta(meta)

/**
 * get head from base64
 */
val gameProfileClass by lazy { Class.forName("com.mojang.authlib.GameProfile") }
val propertyClass by lazy { Class.forName("com.mojang.authlib.properties.Property") }
val getPropertiesMethod by lazy { gameProfileClass.getMethod("getProperties") }
val gameProfileConstructor by lazy { gameProfileClass.getConstructor(UUID::class.java, String::class.java) }
val propertyConstructor by lazy { propertyClass.getConstructor(String::class.java, String::class.java) }

fun headFromBase64(base64: String): ItemStack {
    val item = ItemStack(Material.PLAYER_HEAD, 1)
    val meta = item.itemMeta as SkullMeta

    val profile = gameProfileConstructor.newInstance(UUID.randomUUID(), null as String?)
    val properties = getPropertiesMethod.invoke(profile).forceCast<Multimap<Any, Any>>()
    properties.put("textures", propertyConstructor.newInstance("textures", base64))

    val profileField =
        meta.javaClass.getDeclaredField("profile")
            .apply { isAccessible = true }
    profileField.set(meta, profile)

    return item.apply { itemMeta = meta }
}
fun ItemMeta.displayName(name: String) = this.displayName(name.adventureText())

@Deprecated(message = "Spade is an shovel!", replaceWith = ReplaceWith("isShovel"))
inline val Material.isSpade: Boolean get() = isShovel

inline val Material.isPickaxe: Boolean get() = MaterialTags.PICKAXES.isTagged(this)
inline val Material.isSword: Boolean get() = MaterialTags.SWORDS.isTagged(this)
inline val Material.isAxe: Boolean get() = MaterialTags.AXES.isTagged(this)
inline val Material.isShovel: Boolean get() = MaterialTags.SHOVELS.isTagged(this)
inline val Material.isHoe: Boolean get() = MaterialTags.HOES.isTagged(this)
inline val Material.isOre: Boolean get() = MaterialTags.ORES.isTagged(this)
inline val Material.isLog: Boolean get() = Tag.LOGS.isTagged(this)
inline val Material.isIngot: Boolean get() = name.endsWith("INGOT")
inline val Material.isDoor: Boolean get() = MaterialTags.DOORS.isTagged(this)
inline val Material.isWater: Boolean get() = this == Material.WATER || this == Material.WATER_BUCKET
inline val Material.isLava: Boolean get() = this == Material.LAVA || this == Material.LAVA_BUCKET
inline val Material.isMinecraft: Boolean get() = name.endsWith("MINECART")

val Material.smeltedIngot: Material?
    get() = when (this) {
        Material.IRON_ORE -> Material.IRON_INGOT
        Material.GOLD_ORE -> Material.GOLD_INGOT
        else -> null
    }

val Material.smeltedBlock: Material?
    get() = when(this) {
        Material.STONE -> Material.STONE
        Material.COBBLESTONE -> Material.STONE
        else -> null
    }