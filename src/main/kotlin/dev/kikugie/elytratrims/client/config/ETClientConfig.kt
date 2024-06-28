package dev.kikugie.elytratrims.client.config

import dev.kikugie.elytratrims.client.ETClient
import dev.kikugie.elytratrims.common.config.ETConfigLoader
import dev.kikugie.elytratrims.platform.ModStatus
import kotlinx.serialization.Serializable

@Serializable
data class ETClientConfig(
    @JvmField val render: RenderConfig = RenderConfig(),
    @JvmField val texture: TextureConfig = TextureConfig(),
) {
    companion object {
        val file = ModStatus.configDir.resolve("elytra-trims.json")
        fun load() = ETConfigLoader.load(file, ::ETClientConfig)
        fun save() = ETConfigLoader.save(file, ETClient.config)
    }
}