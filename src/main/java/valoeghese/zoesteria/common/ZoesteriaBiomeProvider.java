package valoeghese.zoesteria.common;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import terrablender.api.BiomeProvider;
import terrablender.worldgen.TBClimate;
import valoeghese.zoesteria.abstr.Bridge;

import java.util.function.Consumer;

public class ZoesteriaBiomeProvider extends BiomeProvider {
	public ZoesteriaBiomeProvider() {
		super(new ResourceLocation("zoesteria", "main_biomes"), 10);
	}

	@Override
	public void addOverworldBiomes(Registry<Biome> registry, Consumer<Pair<TBClimate.ParameterPoint, ResourceKey<Biome>>> mapper) {
		this.addModifiedVanillaOverworldBiomes(mapper, builder -> Bridge.getBridge().getBiomePlacements().forEach(builder::replaceBiome));
	}
}
