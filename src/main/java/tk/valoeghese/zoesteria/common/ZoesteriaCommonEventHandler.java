package tk.valoeghese.zoesteria.common;

import net.minecraft.block.Blocks;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import tk.valoeghese.zoesteria.common.feature.BluffPineFeature;
import tk.valoeghese.zoesteria.common.feature.BluffRuinsFeature;
import tk.valoeghese.zoesteria.common.feature.FallenLogFeature;
import tk.valoeghese.zoesteria.common.feature.LollipopFeature;
import tk.valoeghese.zoesteria.common.feature.TreeLikeFeatureConfig;
import tk.valoeghese.zoesteria.common.feature.TripleFeatureConfig;
import tk.valoeghese.zoesteria.common.feature.TripleNoiseSelectorFeature;
import tk.valoeghese.zoesteria.common.objects.ZoesteriaBlocks;

/**
 * Event registry handler for common stuff.
 */
public class ZoesteriaCommonEventHandler {
	@SubscribeEvent
	public static void onFeatureRegister(RegistryEvent.Register<Feature<?>> event) {
		IForgeRegistry<Feature<?>> registry = event.getRegistry();

		registry.register(BLUFF_PINE.setRegistryName("bluff_pine"));
		registry.register(BLUFF_PINE_SAPLING.setRegistryName("bluff_pine_sapling"));
		registry.register(BLUFF_RUINS.setRegistryName("bluff_ruins"));
		registry.register(FALLEN_LOG.setRegistryName("fallen_log"));
		registry.register(LOLLIPOP_TREE.setRegistryName("lollipop_tree"));
		registry.register(TRIPLE_NOISE_SELECTOR.setRegistryName("triple_noise_selector"));
	}

	@SubscribeEvent
	public static void onFoliagePlacerRegister(RegistryEvent.Register<FoliagePlacerType<?>> event) {
		event.getRegistry().register(NONE_FOLIAGE);
	}

	// Features
	public static final Feature<TreeFeatureConfig> BLUFF_PINE = new BluffPineFeature(true);
	public static final Feature<TreeFeatureConfig> BLUFF_PINE_SAPLING = new BluffPineFeature(false);
	public static final Feature<NoFeatureConfig> BLUFF_RUINS = new BluffRuinsFeature();
	public static final Feature<TreeLikeFeatureConfig> FALLEN_LOG = new FallenLogFeature();
	public static final Feature<TripleFeatureConfig> TRIPLE_NOISE_SELECTOR = new TripleNoiseSelectorFeature();
	public static final Feature<TreeFeatureConfig> LOLLIPOP_TREE = new LollipopFeature();
	public static final FoliagePlacerType<?> NONE_FOLIAGE = new FoliagePlacerType<>(NoneFoliagePlacer::new).setRegistryName("none");

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
					new SimpleBlockPlacer()).tries(32).build()
			);
	public static final ConfiguredFeature<?, ?> BERRY_BUSH = Feature.RANDOM_PATCH.withConfiguration(
			new BlockClusterFeatureConfig.Builder(
					new SimpleBlockStateProvider(ZoesteriaBlocks.SMALL_BERRY_BUSH.get().getDefaultState()),
					new SimpleBlockPlacer()).tries(32).build()
			);
}
