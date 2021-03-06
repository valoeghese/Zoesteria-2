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
import net.minecraft.world.gen.treedecorator.BeehiveTreeDecorator;
import net.minecraft.world.gen.treedecorator.CocoaTreeDecorator;
import net.minecraft.world.gen.treedecorator.LeaveVineTreeDecorator;
import net.minecraft.world.gen.treedecorator.TrunkVineTreeDecorator;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import tk.valoeghese.zoesteria.api.IZoesteriaJavaModule;
import tk.valoeghese.zoesteria.api.ZoesteriaSerialisers;
import tk.valoeghese.zoesteria.api.surface.ISurfaceBuilderTemplate;
import tk.valoeghese.zoesteria.core.pack.GenModifierPack;
import tk.valoeghese.zoesteria.core.serialisers.feature.BlockBlobConfigSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.feature.BlockClusterConfigSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.feature.BlockStateFeatureConfigSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.feature.NoFeatureConfigSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.feature.OreFeatureConfigSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.feature.RandomSelectorConfigSerialiser;
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
import tk.valoeghese.zoesteria.core.serialisers.treedecorator.ProbabilityTreeDecoratorSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.treedecorator.SimpleTreeDecoratorSerialiser;

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
		GenModifierPack.loadInitialBiomes(event.getRegistry());
		GenModifierPack.forEach(pack -> pack.loadRemainingBiomes(event.getRegistry()));

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

		Map<ISurfaceBuilderTemplate<?, ?>, ResourceLocation> templateIdLookup = TEMPLATE_LOOKUP.inverse();

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

			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.COUNT_CHANCE_HEIGHTMAP, HeightChanceConfigSerialiser.BASE);
			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.COUNT_CHANCE_HEIGHTMAP_DOUBLE, HeightChanceConfigSerialiser.BASE);

			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.COUNT_EXTRA_HEIGHTMAP, CountExtraChanceConfigSerialiser.BASE);

			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.COUNT_RANGE, CountRangeConfigSerialiser.BASE);
			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.COUNT_BIASED_RANGE, CountRangeConfigSerialiser.BASE);
			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.COUNT_VERY_BIASED_RANGE, CountRangeConfigSerialiser.BASE);
			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.RANDOM_COUNT_RANGE, CountRangeConfigSerialiser.BASE);

			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.COUNT_DEPTH_AVERAGE, DepthAverageConfigHandler.BASE);

			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.COUNT_HEIGHTMAP, FrequencyConfigSerialiser.BASE);
			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.COUNT_TOP_SOLID, FrequencyConfigSerialiser.BASE);
			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.COUNT_HEIGHTMAP_32, FrequencyConfigSerialiser.BASE);
			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.COUNT_HEIGHTMAP_DOUBLE, FrequencyConfigSerialiser.BASE);
			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.COUNT_HEIGHT_64, FrequencyConfigSerialiser.BASE);
			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.FOREST_ROCK, FrequencyConfigSerialiser.BASE);
			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.HELL_FIRE, FrequencyConfigSerialiser.BASE);
			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.MAGMA, FrequencyConfigSerialiser.BASE);
			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.LIGHT_GEM_CHANCE, FrequencyConfigSerialiser.BASE);

			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.CHANCE_HEIGHTMAP, ChanceConfigSerialiser.BASE);
			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.CHANCE_HEIGHTMAP_DOUBLE, ChanceConfigSerialiser.BASE);
			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.CHANCE_PASSTHROUGH, ChanceConfigSerialiser.BASE);
			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.CHANCE_TOP_SOLID_HEIGHTMAP, ChanceConfigSerialiser.BASE);
			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.LAVA_LAKE, ChanceConfigSerialiser.BASE);
			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.WATER_LAKE, ChanceConfigSerialiser.BASE);
			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.DUNGEONS, ChanceConfigSerialiser.BASE);
			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.ICEBERG, ChanceConfigSerialiser.BASE);

			ZoesteriaSerialisers.registerPlacementSerialiser(Placement.TOP_SOLID_HEIGHTMAP_NOISE_BIASED, TopSolidWithNoiseConfigSerialiser.BASE);
		}

		for (IZoesteriaJavaModule module : MODULES) {
			module.registerPlacementSerialisers();
		}
	}

	public static void registerFeatureSettings() {
		if (!preventFeatureFire) {
			preventFeatureFire = true;

			registerAdditionalSerialisers();

			ZoesteriaSerialisers.registerFeatureSettings(Feature.NORMAL_TREE, TreeFeatureConfigSerialiser.BASE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.ACACIA_TREE, TreeFeatureConfigSerialiser.BASE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.FANCY_TREE, TreeFeatureConfigSerialiser.BASE);

			ZoesteriaSerialisers.registerFeatureSettings(Feature.ORE, OreFeatureConfigSerialiser.BASE);

			ZoesteriaSerialisers.registerFeatureSettings(Feature.FLOWER, BlockClusterConfigSerialiser.BASE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.RANDOM_PATCH, BlockClusterConfigSerialiser.BASE);

			ZoesteriaSerialisers.registerFeatureSettings(Feature.ICEBERG, BlockStateFeatureConfigSerialiser.BASE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.LAKE, BlockStateFeatureConfigSerialiser.BASE);

			ZoesteriaSerialisers.registerFeatureSettings(Feature.PILLAGER_OUTPOST, NoFeatureConfigSerialiser.INSTANCE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.WOODLAND_MANSION, NoFeatureConfigSerialiser.INSTANCE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.JUNGLE_TEMPLE, NoFeatureConfigSerialiser.INSTANCE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.DESERT_PYRAMID, NoFeatureConfigSerialiser.INSTANCE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.IGLOO, NoFeatureConfigSerialiser.INSTANCE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.STRONGHOLD, NoFeatureConfigSerialiser.INSTANCE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.OCEAN_MONUMENT, NoFeatureConfigSerialiser.INSTANCE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.NETHER_BRIDGE, NoFeatureConfigSerialiser.INSTANCE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.END_CITY, NoFeatureConfigSerialiser.INSTANCE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.NO_OP, NoFeatureConfigSerialiser.INSTANCE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.CHORUS_PLANT, NoFeatureConfigSerialiser.INSTANCE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.VOID_START_PLATFORM, NoFeatureConfigSerialiser.INSTANCE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.DESERT_WELL, NoFeatureConfigSerialiser.INSTANCE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.FOSSIL, NoFeatureConfigSerialiser.INSTANCE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.ICE_SPIKE, NoFeatureConfigSerialiser.INSTANCE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.GLOWSTONE_BLOB, NoFeatureConfigSerialiser.INSTANCE);

			// @Reason FREEZE_TOP_LAYER is automatic and mandatory - why wouldn't you use it?
			// I might make it configured if I do nether biomes / end biomes
			//FeatureSerialisers.registerFeatureSettings(Feature.FREEZE_TOP_LAYER, NoFeatureConfigHandler.INSTANCE);

			ZoesteriaSerialisers.registerFeatureSettings(Feature.VINES, NoFeatureConfigSerialiser.INSTANCE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.MONSTER_ROOM, NoFeatureConfigSerialiser.INSTANCE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.BLUE_ICE, NoFeatureConfigSerialiser.INSTANCE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.END_ISLAND, NoFeatureConfigSerialiser.INSTANCE);

			ZoesteriaSerialisers.registerFeatureSettings(Feature.KELP, NoFeatureConfigSerialiser.INSTANCE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.CORAL_TREE, NoFeatureConfigSerialiser.INSTANCE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.CORAL_MUSHROOM, NoFeatureConfigSerialiser.INSTANCE);
			ZoesteriaSerialisers.registerFeatureSettings(Feature.CORAL_CLAW, NoFeatureConfigSerialiser.INSTANCE);

			ZoesteriaSerialisers.registerFeatureSettings(Feature.DISK, SphereReplaceConfigSerialiser.BASE);

			ZoesteriaSerialisers.registerFeatureSettings(Feature.VILLAGE, VillageConfigSerialiser.BASE);

			ZoesteriaSerialisers.registerFeatureSettings(Feature.FOREST_ROCK, BlockBlobConfigSerialiser.BASE);

			ZoesteriaSerialisers.registerFeatureSettings(Feature.RANDOM_SELECTOR, RandomSelectorConfigSerialiser.BASE);

			for (IZoesteriaJavaModule module : MODULES) {
				module.registerFeatureSerialisers();
			}
		}
	}

	/**
	 * Called at the beginning of {@link #registerFeatureSettings()}
	 */
	private static void registerAdditionalSerialisers() {
		ZoesteriaSerialisers.registerFoliagePlacer(new ResourceLocation("blob_foliage_placer"), BlobFoliagePlacer.class, BlobFoliagePlacerSerialiser.BASE);
		ZoesteriaSerialisers.registerFoliagePlacer(new ResourceLocation("spruce_foliage_placer"), SpruceFoliagePlacer.class, SpruceFoliagePlacerSerialiser.BASE);
		ZoesteriaSerialisers.registerFoliagePlacer(new ResourceLocation("pine_foliage_placer"), PineFoliagePlacer.class, PineFoliagePlacerSerialiser.BASE);
		ZoesteriaSerialisers.registerFoliagePlacer(new ResourceLocation("acacia_foliage_placer"), AcaciaFoliagePlacer.class, AcaciaFoliagePlacerSerialiser.BASE);

		ZoesteriaSerialisers.registerTreeDecorator(new ResourceLocation("beehive"), BeehiveTreeDecorator.class, ProbabilityTreeDecoratorSerialiser.BEEHIVE);
		ZoesteriaSerialisers.registerTreeDecorator(new ResourceLocation("cocoa"), CocoaTreeDecorator.class, ProbabilityTreeDecoratorSerialiser.COCOA);
		ZoesteriaSerialisers.registerTreeDecorator(new ResourceLocation("trunk_vine"), TrunkVineTreeDecorator.class, SimpleTreeDecoratorSerialiser.TRUNK_VINE);
		ZoesteriaSerialisers.registerTreeDecorator(new ResourceLocation("leaves_vine"), LeaveVineTreeDecorator.class, SimpleTreeDecoratorSerialiser.LEAVES_VINE);

		for (IZoesteriaJavaModule module : MODULES) {
			module.registerAdditionalSerialisers();
		}
	}

	public static void registerBiomePredicates() {
		for (IZoesteriaJavaModule module : MODULES) {
			module.registerBiomePredicates();
		}
	}

	public static void addJavaModule(IZoesteriaJavaModule module) {
		synchronized (MODULES) {
			MODULES.add(module);
		}
	}

	public static final Queue<BiConsumer<IForgeRegistry<SurfaceBuilder<?>>, Function<ISurfaceBuilderTemplate<?, ?>, ResourceLocation>>> SURFACE_PROCESSING = new LinkedList<>();
	public static final Queue<Consumer<IForgeRegistry<Biome>>> BIOME_PROCESSING = new LinkedList<>();
	private static final BiMap<ResourceLocation, ISurfaceBuilderTemplate<?, ?>> TEMPLATE_LOOKUP = HashBiMap.create();
	private static final List<IZoesteriaJavaModule> MODULES = new ArrayList<>();
	public static boolean preventFeatureFire = false;
	public static boolean preventPlacementFire = false;
}
