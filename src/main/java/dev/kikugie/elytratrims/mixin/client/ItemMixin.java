package dev.kikugie.elytratrims.mixin.client;

import dev.kikugie.elytratrims.api.ElytraTrimsAPI;
import dev.kikugie.elytratrims.client.ETClient;
import dev.kikugie.elytratrims.common.access.FeatureAccess;
import dev.kikugie.elytratrims.common.util.UtilKt;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.BannerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public class ItemMixin {
    @SuppressWarnings("InvalidInjectorMethodSignature")
    @Inject(method = "appendTooltip", at = @At("HEAD"))
    protected void elytratrims$modifyTooltip(
        //? if >=1.21 {
        /*ItemStack stack, Item.TooltipContext context, List<Text> tooltip, net.minecraft.item.tooltip.TooltipType type
        *///?} elif >=1.20.6 {
        /*ItemStack stack, Item.TooltipContext context, List<Text> tooltip, net.minecraft.client.item.TooltipType type
        *///?} else
        ItemStack stack, net.minecraft.world.World world, List<Text> tooltip, net.minecraft.client.item.TooltipContext context
        , CallbackInfo ci
    ) {
        if (!ElytraTrimsAPI.isProbablyElytra(stack)) return;
        if (FeatureAccess.INSTANCE.hasGlow(stack))
            tooltip.add(Text.translatable("elytratrims.item.glow"));
        BannerItem.appendBannerTooltip(stack, tooltip);
        if (ETClient.INSTANCE.getConfig().texture.useElytraModel && Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("elytratrims.item.model1"));
            tooltip.add(Text.translatable("elytratrims.item.model2"));
        }
    }
}