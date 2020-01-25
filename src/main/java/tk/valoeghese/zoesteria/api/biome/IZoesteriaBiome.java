package tk.valoeghese.zoesteria.api.biome;

import java.util.Optional;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraftforge.common.BiomeManager;

public interface IZoesteriaBiome {
	String id();
	<T extends IBiomeProperties> T properties();
	Object2IntMap<BiomeManager.BiomeType> placement();
	Optional<Integer> customSkyColour();
	Optional<String> getRiver();
	boolean canSpawnInBiome();
}
