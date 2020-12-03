package tk.valoeghese.zoesteria.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import tk.valoeghese.zoesteria.api.IZoesteriaJavaModule;
import tk.valoeghese.zoesteria.api.feature.FeatureSerialisers;
import tk.valoeghese.zoesteria.api.surface.ISurfaceBuilderTemplate;
import tk.valoeghese.zoesteria.core.genmodifierpack.GenModifierPack;
import tk.valoeghese.zoesteria.core.serialisers.CountRangeConfigHandler;
import tk.valoeghese.zoesteria.core.serialisers.DepthAverageConfigHandler;
import tk.valoeghese.zoesteria.core.serialisers.HeightChanceConfigHandler;
import tk.valoeghese.zoesteria.core.serialisers.OreFeatureConfigHandler;
import tk.valoeghese.zoesteria.core.serialisers.TreeFeatureConfigHandler;

/**
 * Event registry handler for core stuff.
 */
public class ZoesteriaRegistryHandler {
	@SubscribeEvent
	public static void onBiomeRegister(RegistryEvent.Register<Biome> event) {
		registerFeatureSettings();
		registerPlacementSettings();

		ZoesteriaMod.LOGGER.info("Loading biomes of GenModifierPacks");
		GenModifierPack.init();
		GenModifierPack.forEach(pack -> pack.loadBiomes(event.getRegistry()));

		while (!BIOME_PROCESSING.isEmpty()) {
			BIOME_PROCESSING.remove().accept(event.getRegistry());
		}
	}

	@SubscribeEvent
	public static void onSurfaceBuilderRegister(RegistryEvent.Register<SurfaceBuilder<?>> event) {
		for (IZoesteriaJavaModule module : MODULES) {
			String packId = module.packId();
			module.createSurfaceBuilderTemplates().forEach(template -> TEMPLATE_LOOKUP.put(new ResourceLocation(packId, template.id()), template));
		}

		Map<ISurfaceBuilderTemplate<?>, ResourceLocation> templateIdLookup = TEMPLATE_LOOKUP.inverse();

		while (!SURFACE_PROCESSING.isEmpty()) {
			SURFACE_PROCESSING.remove().accept(event.getRegistry(), templateIdLookup::get);
		}

		ZoesteriaMod.LOGGER.info("Loading surface builders of GenModifierPacks");
		GenModifierPack.init();
		GenModifierPack.forEach(pack -> pack.loadSurfaces(event.getRegistry(), TEMPLATE_LOOKUP::get));
	}

	@SubscribeEvent
	public static void onFeatureRegister(RegistryEvent.Register<Feature<?>> event) {
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

			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_RANGE, CountRangeConfigHandler.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_BIASED_RANGE, CountRangeConfigHandler.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_VERY_BIASED_RANGE, CountRangeConfigHandler.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.RANDOM_COUNT_RANGE, CountRangeConfigHandler.BASE);

			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_DEPTH_AVERAGE, DepthAverageConfigHandler.BASE);
		}

		for (IZoesteriaJavaModule module : MODULES) {
			module.registerPlacementSettings();
		}
	}

	public static void registerFeatureSettings() {
		if (!preventFeatureFire) {
			preventFeatureFire = true;

			FeatureSerialisers.registerFeatureSettings(Feature.NORMAL_TREE, TreeFeatureConfigHandler.BASE);
			FeatureSerialisers.registerFeatureSettings(Feature.ACACIA_TREE, TreeFeatureConfigHandler.BASE);
			FeatureSerialisers.registerFeatureSettings(Feature.FANCY_TREE, TreeFeatureConfigHandler.BASE);
			FeatureSerialisers.registerFeatureSettings(Feature.ORE, OreFeatureConfigHandler.BASE);

			for (IZoesteriaJavaModule module : MODULES) {
				module.registerFeatureSettings();
			}
		}
	}

	public static void addJavaModule(IZoesteriaJavaModule module) {
		synchronized (MODULES) {
			MODULES.add(module);
		}
	}

	public static final Queue<BiConsumer<IForgeRegistry<SurfaceBuilder<?>>, Function<ISurfaceBuilderTemplate<?>, ResourceLocation>>> SURFACE_PROCESSING = new LinkedList<>();
	public static final Queue<Consumer<IForgeRegistry<Biome>>> BIOME_PROCESSING = new LinkedList<>();
	private static final BiMap<ResourceLocation, ISurfaceBuilderTemplate<?>> TEMPLATE_LOOKUP = HashBiMap.create();
	private static final List<IZoesteriaJavaModule> MODULES = new ArrayList<>();
	public static boolean preventFeatureFire = false;
	public static boolean preventPlacementFire = false;
}
