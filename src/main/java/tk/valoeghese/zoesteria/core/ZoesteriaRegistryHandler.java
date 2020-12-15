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
import net.minecraft.world.gen.foliageplacer.AcaciaFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.PineFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.SpruceFoliagePlacer;
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
import tk.valoeghese.zoesteria.core.serialisers.feature.BlockStateFeatureConfigSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.feature.NoFeatureConfigSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.feature.OreFeatureConfigSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.feature.SphereReplaceConfigSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.feature.TreeFeatureConfigSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.feature.VillageConfigSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.foliage.AcaciaFoliagePlacerSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.foliage.BlobFoliagePlacerSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.foliage.PineFoliagePlacerSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.foliage.SpruceFoliagePlacerSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.placement.ChanceConfigSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.placement.CountExtraChanceConfigSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.placement.CountRangeConfigSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.placement.DepthAverageConfigHandler;
import tk.valoeghese.zoesteria.core.serialisers.placement.FrequencyConfigSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.placement.HeightChanceConfigSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.placement.TopSolidWithNoiseConfigSerialiser;

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

			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_CHANCE_HEIGHTMAP, HeightChanceConfigSerialiser.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_CHANCE_HEIGHTMAP_DOUBLE, HeightChanceConfigSerialiser.BASE);

			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_EXTRA_HEIGHTMAP, CountExtraChanceConfigSerialiser.BASE);

			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_RANGE, CountRangeConfigSerialiser.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_BIASED_RANGE, CountRangeConfigSerialiser.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_VERY_BIASED_RANGE, CountRangeConfigSerialiser.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.RANDOM_COUNT_RANGE, CountRangeConfigSerialiser.BASE);

			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_DEPTH_AVERAGE, DepthAverageConfigHandler.BASE);

			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_HEIGHTMAP, FrequencyConfigSerialiser.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_TOP_SOLID, FrequencyConfigSerialiser.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_HEIGHTMAP_32, FrequencyConfigSerialiser.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_HEIGHTMAP_DOUBLE, FrequencyConfigSerialiser.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.COUNT_HEIGHT_64, FrequencyConfigSerialiser.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.FOREST_ROCK, FrequencyConfigSerialiser.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.HELL_FIRE, FrequencyConfigSerialiser.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.MAGMA, FrequencyConfigSerialiser.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.LIGHT_GEM_CHANCE, FrequencyConfigSerialiser.BASE);

			FeatureSerialisers.registerPlacementSettings(Placement.CHANCE_HEIGHTMAP, ChanceConfigSerialiser.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.CHANCE_HEIGHTMAP_DOUBLE, ChanceConfigSerialiser.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.CHANCE_PASSTHROUGH, ChanceConfigSerialiser.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.CHANCE_TOP_SOLID_HEIGHTMAP, ChanceConfigSerialiser.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.LAVA_LAKE, ChanceConfigSerialiser.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.WATER_LAKE, ChanceConfigSerialiser.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.DUNGEONS, ChanceConfigSerialiser.BASE);
			FeatureSerialisers.registerPlacementSettings(Placement.ICEBERG, ChanceConfigSerialiser.BASE);

			FeatureSerialisers.registerPlacementSettings(Placement.TOP_SOLID_HEIGHTMAP_NOISE_BIASED, TopSolidWithNoiseConfigSerialiser.BASE);
		}

		for (IZoesteriaJavaModule module : MODULES) {
			module.registerPlacementSerialisers();
		}
	}

	public static void registerFeatureSettings() {
		if (!preventFeatureFire) {
			preventFeatureFire = true;

			registerFoliageSerialisers();

			FeatureSerialisers.registerFeatureSettings(Feature.NORMAL_TREE, TreeFeatureConfigSerialiser.BASE);
			FeatureSerialisers.registerFeatureSettings(Feature.ACACIA_TREE, TreeFeatureConfigSerialiser.BASE);
			FeatureSerialisers.registerFeatureSettings(Feature.FANCY_TREE, TreeFeatureConfigSerialiser.BASE);

			FeatureSerialisers.registerFeatureSettings(Feature.ORE, OreFeatureConfigSerialiser.BASE);

			FeatureSerialisers.registerFeatureSettings(Feature.FLOWER, BlockClusterFeatureConfigHandler.BASE);
			FeatureSerialisers.registerFeatureSettings(Feature.RANDOM_PATCH, BlockClusterFeatureConfigHandler.BASE);

			FeatureSerialisers.registerFeatureSettings(Feature.ICEBERG, BlockStateFeatureConfigSerialiser.BASE);
			FeatureSerialisers.registerFeatureSettings(Feature.LAKE, BlockStateFeatureConfigSerialiser.BASE);

			FeatureSerialisers.registerFeatureSettings(Feature.PILLAGER_OUTPOST, NoFeatureConfigSerialiser.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.WOODLAND_MANSION, NoFeatureConfigSerialiser.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.JUNGLE_TEMPLE, NoFeatureConfigSerialiser.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.DESERT_PYRAMID, NoFeatureConfigSerialiser.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.IGLOO, NoFeatureConfigSerialiser.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.STRONGHOLD, NoFeatureConfigSerialiser.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.OCEAN_MONUMENT, NoFeatureConfigSerialiser.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.NETHER_BRIDGE, NoFeatureConfigSerialiser.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.END_CITY, NoFeatureConfigSerialiser.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.NO_OP, NoFeatureConfigSerialiser.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.CHORUS_PLANT, NoFeatureConfigSerialiser.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.VOID_START_PLATFORM, NoFeatureConfigSerialiser.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.DESERT_WELL, NoFeatureConfigSerialiser.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.FOSSIL, NoFeatureConfigSerialiser.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.ICE_SPIKE, NoFeatureConfigSerialiser.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.GLOWSTONE_BLOB, NoFeatureConfigSerialiser.INSTANCE);

			// @Reason FREEZE_TOP_LAYER is automatic and mandatory - why wouldn't you use it?
			// I might make it configured if I do nether biomes / end biomes
			//FeatureSerialisers.registerFeatureSettings(Feature.FREEZE_TOP_LAYER, NoFeatureConfigHandler.INSTANCE);

			FeatureSerialisers.registerFeatureSettings(Feature.VINES, NoFeatureConfigSerialiser.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.MONSTER_ROOM, NoFeatureConfigSerialiser.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.BLUE_ICE, NoFeatureConfigSerialiser.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.END_ISLAND, NoFeatureConfigSerialiser.INSTANCE);

			FeatureSerialisers.registerFeatureSettings(Feature.KELP, NoFeatureConfigSerialiser.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.CORAL_TREE, NoFeatureConfigSerialiser.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.CORAL_MUSHROOM, NoFeatureConfigSerialiser.INSTANCE);
			FeatureSerialisers.registerFeatureSettings(Feature.CORAL_CLAW, NoFeatureConfigSerialiser.INSTANCE);

			FeatureSerialisers.registerFeatureSettings(Feature.DISK, SphereReplaceConfigSerialiser.BASE);

			FeatureSerialisers.registerFeatureSettings(Feature.VILLAGE, VillageConfigSerialiser.BASE);

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
		FeatureSerialisers.registerFoliagePlacer(new ResourceLocation("spruce_foliage_placer"), SpruceFoliagePlacer.class, SpruceFoliagePlacerSerialiser.BASE);
		FeatureSerialisers.registerFoliagePlacer(new ResourceLocation("pine_foliage_placer"), PineFoliagePlacer.class, PineFoliagePlacerSerialiser.BASE);
		FeatureSerialisers.registerFoliagePlacer(new ResourceLocation("acacia_foliage_placer"), AcaciaFoliagePlacer.class, AcaciaFoliagePlacerSerialiser.BASE);

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
