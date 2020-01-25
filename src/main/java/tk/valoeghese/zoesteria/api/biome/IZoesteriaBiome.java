package tk.valoeghese.zoesteria.api.biome;

import java.util.Optional;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraftforge.common.BiomeManager;

public interface IZoesteriaBiome {
	String id();
	IBiomeProperties properties();
	void addPlacement(Object2IntMap<BiomeManager.BiomeType> biomePlacement);
	Optional<Integer> customSkyColour();
	Optional<String> getRiver();
	boolean canSpawnInBiome();
}
