package dev.kikugie.elytratrims.common.access

import dev.kikugie.elytratrims.common.compat.StackedArmorTrimsCompat
import dev.kikugie.elytratrims.common.util.toArgb
import net.minecraft.item.ItemStack
import net.minecraft.item.trim.ArmorTrim
import net.minecraft.registry.DynamicRegistryManager

//? if <=1.20.4 {
import net.minecraft.block.entity.BannerBlockEntity
import net.minecraft.item.BannerItem
import net.minecraft.item.BlockItem
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.DyeColor

object FeatureAccess : IFeatureAccess {
    private val DYEABLE = object : net.minecraft.item.DyeableItem {}

    override fun ItemStack.getPatterns(): List<BannerLayer> {
        val nbt = BannerBlockEntity.getPatternListNbt(this) ?: return emptyList()
        return BannerBlockEntity.getPatternsFromNbt(DyeColor.WHITE, nbt).drop(1).map {
            BannerLayer(it.first, it.second)
        }
    }

    override fun ItemStack.getBaseColor(): Int = (item as? BannerItem)?.color?.toArgb() ?: 0

    override fun ItemStack.setPatterns(source: ItemStack) {
        val nbt = BannerBlockEntity.getPatternListNbt(source) ?: return
        val container = BlockItem.getBlockEntityNbt(this) ?: NbtCompound()
        if (container.isEmpty) setSubNbt("BlockEntityTag", container)
        container.put("Patterns", nbt.copy())
    }

    override fun ItemStack.removePatterns() {
        val container = BlockItem.getBlockEntityNbt(this) ?: return
        container.remove("Patterns")
        if (container.isEmpty) removeSubNbt("BlockEntityTag")
    }

    override fun ItemStack.getColor() = if (DYEABLE.hasColor(this)) DYEABLE.getColor(this) else 0

    override fun ItemStack.setColor(color: Int) {
        DYEABLE.setColor(this, color)
    }

    override fun ItemStack.removeColor() {
        DYEABLE.removeColor(this)
    }

    override fun ItemStack.hasGlow(): Boolean {
        val glow = nbt?.getBoolean("glow")
        return glow ?: (getSubNbt("display")?.getBoolean("glow") ?: false)
    }

    override fun ItemStack.addGlow() {
        orCreateNbt.putBoolean("glow", true)
    }

    override fun ItemStack.removeGlow() {
        nbt?.remove("glow")
        val nbt = getSubNbt("display") ?: return
        nbt.remove("glow")
        if (nbt.isEmpty) removeSubNbt("display")
    }

    override fun ItemStack.getAnimationStatus() = nbt?.getBoolean("bad_apple") ?: false
    override fun ItemStack.addAnimationStatus() {
        orCreateNbt.putBoolean("bad_apple", true)
    }

    override fun ItemStack.removeAnimationStatus() {
        nbt?.remove("bad_apple")
    }
}
//?} else {
/*import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.DyedColorComponent
import net.minecraft.component.type.NbtComponent
import net.minecraft.item.BannerItem

object FeatureAccess : IFeatureAccess {
    override fun ItemStack.getPatterns() = get(DataComponentTypes.BANNER_PATTERNS)?.layers?.map {
        BannerLayer(it.pattern, it.color)
    } ?: emptyList()

    override fun ItemStack.getBaseColor(): Int = (item as? BannerItem)?.color?.toArgb() ?: 0

    override fun ItemStack.setPatterns(source: ItemStack) {
        applyComponentsFrom(source.components.filtered { it == DataComponentTypes.BANNER_PATTERNS })
    }

    override fun ItemStack.removePatterns() {
        remove(DataComponentTypes.BANNER_PATTERNS)
    }

    override fun ItemStack.getColor(): Int = DyedColorComponent.getColor(this, 0)

    override fun ItemStack.setColor(color: Int) {
        set(DataComponentTypes.DYED_COLOR, DyedColorComponent(color, true))
    }

    override fun ItemStack.removeColor() {
        remove(DataComponentTypes.DYED_COLOR)
    }

    // Why copy nbt if we don't modify it
    override fun ItemStack.hasGlow(): Boolean = get(DataComponentTypes.CUSTOM_DATA)?.nbt?.getBoolean("glow") ?: false

    override fun ItemStack.addGlow() {
        val data = (get(DataComponentTypes.CUSTOM_DATA) ?: NbtComponent.DEFAULT).copyNbt()
        data.putBoolean("glow", true)
        NbtComponent.set(DataComponentTypes.CUSTOM_DATA, this, data)
    }

    override fun ItemStack.removeGlow() {
        val data = get(DataComponentTypes.CUSTOM_DATA)?.copyNbt() ?: return
        data.remove("glow")
        NbtComponent.set(DataComponentTypes.CUSTOM_DATA, this, data)
    }

    override fun ItemStack.getAnimationStatus(): Boolean = get(DataComponentTypes.CUSTOM_DATA)?.nbt?.getBoolean("animation") ?: false

    override fun ItemStack.addAnimationStatus() {
        val data = (get(DataComponentTypes.CUSTOM_DATA) ?: NbtComponent.DEFAULT).copyNbt()
        data.putBoolean("animation", true)
        NbtComponent.set(DataComponentTypes.CUSTOM_DATA, this, data)
    }

    override fun ItemStack.removeAnimationStatus() {
        val data = get(DataComponentTypes.CUSTOM_DATA)?.copyNbt() ?: return
        data.remove("animation")
        NbtComponent.set(DataComponentTypes.CUSTOM_DATA, this, data)
    }
}
*///?}

private interface IFeatureAccess {
    fun ItemStack.getTrims(manager: DynamicRegistryManager): List<ArmorTrim> = StackedArmorTrimsCompat.getTrimList(manager, this)

    fun ItemStack.getPatterns(): List<BannerLayer>
    fun ItemStack.getBaseColor(): Int
    fun ItemStack.setPatterns(source: ItemStack)
    fun ItemStack.removePatterns()

    fun ItemStack.getColor(): Int
    fun ItemStack.setColor(color: Int)
    fun ItemStack.removeColor()

    fun ItemStack.hasGlow(): Boolean
    fun ItemStack.addGlow()
    fun ItemStack.removeGlow()

    fun ItemStack.getAnimationStatus(): Boolean
    fun ItemStack.addAnimationStatus()
    fun ItemStack.removeAnimationStatus()
}