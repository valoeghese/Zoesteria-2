package tk.valoeghese.zoesteria.common;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tk.valoeghese.zoesteria.common.feature.BluffPineFeature;

/**
 * Event registry handler for common stuff.
 */
public class ZoesteriaCommonEventHandler {
	@SubscribeEvent
	public static void onFeatureRegister(RegistryEvent.Register<Feature<?>> event) {
		event.getRegistry().register(BLUFF_PINE.setRegistryName("bluff_pine"));
		event.getRegistry().register(BLUFF_PINE_SAPLING.setRegistryName("bluff_pine_sapling"));
	}

	public static final Feature<TreeFeatureConfig> BLUFF_PINE = new BluffPineFeature(true);
	public static final Feature<TreeFeatureConfig> BLUFF_PINE_SAPLING = new BluffPineFeature(false);
}
