package tk.valoeghese.zoesteria.common;

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
import tk.valoeghese.zoesteria.common.feature.TripleSelectorFeature;

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
		registry.register(TRIPLE_SELECTOR.setRegistryName("triple_selector"));
	}

	@SubscribeEvent
	public static void onFoliagePlacerRegister(RegistryEvent.Register<FoliagePlacerType<?>> event) {
		event.getRegistry().register(NONE_FOLIAGE);
	}

	public static final Feature<TreeFeatureConfig> BLUFF_PINE = new BluffPineFeature(true);
	public static final Feature<TreeFeatureConfig> BLUFF_PINE_SAPLING = new BluffPineFeature(false);
	public static final Feature<NoFeatureConfig> BLUFF_RUINS = new BluffRuinsFeature();
	public static final Feature<TreeLikeFeatureConfig> FALLEN_LOG = new FallenLogFeature();
	public static final Feature<TripleFeatureConfig> TRIPLE_SELECTOR = new TripleSelectorFeature();
	public static final Feature<TreeFeatureConfig> LOLLIPOP_TREE = new LollipopFeature();
	public static final FoliagePlacerType<?> NONE_FOLIAGE = new FoliagePlacerType<>(NoneFoliagePlacer::new).setRegistryName("none");
}
