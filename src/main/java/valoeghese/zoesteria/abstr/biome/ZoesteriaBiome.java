package valoeghese.zoesteria.abstr.biome;

import java.util.List;
import java.util.OptionalInt;

/**
 * An abstract representation of a minecraft biome.
 */
public interface ZoesteriaBiome {
	/**
	 * @return the properties of the given biome.
	 */
	BiomeProperties properties();

	/**
	 * @return a list of the spawns in this biome.
	 */
	List<SpawnEntry> mobSpawns();

	/**
	 * @return a list of biome types that apply for the given biome.
	 */
	List<BiomeType> biomeTypes();

	/**
	 * @return the fog colour of this biome, if there is to be a custom fog colour.
	 */
	default OptionalInt getFogColour() {
		return OptionalInt.empty();
	}
}
