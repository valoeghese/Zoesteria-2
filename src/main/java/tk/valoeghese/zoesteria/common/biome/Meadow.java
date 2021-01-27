package tk.valoeghese.zoesteria.common.biome;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.ImmutableList;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager.BiomeType;
import tk.valoeghese.zoesteria.api.biome.BiomeDecorations;
import tk.valoeghese.zoesteria.api.biome.BiomeDefaultFeatures;
import tk.valoeghese.zoesteria.api.biome.IBiome;
import tk.valoeghese.zoesteria.api.biome.IBiomeProperties;
import tk.valoeghese.zoesteria.api.biome.SpawnEntry;
import tk.valoeghese.zoesteria.common.ZoesteriaCommonEventHandler;
import tk.valoeghese.zoesteria.common.placement.LinePlacement;

public class Meadow implements IBiome {
	@Override
	public String id() {
		return "meadow";
	}

	@Override
	public IBiomeProperties properties() {
		return IBiomeProperties.builder(Biome.Category.PLAINS)
				.depth(0.3f)
				.temperature(0.55f)
				.downfall(0.8f)
				.scale(-0.1f)
				.build();
	}

	@Override
	public void addPlacement(Object2IntMap<BiomeType> biomePlacement) {
		// a less common biome
		biomePlacement.put(BiomeType.COOL, 5);
		biomePlacement.put(BiomeType.WARM, 5);
	}

	@Override
	public boolean canSpawnInBiome() {
		return true;
	}

	@Override
	public BiomeDecorations getDecorations() {
		BiomeDecorations decorations = BiomeDecorations.create()
				.addDecoration(Decoration.VEGETAL_DECORATION, Feature.NORMAL_TREE
						.withConfiguration(DefaultBiomeFeatures.OAK_TREE_CONFIG)
						.withPlacement(ZoesteriaCommonEventHandler.LINE_PLACEMENT.configure(new FrequencyConfig(9))))
				.addDecoration(Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(
						new BlockClusterFeatureConfig.Builder(new WeightedBlockStateProvider()
								.addWeightedBlockstate(Blocks.POPPY.getDefaultState(), 10)
								.addWeightedBlockstate(Blocks.OXEYE_DAISY.getDefaultState(), 10)
								.addWeightedBlockstate(Blocks.DANDELION.getDefaultState(), 10)
								.addWeightedBlockstate(Blocks.WHITE_TULIP.getDefaultState(), 5)
								.addWeightedBlockstate(Blocks.RED_TULIP.getDefaultState(), 5)
								.addWeightedBlockstate(Blocks.CORNFLOWER.getDefaultState(), 3)
								.addWeightedBlockstate(Blocks.BLUE_ORCHID.getDefaultState(), 1)
								.addWeightedBlockstate(Blocks.ALLIUM.getDefaultState(), 1),
								new SimpleBlockPlacer()).tries(64).build())
						.withPlacement(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(25))));

		BiomeDefaultFeatures.addGrass(decorations, 25);
		BiomeDefaultFeatures.addWaterLakes(decorations, Blocks.WATER.getDefaultState(), 12);
		BiomeDefaultFeatures.addLavaLakes(decorations, Blocks.LAVA.getDefaultState(), 95);
		BiomeDefaultFeatures.addOres(decorations);
		BiomeDefaultFeatures.addSedimentDisks(decorations);
		BiomeDefaultFeatures.addStoneVariants(decorations);
		BiomeDefaultFeatures.addMushrooms(decorations, 1, 1);

		return decorations;
	}

	@Override
	public List<Type> biomeTypes() {
		return ImmutableList.of(
				Type.OVERWORLD,
				Type.PLAINS,
				Type.LUSH,
				Type.SPARSE);
	}

	@Override
	public List<SpawnEntry> mobSpawns() {
		return ImmutableList.of(
				new SpawnEntry(EntityType.RABBIT).spawnWeight(16).spawnGroupCount(4, 4),
				new SpawnEntry(EntityType.SHEEP).spawnWeight(10).spawnGroupCount(4, 7),
				new SpawnEntry(EntityType.COW).spawnWeight(8).spawnGroupCount(4, 4),
				new SpawnEntry(EntityType.CHICKEN).spawnWeight(5).spawnGroupCount(4, 4),
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

	@Override
	public Optional<List<String>> getHillsBiomes() {
		return Optional.of(ImmutableList.of("zoesteria:meadow", "zoesteria:low_woodlands"));
	}

	@Override
	public Optional<Integer> customGrassColour() {
		return Optional.of(0x72E574);
	}
}
