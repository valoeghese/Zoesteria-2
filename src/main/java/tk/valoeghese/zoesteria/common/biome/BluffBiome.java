package tk.valoeghese.zoesteria.common.biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraftforge.common.BiomeManager.BiomeType;
import tk.valoeghese.zoesteria.api.biome.IBiomeProperties;
import tk.valoeghese.zoesteria.api.biome.IZoesteriaBiome;

public final class BluffBiome implements IZoesteriaBiome {
	@Override
	public String id() {
		return "bluff";
	}

	@Override
	public IBiomeProperties properties() {
		return IBiomeProperties.builder(Biome.Category.EXTREME_HILLS)
				.depth(0.9F)
				.scale(1.7F)
				.temperature(0.3F)
				.downfall(0.6F)
				.fillerBlock("minecraft:stone")
				.build();
	}

	@Override
	public void addPlacement(Object2IntMap<BiomeType> biomePlacement) {
		biomePlacement.put(BiomeType.COOL, 20);
	}

	@Override
	public Optional<Integer> customSkyColour() {
		return Optional.empty();
	}

	@Override
	public Optional<String> getRiver() {
		return Optional.of("zoesteria:bluff");
	}

	@Override
	public boolean canSpawnInBiome() {
		return true;
	}

	@Override
	public List<ConfiguredFeature<?, ?>> getFeatures() {
		List<ConfiguredFeature<?, ?>> result = new ArrayList<>();
		return result;
	}
}
