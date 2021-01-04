package tk.valoeghese.zoesteria.common.biome;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.ImmutableList;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.blockplacer.DoublePlantBlockPlacer;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockBlobConfig;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidWithNoiseConfig;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager.BiomeType;
import tk.valoeghese.zoesteria.api.biome.BiomeDecorations;
import tk.valoeghese.zoesteria.api.biome.BiomeDefaultFeatures;
import tk.valoeghese.zoesteria.api.biome.IBiome;
import tk.valoeghese.zoesteria.api.biome.IBiomeProperties;
import tk.valoeghese.zoesteria.api.biome.SpawnEntry;
import tk.valoeghese.zoesteria.common.ZoesteriaCommonEventHandler;
import tk.valoeghese.zoesteria.common.objects.ZoesteriaBlocks;

public class Prairie implements IBiome {
	public Prairie(String id, float baseHeight) {
		this.id = id;
		this.baseHeight = baseHeight;
	}

	private final String id;
	private final float baseHeight;

	@Override
	public String id() {
		return this.id;
	}

	@Override
	public IBiomeProperties properties() {
		return IBiomeProperties.builder(Category.PLAINS)
				.depth(this.baseHeight)
				.scale(-0.01f)
				.temperature(0.52f)
				.downfall(0.6f)
				.entitySpawnChance(0.12f)
				.build();
	}

	@Override
	public Optional<Integer> customGrassColour() {
		return Optional.of(0xffdc51);
	}

	@Override
	public void addPlacement(Object2IntMap<BiomeType> biomePlacement) {
		if (this.baseHeight < 0.4f) {
			biomePlacement.put(BiomeType.WARM, 10); // a common biome in warm areas.
			biomePlacement.put(BiomeType.COOL, 4); // rarer in cool areas, but exists
		}
	}

	@Override
	public boolean canSpawnInBiome() {
		return true;
	}

	@Override
	public Optional<List<String>> getHillsBiomes() {
		return this.baseHeight < 0.4f ? Optional.of(ImmutableList.of("zoesteria:prairie_rise")) : Optional.empty();
	}

	@Override
	public BiomeDecorations getDecorations() {
		BiomeDecorations decorations = BiomeDecorations.create()
				.addDecoration(Decoration.LOCAL_MODIFICATIONS, Feature.FOREST_ROCK
						.withConfiguration(new BlockBlobConfig(Blocks.STONE.getDefaultState(), 1))
						.withPlacement(Placement.TOP_SOLID_HEIGHTMAP_NOISE_BIASED.configure(new TopSolidWithNoiseConfig(
								9, // amplitude
								32.0, // stretch of coordinates (pos / val)
								-0.5, // offset of raw noise
								Heightmap.Type.OCEAN_FLOOR_WG))))
				.addDecoration(Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(
						new BlockClusterFeatureConfig.Builder(
								new SimpleBlockStateProvider(ZoesteriaBlocks.PRAIRIE_GRASS.get().getDefaultState()),
								new SimpleBlockPlacer()).tries(32).build()
						)
						.withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(28))))
				.addDecoration(Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(
						new BlockClusterFeatureConfig.Builder(
								new SimpleBlockStateProvider(ZoesteriaBlocks.PRAIRIE_GRASS_TALL.get().getDefaultState()),
								new DoublePlantBlockPlacer()).tries(32).build()
						)
						.withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(10))))
				.addDecoration(Decoration.VEGETAL_DECORATION, ZoesteriaCommonEventHandler.CONFIGURED_HICKORY
						.withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(0, 0.0325f, 9))));

		BiomeDefaultFeatures.addWaterLakes(decorations, Blocks.WATER.getDefaultState(), 50);
		BiomeDefaultFeatures.addLavaLakes(decorations, Blocks.LAVA.getDefaultState(), 95);
		BiomeDefaultFeatures.addOres(decorations);
		BiomeDefaultFeatures.addSedimentDisks(decorations);
		BiomeDefaultFeatures.addStoneVariants(decorations);
		BiomeDefaultFeatures.addMushrooms(decorations, 2, 1);

		decorations.addStructure(Feature.PILLAGER_OUTPOST.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
		decorations.addStructure(Feature.VILLAGE.withConfiguration(new VillageConfig("village/plains/town_centers", 7)));

		return decorations;
	}

	@Override
	public List<Type> biomeTypes() {
		return ImmutableList.of(
				Type.OVERWORLD,
				Type.PLAINS,
				Type.SPARSE);
	}

	@Override
	public List<SpawnEntry> mobSpawns() {
		return ImmutableList.of(
				new SpawnEntry(EntityType.COW).spawnWeight(16).spawnGroupCount(4, 7),
				new SpawnEntry(EntityType.RABBIT).spawnWeight(10).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.SHEEP).spawnWeight(8).spawnGroupCount(2, 5),
				new SpawnEntry(EntityType.CHICKEN).spawnWeight(5).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.PIG).spawnWeight(2).spawnGroupCount(2, 3),

				new SpawnEntry(EntityType.BAT).spawnWeight(10).spawnGroupCount(8, 8),

				new SpawnEntry(EntityType.SPIDER).spawnWeight(100).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.ZOMBIE).spawnWeight(95).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.ZOMBIE_VILLAGER).spawnWeight(5).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.SKELETON).spawnWeight(100).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.CREEPER).spawnWeight(100).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.SLIME).spawnWeight(100).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.ENDERMAN).spawnWeight(10).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.WITCH).spawnWeight(5).spawnGroupCount(2, 4));
	}
}
