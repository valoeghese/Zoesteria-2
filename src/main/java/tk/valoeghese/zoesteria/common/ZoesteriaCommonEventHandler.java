package tk.valoeghese.zoesteria.common;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tk.valoeghese.zoesteria.common.feature.BluffPineFeature;
import tk.valoeghese.zoesteria.common.feature.BluffRuinsFeature;

/**
 * Event registry handler for common stuff.
 */
public class ZoesteriaCommonEventHandler {
	@SubscribeEvent
	public static void onFeatureRegister(RegistryEvent.Register<Feature<?>> event) {
		event.getRegistry().register(BLUFF_PINE.setRegistryName("bluff_pine"));
		event.getRegistry().register(BLUFF_PINE_SAPLING.setRegistryName("bluff_pine_sapling"));
		event.getRegistry().register(BLUFF_RUINS.setRegistryName("bluff_ruins"));
	}

	@SubscribeEvent
	public static void onFoliagePlacerRegister(RegistryEvent.Register<FoliagePlacerType<?>> event) {
		event.getRegistry().register(NONE_FOLIAGE);
	}

	public static final Feature<TreeFeatureConfig> BLUFF_PINE = new BluffPineFeature(true);
	public static final Feature<TreeFeatureConfig> BLUFF_PINE_SAPLING = new BluffPineFeature(false);
	public static final Feature<NoFeatureConfig> BLUFF_RUINS = new BluffRuinsFeature();
	public static final FoliagePlacerType<?> NONE_FOLIAGE = new FoliagePlacerType<>(NoneFoliagePlacer::new).setRegistryName("none");
}
