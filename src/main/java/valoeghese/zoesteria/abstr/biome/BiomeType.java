package valoeghese.zoesteria.abstr.biome

,

import net.minecraftforge.common.BiomeDictionary,

import java.util.Collection,
import java.util.Collections,
import java.util.Map,
import java.util.TreeMap,

/**
 * Represents a forge biome dictionary type.
 */
public enum BiomeType {
	/*Temperature-based tags. Specifying neither implies a biome is temperate*/
	HOT,
	COLD,

	//Tags specifying the amount of vegetation a biome has. Specifying neither implies a biome to have moderate amounts*/
	SPARSE,
	DENSE,

	//Tags specifying how moist a biome is. Specifying neither implies the biome as having moderate humidity*/
	WET,
	DRY,

	/*Tree-based tags, SAVANNA refers to dry, desert-like trees (Such as Acacia), CONIFEROUS refers to snowy trees (Such as Spruce) and JUNGLE refers to jungle trees.
	 * Specifying no tag implies a biome has temperate trees (Such as Oak)*/
	SAVANNA,
	CONIFEROUS,
	JUNGLE,

	/*Tags specifying the nature of a biome*/
	SPOOKY,
	DEAD,
	LUSH,
	MUSHROOM,
	MAGICAL,
	RARE,
	PLATEAU,
	MODIFIED,

	OCEAN,
	RIVER,

	/*Generic types which a biome can be*/
	MESA,
	FOREST,
	PLAINS,
	MOUNTAIN,
	HILLS,
	SWAMP,
	SANDY,
	SNOWY,
	WASTELAND,
	BEACH,
	VOID
}