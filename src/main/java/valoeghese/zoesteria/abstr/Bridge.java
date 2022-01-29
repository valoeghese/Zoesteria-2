package valoeghese.zoesteria.abstr;

import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MobSpawnSettings;
import valoeghese.zoesteria.abstr.biome.BiomeProperties;
import valoeghese.zoesteria.abstr.biome.SpawnEntry;
import valoeghese.zoesteria.abstr.biome.ZoesteriaBiome;

import java.util.Map;

/**
 * For the implementation to touch.
 */
public abstract class Bridge implements Proxy {
	abstract public Map<Climate.ParameterPoint, ResourceKey<Biome>> getBiomePlacements();

	static Bridge instance;

	public static void setBridge(Bridge bridge) {
		Bridge.instance = bridge;
	}

	public static Bridge getBridge() {
		return instance;
	}

	// TODO yeah this is in a random place but I should make spinifex only generate 1 block above the lowest non-water level. And make it quite dense thickets there (more so 2 blocks above +)

	// TODO features, via an addAdditionalFeatures method
	protected static Biome build(ZoesteriaBiome biome) {
		Biome.BiomeBuilder builder = new Biome.BiomeBuilder();

		// properties
		BiomeProperties properties = biome.properties();

		builder.biomeCategory(properties.category)
				.precipitation(properties.precipitation)
				.temperature(properties.temperature)
				.downfall(properties.rainfall);

		// spawns
		MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();

		for (SpawnEntry entry : biome.mobSpawns()) {
			spawns.addSpawn(entry.getEntityType().getCategory(), new MobSpawnSettings.SpawnerData(entry.getEntityType(), entry.getSpawnWeight(), entry.getMinGroupCount(), entry.getMaxGroupCount()));
		}
		builder.mobSpawnSettings(spawns.build());

		// special effects
		builder.specialEffects(new BiomeSpecialEffects.Builder()
				.fogColor(biome.getFogColour().orElse(OVERWORLD_FOG_COLOR))
				.waterColor(NORMAL_WATER_COLOR)
				.waterFogColor(NORMAL_WATER_FOG_COLOR)
				.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
				.backgroundMusic(null)
				.build()
		);

		// features
		BiomeGenerationSettings.Builder generationSettings = new BiomeGenerationSettings.Builder();
		BiomeDefaultFeatures.addSurfaceFreezing(generationSettings);
		BiomeDefaultFeatures.addDefaultOres(generationSettings);
		BiomeDefaultFeatures.addDefaultOres(generationSettings);

		builder.generationSettings(generationSettings.build());

		// and they all lived happily ever after; the end
		return builder.build();
	}

	// from OverworldBiomes.java
	private static final int NORMAL_WATER_COLOR = 4159204;
	private static final int NORMAL_WATER_FOG_COLOR = 329011;
	private static final int OVERWORLD_FOG_COLOR = 12638463;
}
