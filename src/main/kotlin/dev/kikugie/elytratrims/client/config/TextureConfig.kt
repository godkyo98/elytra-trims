package dev.kikugie.elytratrims.client.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class TextureConfig(
    val useBannerTextures: TextureOption,
    val cropTrims: TextureOption,
    val useDarkerTrim: TextureOption,
    val useElytraModel: TextureOption,
    val animationEasterEgg: TextureOption
) {
    constructor(
        useBannerTextures: Boolean,
        cropTrims: Boolean,
        useDarkerTrims: Boolean,
        useElytraModel: Boolean,
        animationEasterEgg: Boolean,
    ) : this(
        useBannerTextures.toOption(false, "useBannerTextures"),
        cropTrims.toOption(false, "cropTrims"),
        useDarkerTrims.toOption(false, "useDarkerTrim"),
        useElytraModel.toOption(true, "useElytraModel"),
        animationEasterEgg.toOption(true, "animationEasterEgg"),
    )

    companion object {
        fun default(): TextureConfig = TextureConfig(
            useBannerTextures = false,
            cropTrims = true,
            useDarkerTrims = false,
            useElytraModel = true,
            animationEasterEgg = true
        )

        val CODEC: Codec<TextureConfig> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.BOOL.fieldOf("useBannerTextures").forGetter { it.useBannerTextures.value },
                Codec.BOOL.fieldOf("cropTrims").forGetter { it.cropTrims.value },
                Codec.BOOL.fieldOf("useDarkerTrim").forGetter { it.useDarkerTrim.value },
                Codec.BOOL.fieldOf("useElytraModel").forGetter { it.useElytraModel.value },
                Codec.BOOL.fieldOf("animationEasterEgg").forGetter { it.animationEasterEgg.value },
            ).apply(instance, ::TextureConfig)
        }
    }
}