package tk.valoeghese.zoesteria.api.biome;

import java.util.List;
import java.util.Optional;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;

public interface IZoesteriaBiome {
	String id();
	IBiomeProperties properties();
	void addPlacement(Object2IntMap<BiomeManager.BiomeType> biomePlacement);
	boolean canSpawnInBiome();
	BiomeDecorations getDecorations();
	List<BiomeDictionary.Type> biomeDictionaryTypes();

	default Optional<Integer> customSkyColour() {
		return Optional.empty();
	}

	default Optional<String> getRiverBiome() {
		return Optional.empty();
	}

	default Optional<List<String>> getHillsBiomes() {
		return Optional.empty();
	}
}
