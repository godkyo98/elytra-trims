package dev.kikugie.elytratrims.common.recipe

import dev.kikugie.elytratrims.api.ElytraTrimsAPI
import dev.kikugie.elytratrims.common.access.FeatureAccess.addAnimationStatus
import dev.kikugie.elytratrims.common.access.FeatureAccess.removeColor
import dev.kikugie.elytratrims.common.access.FeatureAccess.removePatterns
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.Identifier

class ETAnimationRecipe(id: Identifier) : DelegatedRecipe(id, SAMPLE) {
    override fun matches(input: Stacks): Boolean {
        var elytra = 0
        var apple = 0
        var flesh = 0
        input.forEach {
            when {
                ElytraTrimsAPI.isProbablyElytra(it) -> elytra++
                it.item == Items.APPLE -> apple++
                it.item == Items.ROTTEN_FLESH -> flesh++
            }
            if (elytra > 1 || apple > 1 || flesh > 1) return false
        }
        return elytra == 1 && apple == 1 && flesh == 1
    }

    override fun craft(input: Stacks): ItemStack {
        val elytra = input.firstItemCopy(ElytraTrimsAPI::isProbablyElytra) ?: return ItemStack.EMPTY
        with(elytra) {
            addAnimationStatus()
            removePatterns()
            removeColor()
        }
        return elytra
    }

    override fun fits(width: Int, height: Int) = width * height >= 3

    companion object {
        val SAMPLE = ItemStack(Items.ELYTRA).apply { addAnimationStatus() }
    }
}