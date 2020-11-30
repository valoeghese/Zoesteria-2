package tk.valoeghese.zoesteria.core.genmodifierpack;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import com.google.common.collect.Maps;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.util.Tuple;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.DecoratedFlowerFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import tk.valoeghese.common.util.FileUtils;
import tk.valoeghese.zoesteria.api.IZoesteriaJavaModule;
import tk.valoeghese.zoesteria.api.biome.IBiomeProperties;
import tk.valoeghese.zoesteria.api.biome.IZoesteriaBiome;
import tk.valoeghese.zoesteria.api.feature.FeatureSerialisers;
import tk.valoeghese.zoesteria.api.feature.IZoesteriaFeatureConfig;
import tk.valoeghese.zoesteria.api.feature.IZoesteriaPlacementConfig;
import tk.valoeghese.zoesteria.core.ZoesteriaMod;
import tk.valoeghese.zoesteria.core.ZoesteriaRegistryHandler;
import tk.valoeghese.zoesteria.core.genmodifierpack.biome.BiomeFactory;
import tk.valoeghese.zoesteriaconfig.api.ZoesteriaConfig;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public final class GenModifierPack {
	private GenModifierPack(String id, String packDir, boolean enabled) {
		this.id = id;
		this.packDir = packDir;
		this.disabled = !enabled;
	}

	private final String id;
	private final String packDir;
	private final boolean disabled;

	public void loadBiomes(IForgeRegistry<Biome> biomeRegistry) {
		ZoesteriaRegistryHandler.featureSettingsRegister();
		ZoesteriaRegistryHandler.placementSettingsRegister();
		
		doHacks(biomeRegistry);

		if (this.disabled) {
			return;
		}

		File biomesDir = new File(this.packDir + "./biomes/");

		if (!biomesDir.isDirectory()) {
			return;
		}

		FileUtils.trailFilesOfExtension(biomesDir, "cfg", (file, trail) -> {
			BiomeFactory.buildBiome(file, this.id, biomeRegistry);
		});

		doHacks2(biomeRegistry);
	}

	public String getId() {
		return this.id;
	}

	public boolean isDisabled() {
		return this.disabled;
	}

	public static void addIfAbsent(String packDir) {
		File manifest = new File(packDir + "/manifest.cfg");

		if (!manifest.isFile()) {
			return;
		}

		Container packManifest = ZoesteriaConfig.loadConfig(manifest);
		int version = packManifest.getIntegerValue("schemaVersion").intValue();

		switch (version) {
		case 0:
			String id = packManifest.getStringValue("id");

			if (isLoaded(id)) {
				System.out.println("GenModifierPack with id \"" + id + "\" already loaded.");
				return;
			}

			Boolean enabled = Utils.getBoolean(packManifest, "enabled", true);

			System.out.println("Zoesteria has detected module: " + id);
			PACKS.put(id, new GenModifierPack(id, packDir, enabled.booleanValue()));
			break;
		default:
			throw new RuntimeException("Unknown pack schemaVersion detected!");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void addJavaModuleIfAbsent(IZoesteriaJavaModule module) {
		String packId = module.packId();

		if (!isLoaded(packId)) {
			// create module dir
			String packDir = "./zoesteria/" + packId;
			File packDirFile = new File(packDir);
			packDirFile.mkdirs();

			// create manifest
			ZoesteriaConfig.createWritableConfig(module.createManifest().asMap()).writeToFile(new File(packDir + "/manifest.cfg"));

			new File(packDir + "/biomes").mkdir();

			// create biome files
			for (IZoesteriaBiome biome : module.createBiomes()) {
				Map<String, Object> fileData = Maps.newLinkedHashMap();

				fileData.put("id", biome.id());

				// create properties
				IBiomeProperties biomeProperties = biome.properties();
				Map<String, Object> biomePropertiesData = Maps.newLinkedHashMap();

				biomePropertiesData.put("category", biomeProperties.category().name().toLowerCase());
				biomePropertiesData.put("precipitation", biomeProperties.precipitation().name().toLowerCase());
				biomePropertiesData.put("depth", biomeProperties.depth());
				biomePropertiesData.put("scale", biomeProperties.scale());
				biomePropertiesData.put("temperature", biomeProperties.temperature());
				biomePropertiesData.put("downfall", biomeProperties.downfall());

				// create surface
				Optional<String> topBlock = biomeProperties.topBlock();
				Optional<String> fillerBlock = biomeProperties.fillerBlock();
				Optional<String> underwaterBlock = biomeProperties.underwaterBlock();

				boolean hasTopBlock = topBlock.isPresent();
				boolean hasFillerBlock = fillerBlock.isPresent();
				boolean hasUnderwaterBlock = underwaterBlock.isPresent();

				biomePropertiesData.put("waterColor", biomeProperties.waterColour());
				biomePropertiesData.put("waterFogColor", biomeProperties.waterFogColour());

				Optional<Integer> skyColour = biome.customSkyColour();

				if (skyColour.isPresent()) {
					biomePropertiesData.put("skyColor", skyColour.get().toString());
				}

				if (hasTopBlock || hasFillerBlock || hasUnderwaterBlock) {
					Map<String, Object> surfaceData = Maps.newLinkedHashMap();

					if (hasTopBlock) {
						surfaceData.put("topBlock", topBlock.get());
					}

					if (hasFillerBlock) {
						surfaceData.put("fillerBlock", fillerBlock.get());
					}

					if (hasUnderwaterBlock) {
						surfaceData.put("underwaterBlock", underwaterBlock.get());
					}

					biomePropertiesData.put("surface", surfaceData);
				}

				// features
				List<Tuple<GenerationStage.Decoration, ConfiguredFeature>> features = biome.getDecorations().toImmutableList();

				if (!features.isEmpty()) {
					List<Object> decorations = new ArrayList<>();

					for (Tuple<GenerationStage.Decoration, ConfiguredFeature> decoration : features) {
						ConfiguredFeature feature = decoration.getB();
						DecoratedFeatureConfig dfc;

						if (feature.feature instanceof DecoratedFeature || feature.feature instanceof DecoratedFlowerFeature) {
							dfc = (DecoratedFeatureConfig) feature.config;
						} else {
							ZoesteriaMod.LOGGER.warn("Can only serialise decorated features in configs! Defaulting to a Passthrough placement?");
							feature = feature.withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG));
							dfc = (DecoratedFeatureConfig) feature.config;
						}

						Map<String, Object> entry = new LinkedHashMap<>();
						entry.put("step", decoration.getA().name());
						entry.put("feature", ForgeRegistries.FEATURES.getKey(dfc.feature.feature).toString());
						entry.put("placementType", ForgeRegistries.DECORATORS.getKey(dfc.decorator.decorator).toString());

						// haha funni raw type go brr
						handleFeature((ConfiguredFeature) dfc.feature, entry);
						handlePlacement((ConfiguredPlacement) dfc.decorator, entry);

						// add to decorations list
						decorations.add(entry);
					}

					fileData.put("decorations", decorations);
				}

				// final
				fileData.put("properties", biomePropertiesData);

				Optional<String> river = biome.getRiver();

				if (river.isPresent()) {
					fileData.put("river", river.get());
				}

				Object2IntMap<BiomeManager.BiomeType> biomePlacement = new Object2IntArrayMap<>();
				biome.addPlacement(biomePlacement);

				Map<String, Object> biomePlacementData = Maps.newLinkedHashMap();

				biomePlacement.forEach((biomeType, weight) -> biomePlacementData.put(biomeType.name().toLowerCase(), weight.toString()));
				biomePlacementData.put("canSpawnInBiome", String.valueOf(biome.canSpawnInBiome()));

				fileData.put("biomePlacement", biomePlacementData);

				// write to file
				ZoesteriaMod.LOGGER.info("Writing biome " + biome.id() + " to config file.");
				ZoesteriaConfig.createWritableConfig(fileData).writeToFile(new File(packDir + "/biomes/" + biome.id() + ".cfg"));
			}

			GenModifierPack.addIfAbsent(packDir);

			if (loadedPackBiomes && PACKS.containsKey(packId)) {
				PACKS.get(packId).loadBiomes(ForgeRegistries.BIOMES);
			}
		}
	}

	private static <T extends IFeatureConfig> void handleFeature(ConfiguredFeature<T, Feature<T>> feature, Map<String, Object> map) {
		IZoesteriaFeatureConfig<T> fc = FeatureSerialisers.getFeatureSettings(feature.feature);
		fc = fc.loadFrom(feature.config); // load from config

		EditableContainer settings = ZoesteriaConfig.createWritableConfig(new LinkedHashMap<>());
		fc.serialise(settings);
		map.put("settings", settings.asMap());
	}

	private static <T extends IPlacementConfig> void handlePlacement(ConfiguredPlacement<T> placement, Map<String, Object> map) {
		IZoesteriaPlacementConfig<T> fc = FeatureSerialisers.getPlacement(placement.decorator);
		fc = fc.loadFrom(placement.config);

		EditableContainer settings = ZoesteriaConfig.createWritableConfig(new LinkedHashMap<>());
		fc.serialise(settings);
		map.put("placement", settings.asMap());
	}

	public static void forEach(Consumer<GenModifierPack> callback) {
		PACKS.forEach((id, pack) -> callback.accept(pack));
	}

	public static void init() {
		if (!initialised) {
			if (ROOT_DIR.isDirectory()) {
				FileUtils.forEachDirectory(ROOT_DIR, dir -> {
					GenModifierPack.addIfAbsent(dir.getPath());
				});
			}

			initialised = true;
		}
	}

	public static void flagLoadedPackBiomes() {
		loadedPackBiomes = true;
	}

	public static boolean isLoaded(String packId) {
		return PACKS.containsKey(packId);
	}

	public static void doHacks(IForgeRegistry<?> registry) {
		if (loadedPackBiomes) {
			try {
				Field yeet = ForgeRegistry.class.getDeclaredField("isFrozen");
				yeet.setAccessible(true);
				yeet.set(registry, false);
			} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public static void doHacks2(IForgeRegistry<?> registry) {
		if (loadedPackBiomes) {
			try {
				Field yeet = ForgeRegistry.class.getDeclaredField("isFrozen");
				yeet.setAccessible(true);
				yeet.set(registry, true);
			} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private static boolean initialised = false;
	private static boolean loadedPackBiomes = false;

	private static final Map<String, GenModifierPack> PACKS = Maps.newHashMap();
	public static final File ROOT_DIR = new File("./zoesteria");
}
