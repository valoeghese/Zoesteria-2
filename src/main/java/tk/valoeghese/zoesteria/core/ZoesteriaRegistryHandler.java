package tk.valoeghese.zoesteria.core;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tk.valoeghese.zoesteria.api.feature.FeatureSerialisers;
import tk.valoeghese.zoesteria.common.feature.TreeFeatureConfigHandler;
import tk.valoeghese.zoesteria.core.genmodifierpack.GenModifierPack;

public class ZoesteriaRegistryHandler {
	// =====  CORE ======

	@SubscribeEvent
	public static void onBiomeRegister(RegistryEvent.Register<Biome> event) {
		ZoesteriaMod.LOGGER.info("Loading biomes of GenModifierPacks");
		GenModifierPack.init();
		GenModifierPack.forEach(pack -> pack.loadBiomes(event.getRegistry()));
		GenModifierPack.flagLoadedPackBiomes();
	}

	// ===== COMMON / ZOESTERIA MODULE =====

	@SubscribeEvent
	public static void onFeatureRegister(RegistryEvent.Register<Feature<?>> event) {
		FeatureSerialisers.registerFeatureSettings(Feature.NORMAL_TREE, TreeFeatureConfigHandler.BASE);
		FeatureSerialisers.registerFeatureSettings(Feature.ACACIA_TREE, TreeFeatureConfigHandler.BASE);
		FeatureSerialisers.registerFeatureSettings(Feature.FANCY_TREE, TreeFeatureConfigHandler.BASE);
	}

	@SubscribeEvent
	public static void onPlacementRegister(RegistryEvent.Register<Placement<?>> event) {

	}
}
