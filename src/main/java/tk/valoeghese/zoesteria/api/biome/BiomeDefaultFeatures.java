package tk.valoeghese.zoesteria.api.biome;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.DepthAverageConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;

public final class BiomeDefaultFeatures {
	private BiomeDefaultFeatures() {
	}

	public static void addStoneVariants(BiomeDecorations decorations) {
		decorations.addDecoration(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, DIRT, 33)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(10, 0, 0, 256))));
		decorations.addDecoration(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, GRAVEL, 33)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(8, 0, 0, 256))));
		decorations.addDecoration(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, GRANITE, 33)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(10, 0, 0, 80))));
		decorations.addDecoration(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, DIORITE, 33)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(10, 0, 0, 80))));
		decorations.addDecoration(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ANDESITE, 33)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(10, 0, 0, 80))));
	}

	public static void addOres(BiomeDecorations decorations) {
		decorations.addDecoration(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, COAL_ORE, 17)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(20, 0, 0, 128))));
		decorations.addDecoration(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, IRON_ORE, 9)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(20, 0, 0, 64))));
		decorations.addDecoration(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, GOLD_ORE, 9)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(2, 0, 0, 32))));
		decorations.addDecoration(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, REDSTONE_ORE, 8)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(8, 0, 0, 16))));
		decorations.addDecoration(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, DIAMOND_ORE, 8)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(1, 0, 0, 16))));
		decorations.addDecoration(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, LAPIS_ORE, 7)).withPlacement(Placement.COUNT_DEPTH_AVERAGE.configure(new DepthAverageConfig(1, 16, 16))));
	}

	public static void addGrass(BiomeDecorations decorations, int density) {
		decorations.addDecoration(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(GRASS_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(density))));
	}

	public static void addMushrooms(BiomeDecorations decorations, int densityWhite, int densityRed) {
		if (densityWhite > 0) {
			decorations.addDecoration(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(MUSHROOM_WHITE_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(densityWhite))));
		}
		if (densityRed > 0) {
			decorations.addDecoration(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(MUSHROOM_RED_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(densityRed))));
		}
	}

	public static void addWaterLakes(BiomeDecorations decorations, BlockState state, int chance) {
		decorations.addDecoration(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Feature.LAKE
				.withConfiguration(new BlockStateFeatureConfig(state))
				.withPlacement(Placement.WATER_LAKE.configure(new ChanceConfig(chance))));
	}

	public static void addLavaLakes(BiomeDecorations decorations, BlockState state, int chance) {
		decorations.addDecoration(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Feature.LAKE
				.withConfiguration(new BlockStateFeatureConfig(state))
				.withPlacement(Placement.LAVA_LAKE.configure(new ChanceConfig(chance))));
	}

	private static final BlockState DIRT = Blocks.DIRT.getDefaultState();
	private static final BlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
	private static final BlockState GRANITE = Blocks.GRANITE.getDefaultState();
	private static final BlockState DIORITE = Blocks.DIORITE.getDefaultState();
	private static final BlockState ANDESITE = Blocks.ANDESITE.getDefaultState();
	private static final BlockState COAL_ORE = Blocks.COAL_ORE.getDefaultState();
	private static final BlockState IRON_ORE = Blocks.IRON_ORE.getDefaultState();
	private static final BlockState GOLD_ORE = Blocks.GOLD_ORE.getDefaultState();
	private static final BlockState REDSTONE_ORE = Blocks.REDSTONE_ORE.getDefaultState();
	private static final BlockState DIAMOND_ORE = Blocks.DIAMOND_ORE.getDefaultState();
	private static final BlockState LAPIS_ORE = Blocks.LAPIS_ORE.getDefaultState();

	public static final BlockClusterFeatureConfig GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.GRASS.getDefaultState()), new SimpleBlockPlacer())).tries(32).build();
	public static final BlockClusterFeatureConfig MUSHROOM_WHITE_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.BROWN_MUSHROOM.getDefaultState()), new SimpleBlockPlacer())).tries(64).func_227317_b_().build();
	public static final BlockClusterFeatureConfig MUSHROOM_RED_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.RED_MUSHROOM.getDefaultState()), new SimpleBlockPlacer())).tries(64).func_227317_b_().build();
}
