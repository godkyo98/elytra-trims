package dev.kikugie.elytratrims.common.recipe

import dev.kikugie.elytratrims.common.ETReference
import dev.kikugie.elytratrims.common.access.FeatureAccess.addGlow
import dev.kikugie.elytratrims.common.util.isProbablyElytra
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.util.Identifier

class ETGlowRecipe(id: Identifier, category: CraftingRecipeCategory) : DelegatedRecipe(id, category) {
    override fun matches(inventory: Inventory): Boolean {
        var item = 0
        var sac = 0
        for (slot in 0 until inventory.size()) {
            val stack = inventory.getStack(slot)
            if (isProbablyElytra(stack.item)) item++
            else if (stack.item === Items.GLOW_INK_SAC) sac++
            else if (!stack.isEmpty) return false
            if (item > 1 || sac > 1) return false
        }
        return item == 1 && sac == 1
    }

    override fun craft(inventory: Inventory): ItemStack {
        val elytra = inventory.firstItem(::isProbablyElytra)?.copy() ?: return ItemStack.EMPTY
        elytra.addGlow()
        return elytra
    }

    override fun fits(width: Int, height: Int): Boolean = width * height >= 2

    override fun getSerializer() = SERIALIZER

    companion object {
        val SERIALIZER: RecipeSerializer<ETGlowRecipe> = serializer(ETReference.id("crafting_special_elytraglow"), ::ETGlowRecipe)
    }
}