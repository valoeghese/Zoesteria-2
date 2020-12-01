package tk.valoeghese.zoesteria.core;

import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tk.valoeghese.zoesteria.api.feature.FeatureSerialisers;
import tk.valoeghese.zoesteria.common.feature.BluffPineFeature;
import tk.valoeghese.zoesteria.common.feature.HeightChanceConfigHandler;
import tk.valoeghese.zoesteria.common.feature.TreeFeatureConfigHandler;
import tk.valoeghese.zoesteria.core.genmodifierpack.GenModifierPack;

public class ZoesteriaRegistryHandler {
	// =====  CORE ======

	@SubscribeEvent
	public static void onBiomeRegister(RegistryEvent.Register<Biome> event) {
		ZoesteriaMod.LOGGER.info("Loading biomes of GenModifierPacks");
		GenModifierPack.init();
		GenModifierPack.forEach(pack -> pack.loadBiomes(event.getRegistry()));

		while (!BIOME_PROCESSING.isEmpty()) {
			BIOME_PROCESSING.remove().run();
		}
	}

	// ===== COMMON / ZOESTERIA MODULE =====

	@SubscribeEvent
	public static void onFeatureRegister(RegistryEvent.Register<Feature<?>> event) {
		event.getRegistry().register(BLUFF_PINE.setRegistryName("bluff_pine"));
		registerFeatureSettings();
	}

	@SubscribeEvent
	public static void onPlacementRegister(RegistryEvent.Register<Placement<?>> event) {
		registerPlacementSettings();
	}

	public static void registerPlacementSettings() {
		if (!preventPlacementFire) {
			preventPlacementFire = true;

			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_CHANCE_HEIGHTMAP, HeightChanceConfigHandler.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_CHANCE_HEIGHTMAP_DOUBLE, HeightChanceConfigHandler.BASE);
		}
	}

	public static void registerFeatureSettings() {
		if (!preventFeatureFire) {
			preventFeatureFire = true;

			FeatureSerialisers.registerFeatureSettings(Feature.NORMAL_TREE, TreeFeatureConfigHandler.BASE);
			FeatureSerialisers.registerFeatureSettings(Feature.ACACIA_TREE, TreeFeatureConfigHandler.BASE);
			FeatureSerialisers.registerFeatureSettings(Feature.FANCY_TREE, TreeFeatureConfigHandler.BASE);
			FeatureSerialisers.registerFeatureSettings(BLUFF_PINE, TreeFeatureConfigHandler.BASE);
		}
	}

	public static final Feature<TreeFeatureConfig> BLUFF_PINE = new BluffPineFeature();
	public static final Queue<Runnable> BIOME_PROCESSING = new LinkedList<>();
	public static boolean preventFeatureFire = false;
	public static boolean preventPlacementFire = false;
}
