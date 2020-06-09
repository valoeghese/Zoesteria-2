package tk.valoeghese.zoesteria.api.biome;

import java.util.List;
import java.util.Optional;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraftforge.common.BiomeManager;

public interface IZoesteriaBiome {
	String id();
	IBiomeProperties properties();
	void addPlacement(Object2IntMap<BiomeManager.BiomeType> biomePlacement);
	Optional<Integer> customSkyColour();
	Optional<String> getRiver();
	boolean canSpawnInBiome();
	List<ConfiguredFeature<?, ?>> getFeatures();
}
