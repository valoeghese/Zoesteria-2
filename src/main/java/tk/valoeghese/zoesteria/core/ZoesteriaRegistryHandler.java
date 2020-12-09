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
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import tk.valoeghese.zoesteria.api.IZoesteriaJavaModule;
import tk.valoeghese.zoesteria.api.feature.FeatureSerialisers;
import tk.valoeghese.zoesteria.api.surface.ISurfaceBuilderTemplate;
import tk.valoeghese.zoesteria.core.pack.GenModifierPack;
import tk.valoeghese.zoesteria.core.serialisers.feature.BlockClusterFeatureConfigHandler;
import tk.valoeghese.zoesteria.core.serialisers.feature.NoFeatureConfigHandler;
import tk.valoeghese.zoesteria.core.serialisers.feature.OreFeatureConfigHandler;
import tk.valoeghese.zoesteria.core.serialisers.feature.TreeFeatureConfigHandler;
import tk.valoeghese.zoesteria.core.serialisers.foliage.BlobFoliagePlacerSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.placement.ChanceConfigHandler;
import tk.valoeghese.zoesteria.core.serialisers.placement.CountExtraChanceConfigHandler;
import tk.valoeghese.zoesteria.core.serialisers.placement.CountRangeConfigHandler;
import tk.valoeghese.zoesteria.core.serialisers.placement.DepthAverageConfigHandler;
import tk.valoeghese.zoesteria.core.serialisers.placement.FrequencyConfigHandler;
import tk.valoeghese.zoesteria.core.serialisers.placement.HeightChanceConfigHandler;

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

			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_EXTRA_HEIGHTMAP, CountExtraChanceConfigHandler.BASE);

			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_RANGE, CountRangeConfigHandler.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_BIASED_RANGE, CountRangeConfigHandler.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_VERY_BIASED_RANGE, CountRangeConfigHandler.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.RANDOM_COUNT_RANGE, CountRangeConfigHandler.BASE);

			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_DEPTH_AVERAGE, DepthAverageConfigHandler.BASE);

			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_HEIGHTMAP, FrequencyConfigHandler.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_TOP_SOLID, FrequencyConfigHandler.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_HEIGHTMAP_32, FrequencyConfigHandler.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_HEIGHTMAP_DOUBLE, FrequencyConfigHandler.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_HEIGHT_64, FrequencyConfigHandler.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.FOREST_ROCK, FrequencyConfigHandler.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.HELL_FIRE, FrequencyConfigHandler.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.MAGMA, FrequencyConfigHandler.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.LIGHT_GEM_CHANCE, FrequencyConfigHandler.BASE);

			FeatureSerialisers.registerPlacementSettings(Placement.CHANCE_HEIGHTMAP, ChanceConfigHandler.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.CHANCE_HEIGHTMAP_DOUBLE, ChanceConfigHandler.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.CHANCE_PASSTHROUGH, ChanceConfigHandler.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.CHANCE_TOP_SOLID_HEIGHTMAP, ChanceConfigHandler.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.LAVA_LAKE, ChanceConfigHandler.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.WATER_LAKE, ChanceConfigHandler.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.DUNGEONS, ChanceConfigHandler.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.ICEBERG, ChanceConfigHandler.BASE);
		}

		for (IZoesteriaJavaModule module : MODULES) {
			module.registerPlacementSerialisers();
		}
	}

	public static void registerFeatureSettings() {
		if (!preventFeatureFire) {
			preventFeatureFire = true;

			registerFoliageSerialisers();

			FeatureSerialisers.registerFeatureSettings(Feature.NORMAL_TREE, TreeFeatureConfigHandler.BASE);
			FeatureSerialisers.registerFeatureSettings(Feature.ACACIA_TREE, TreeFeatureConfigHandler.BASE);
			FeatureSerialisers.registerFeatureSettings(Feature.FANCY_TREE, TreeFeatureConfigHandler.BASE);

			FeatureSerialisers.registerFeatureSettings(Feature.ORE, OreFeatureConfigHandler.BASE);

			FeatureSerialisers.registerFeatureSettings(Feature.FLOWER, BlockClusterFeatureConfigHandler.BASE);
			FeatureSerialisers.registerFeatureSettings(Feature.RANDOM_PATCH, BlockClusterFeatureConfigHandler.BASE);

			FeatureSerialisers.registerFeatureSettings(Feature.PILLAGER_OUTPOST, NoFeatureConfigHandler.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.WOODLAND_MANSION, NoFeatureConfigHandler.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.JUNGLE_TEMPLE, NoFeatureConfigHandler.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.DESERT_PYRAMID, NoFeatureConfigHandler.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.IGLOO, NoFeatureConfigHandler.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.STRONGHOLD, NoFeatureConfigHandler.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.OCEAN_MONUMENT, NoFeatureConfigHandler.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.NETHER_BRIDGE, NoFeatureConfigHandler.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.END_CITY, NoFeatureConfigHandler.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.NO_OP, NoFeatureConfigHandler.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.CHORUS_PLANT, NoFeatureConfigHandler.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.VOID_START_PLATFORM, NoFeatureConfigHandler.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.DESERT_WELL, NoFeatureConfigHandler.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.FOSSIL, NoFeatureConfigHandler.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.ICE_SPIKE, NoFeatureConfigHandler.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.GLOWSTONE_BLOB, NoFeatureConfigHandler.INSTANCE);

			// @Reason FREEZE_TOP_LAYER is automatic and mandatory - why wouldn't you use it?
			// I might make it configured if I do nether biomes / end biomes
			//FeatureSerialisers.registerFeatureSettings(Feature.FREEZE_TOP_LAYER, NoFeatureConfigHandler.INSTANCE);

			FeatureSerialisers.registerFeatureSettings(Feature.VINES, NoFeatureConfigHandler.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.MONSTER_ROOM, NoFeatureConfigHandler.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.BLUE_ICE, NoFeatureConfigHandler.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.END_ISLAND, NoFeatureConfigHandler.INSTANCE);

			FeatureSerialisers.registerFeatureSettings(Feature.KELP, NoFeatureConfigHandler.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.CORAL_TREE, NoFeatureConfigHandler.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.CORAL_MUSHROOM, NoFeatureConfigHandler.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.CORAL_CLAW, NoFeatureConfigHandler.INSTANCE);

			for (IZoesteriaJavaModule module : MODULES) {
				module.registerFeatureSerialisers();
			}
		}
	}

	/**
	 * Called at the beginning of {@link #registerFeatureSettings()}
	 */
	private static void registerFoliageSerialisers() {
		FeatureSerialisers.registerFoliagePlacer(new ResourceLocation("blob_foliage_placer"), BlobFoliagePlacer.class, BlobFoliagePlacerSerialiser.BASE);

		for (IZoesteriaJavaModule module : MODULES) {
			module.registerFoliageSerialisers();
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
