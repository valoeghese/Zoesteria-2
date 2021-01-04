package tk.valoeghese.zoesteria.common.biome;

import java.util.ArrayList;
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
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockBlobConfig;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidWithNoiseConfig;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager.BiomeType;
import tk.valoeghese.zoesteria.api.biome.BiomeDecorations;
import tk.valoeghese.zoesteria.api.biome.BiomeDefaultFeatures;
import tk.valoeghese.zoesteria.api.biome.IBiome;
import tk.valoeghese.zoesteria.api.biome.IBiomeProperties;
import tk.valoeghese.zoesteria.api.biome.SpawnEntry;
import tk.valoeghese.zoesteria.common.ZoesteriaCommonEventHandler;
import tk.valoeghese.zoesteria.common.objects.ZoesteriaBlocks;

public class Pampas implements IBiome {
	public Pampas(String id, Type type) {
		this.id = id;
		this.type = type;
	}

	private final String id;
	private final Type type;

	@Override
	public String id() {
		return this.id;
	}

	@Override
	public IBiomeProperties properties() {
		IBiomeProperties.Builder builder = IBiomeProperties.builder(Category.PLAINS)
				.depth(this.type.baseHeight)
				.scale(this.type.scale)
				.temperature(0.7f)
				.downfall(0.3f);

		if (this.type == Type.HILLS) {
			builder.surfaceBuilder("zoesteria:pampas_hills");
		}

		return builder.build();
	}

	@Override
	public boolean canSpawnInBiome() {
		return true;
	}

	@Override
	public void addPlacement(Object2IntMap<BiomeType> biomePlacement) {
		if (this.type == Type.NORMAL) {
			biomePlacement.put(BiomeType.COOL, 90);
			biomePlacement.put(BiomeType.WARM, 90);
		}
	}

	@Override
	public Optional<List<String>> getHillsBiomes() {
		return this.type != Type.NORMAL ? Optional.empty() : Optional.of(ImmutableList.of("zoesteria:pampas_hills", "zoesteria:pampas_flats"));
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
				.addDecoration(Decoration.VEGETAL_DECORATION, ZoesteriaCommonEventHandler.CONFIGURED_HICKORY
						.withPlacement(ZoesteriaCommonEventHandler.LINE_PLACEMENT.configure(new FrequencyConfig(3))))
				.addDecoration(Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(
						new BlockClusterFeatureConfig.Builder(
								new SimpleBlockStateProvider(ZoesteriaBlocks.PAMPAS_GRASS.get().getDefaultState()),
								new DoublePlantBlockPlacer()).xSpread(3).zSpread(3).tries(32).build()
						)
						.withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(8))));

		BiomeDefaultFeatures.addGrass(decorations, 40);
		BiomeDefaultFeatures.addWaterLakes(decorations, Blocks.WATER.getDefaultState(), 50);
		BiomeDefaultFeatures.addLavaLakes(decorations, Blocks.LAVA.getDefaultState(), 95);
		BiomeDefaultFeatures.addOres(decorations);
		BiomeDefaultFeatures.addSedimentDisks(decorations);
		BiomeDefaultFeatures.addStoneVariants(decorations);
		BiomeDefaultFeatures.addMushrooms(decorations, 2, 1);

		decorations.addStructure(Feature.PILLAGER_OUTPOST.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
		decorations.addStructure(Feature.VILLAGE.withConfiguration(new VillageConfig("village/plains/town_centers", 4)));

		return decorations;
	}

	@Override
	public List<BiomeDictionary.Type> biomeTypes() {
		List<BiomeDictionary.Type> result = new ArrayList<>();
		result.add(BiomeDictionary.Type.OVERWORLD);
		result.add(BiomeDictionary.Type.PLAINS);

		if (this.type == Type.FLATS) {
			result.add(BiomeDictionary.Type.SPARSE);
		} else if (this.type == Type.HILLS) {
			result.add(BiomeDictionary.Type.HILLS);
		}

		return result;
	}

	@Override
	public List<SpawnEntry> mobSpawns() {
		return ImmutableList.of(
				new SpawnEntry(EntityType.COW).spawnWeight(14).spawnGroupCount(3, 5),
				new SpawnEntry(EntityType.SHEEP).spawnWeight(14).spawnGroupCount(3, 5),
				new SpawnEntry(EntityType.RABBIT).spawnWeight(8).spawnGroupCount(2, 4),
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

	public enum Type {
		FLATS(0.15f, -0.1f),
		NORMAL(0.4f, 0.1f),
		HILLS(1.45f, 0.14f);

		private Type(float baseHeight, float scale) {
			this.baseHeight = baseHeight;
			this.scale = scale;
		}

		private final float baseHeight;
		private final float scale;
	}
}
