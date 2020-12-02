package tk.valoeghese.zoesteria.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import tk.valoeghese.zoesteria.api.IZFGSerialisable;
import tk.valoeghese.zoesteria.api.IZoesteriaJavaModule;
import tk.valoeghese.zoesteria.api.feature.FeatureSerialisers;
import tk.valoeghese.zoesteria.api.surface.ISurfaceBuilderTemplate;
import tk.valoeghese.zoesteria.common.feature.BluffPineFeature;
import tk.valoeghese.zoesteria.common.feature.HeightChanceConfigHandler;
import tk.valoeghese.zoesteria.common.feature.TreeFeatureConfigHandler;
import tk.valoeghese.zoesteria.core.genmodifierpack.GenModifierPack;
import tk.valoeghese.zoesteriaconfig.api.ZoesteriaConfig;
import tk.valoeghese.zoesteriaconfig.api.container.WritableConfig;

public class ZoesteriaRegistryHandler {
	// ===== CORE ONLY ======

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
			module.createSurfaceBuilderTemplates().forEach(template -> TEMPLATE_LOOKUP.put(new ResourceLocation(module.packId(), template.id()), template));
		}

		Map<ISurfaceBuilderTemplate<?>, ResourceLocation> templateIdLookup = TEMPLATE_LOOKUP.inverse();

		for (IZoesteriaJavaModule module : MODULES) {
			if (!GenModifierPack.isLoaded(module.packId())) {
				module.createSurfaceBuilders().forEach(surfaceBuilder -> {
					WritableConfig config = ZoesteriaConfig.createWritableConfig(new LinkedHashMap<>());
					config.putStringValue("id", surfaceBuilder.id);
					config.putStringValue("template", templateIdLookup.get(surfaceBuilder.template).toString());

					// convert to data
					List<? extends IZFGSerialisable> stepsJava = surfaceBuilder.steps;
					List<Object> stepsData = new ArrayList<>();

					for (IZFGSerialisable serialisable : stepsJava) {
						// add data
						stepsData.add(serialisable.toZoesteriaConfig().asMap());
					}

					config.putList("steps", stepsData);
				});
			}
		}

		ZoesteriaMod.LOGGER.info("Loading surface builders of GenModifierPacks");
		GenModifierPack.init();
		GenModifierPack.forEach(pack -> pack.loadSurfaces(event.getRegistry(), TEMPLATE_LOOKUP::get));
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

	public static final Feature<TreeFeatureConfig> BLUFF_PINE = new BluffPineFeature();
	public static final Queue<Consumer<IForgeRegistry<Biome>>> BIOME_PROCESSING = new LinkedList<>();
	private static final BiMap<ResourceLocation, ISurfaceBuilderTemplate<?>> TEMPLATE_LOOKUP = HashBiMap.create();
	private static final List<IZoesteriaJavaModule> MODULES = new ArrayList<>();
	public static boolean preventFeatureFire = false;
	public static boolean preventPlacementFire = false;
}
