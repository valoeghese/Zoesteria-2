package valoeghese.zoesteria.common;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import valoeghese.zoesteria.common.feature.BluffPineFeature;
import valoeghese.zoesteria.common.feature.BluffRuinsFeature;
import valoeghese.zoesteria.common.feature.FallenLogFeature;
import valoeghese.zoesteria.common.feature.LollipopFeature;
import valoeghese.zoesteria.common.feature.ShrubFeatureConfig;
import valoeghese.zoesteria.common.feature.SimpleShrubFeature;
import valoeghese.zoesteria.common.feature.TreeLikeFeatureConfig;
import valoeghese.zoesteria.common.feature.TripleFeatureConfig;
import valoeghese.zoesteria.common.feature.TripleNoiseSelectorFeature;
import valoeghese.zoesteria.common.objects.ZoesteriaBlocks;
import valoeghese.zoesteria.common.placement.LinePlacement;
import valoeghese.zoesteria.common.placement.LinePlacementConfig;
import valoeghese.zoesteria.common.surface.FillToSeaLevelSurfaceBuilder;

/**
 * Event registry handler for common stuff.
 */
public class ZoesteriaFeatures {
	@SubscribeEvent
	public static void onPlacementRegister(RegistryEvent.Register<Placement<?>> event) {
		IForgeRegistry<Placement<?>> registry = event.getRegistry();

		registry.register(LINE_PLACEMENT.setRegistryName("line"));
	}

	@SubscribeEvent
	public static void onFeatureRegister(RegistryEvent.Register<Feature<?>> event) {
		IForgeRegistry<Feature<?>> registry = event.getRegistry();

		registry.register(BLUFF_PINE.setRegistryName("bluff_pine"));
		registry.register(BLUFF_PINE_SAPLING.setRegistryName("bluff_pine_sapling"));
		registry.register(BLUFF_RUINS.setRegistryName("bluff_ruins"));
		registry.register(FALLEN_LOG.setRegistryName("fallen_log"));
		registry.register(LOLLIPOP_TREE.setRegistryName("lollipop_tree"));
		registry.register(TRIPLE_NOISE_SELECTOR.setRegistryName("triple_noise_selector"));
		registry.register(SIMPLE_SHRUB.setRegistryName("simple_shrub"));
	}

	@SubscribeEvent
	public static void onSurfaceBuilderRegister(RegistryEvent.Register<SurfaceBuilder<?>> event) {
		event.getRegistry().register(FILL_TO_SEA_LEVEL.setRegistryName("fill_to_sea_level"));
	}

	@SubscribeEvent
	public static void onFoliagePlacerRegister(RegistryEvent.Register<FoliagePlacerType<?>> event) {
		event.getRegistry().register(NONE_FOLIAGE.setRegistryName("none"));
	}

	// Features
	public static final Feature<TreeConfiguration> BLUFF_PINE = new BluffPineFeature(true);
	public static final Feature<TreeConfiguration> BLUFF_PINE_SAPLING = new BluffPineFeature(false);
	public static final Feature<NoneFeatureConfiguration> BLUFF_RUINS = new BluffRuinsFeature();
	public static final Feature<TreeLikeFeatureConfig> FALLEN_LOG = new FallenLogFeature();
	public static final Feature<TripleFeatureConfig> TRIPLE_NOISE_SELECTOR = new TripleNoiseSelectorFeature();
	public static final Feature<TreeConfiguration> LOLLIPOP_TREE = new LollipopFeature();
	public static final Feature<ShrubFeatureConfig> SIMPLE_SHRUB = new SimpleShrubFeature();

	public static final FoliagePlacerType<?> NONE_FOLIAGE = new FoliagePlacerType<>(NoneFoliagePlacer::new);

	// Configured Features
	public static final ConfiguredFeature<TreeFeatureConfig, ?> CONFIGURED_ASPEN = LOLLIPOP_TREE
			.withConfiguration(new TreeFeatureConfig.Builder(
					new SimpleBlockStateProvider(Blocks.BIRCH_LOG.getDefaultState()),
					new SimpleBlockStateProvider(ZoesteriaBlocks.ASPEN_LEAVES.get().getDefaultState()),
					new NoneFoliagePlacer())
					.baseHeight(6)
					.heightRandA(3)
					.heightRandB(2)
					.trunkHeight(1)
					.trunkHeightRandom(1)
					.trunkTopOffset(1)
					.trunkTopOffsetRandom(0)
					.build()
					);

	public static final ConfiguredFeature<TreeFeatureConfig, ?> CONFIGURED_HICKORY = LOLLIPOP_TREE
			.withConfiguration(new TreeFeatureConfig.Builder(
					new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState()),
					new SimpleBlockStateProvider(Blocks.OAK_LEAVES.getDefaultState()),
					new NoneFoliagePlacer())
					.baseHeight(5)
					.heightRandA(2)
					.heightRandB(2)
					.trunkHeight(1)
					.trunkHeightRandom(1)
					.trunkTopOffset(1)
					.trunkTopOffsetRandom(0)
					.build()
					);

	public static final ConfiguredFeature<TreeFeatureConfig, ?> CONFIGURED_BLUFF_PINE_SAPLING = BLUFF_PINE_SAPLING
			.withConfiguration(new TreeFeatureConfig.Builder(
					new SimpleBlockStateProvider(
							Blocks.SPRUCE_LOG.getDefaultState()),
					new SimpleBlockStateProvider(ZoesteriaBlocks.BLUFF_PINE_LEAVES.get().getDefaultState()),
					new NoneFoliagePlacer()) // unused
					.baseHeight(11)
					.heightRandA(4)
					.heightRandB(5)
					.trunkTopOffset(1)
					.trunkTopOffsetRandom(1)
					.trunkHeight(1)
					.trunkHeightRandom(1)
					.build());

	public static final ConfiguredFeature<?, ?> SIMPLE_BUSH = Feature.RANDOM_PATCH.withConfiguration(
			new BlockClusterFeatureConfig.Builder(
					new SimpleBlockStateProvider(ZoesteriaBlocks.SMALL_BUSH.get().getDefaultState()),
					new SimpleBlockPlacer()).tries(8).build()
			);

	public static final ConfiguredFeature<?, ?> BERRY_BUSH = Feature.RANDOM_PATCH.withConfiguration(
			new BlockClusterFeatureConfig.Builder(
					new SimpleBlockStateProvider(ZoesteriaBlocks.SMALL_BERRY_BUSH.get().getDefaultState()),
					new SimpleBlockPlacer()).tries(8).build()
			);

	public static final ConfiguredFeature<?, ?> CONFIGURED_CACTLET = Feature.RANDOM_PATCH.withConfiguration(
			new BlockClusterFeatureConfig.Builder(
					new WeightedBlockStateProvider()
					.addWeightedBlockstate(ZoesteriaBlocks.SMALL_CACTLET.get().getDefaultState(), 2)
					.addWeightedBlockstate(ZoesteriaBlocks.CACTLET.get().getDefaultState(), 1),
					new SimpleBlockPlacer()).tries(16).build()
			);

	public static final ConfiguredFeature<?, ?> CONFIGURED_LARGE_CACTLET = Feature.RANDOM_PATCH.withConfiguration(
			new BlockClusterFeatureConfig.Builder(
					new SimpleBlockStateProvider(ZoesteriaBlocks.LARGE_CACTLET.get().getDefaultState()),
					new DoublePlantBlockPlacer()
					).tries(8).build());

	public static final ConfiguredFeature<?, ?> CONFIGURED_TOADSTOOL = Feature.RANDOM_PATCH.withConfiguration(
			new BlockClusterFeatureConfig.Builder(
					new WeightedBlockStateProvider()
					.addWeightedBlockstate(ZoesteriaBlocks.TOADSTOOL.get().getDefaultState(), 3)
					.addWeightedBlockstate(ZoesteriaBlocks.TOADSTOOLS.get().getDefaultState(), 1),
					new SimpleBlockPlacer()).xSpread(4).zSpread(4).tries(16).build()
			);

	public static final ConfiguredFeature<?, ?> CONFIGURED_TOETOE = Feature.RANDOM_PATCH.withConfiguration(
			new BlockClusterFeatureConfig.Builder(
					new SimpleBlockStateProvider(ZoesteriaBlocks.PAMPAS_GRASS.get().getDefaultState()),
					new DoublePlantBlockPlacer()).xSpread(3).zSpread(3).tries(16).build()
			);

	private static final SurfaceBuilder<SurfaceBuilderConfig> FILL_TO_SEA_LEVEL = new FillToSeaLevelSurfaceBuilder();

	public static final Placement<LinePlacementConfig> LINE_PLACEMENT = new LinePlacement();
}
