package tk.valoeghese.zoesteria.core.pack;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.collect.Maps;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.util.ResourceLocation;
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
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import tk.valoeghese.common.util.FileUtils;
import tk.valoeghese.zoesteria.api.IZFGSerialisable;
import tk.valoeghese.zoesteria.api.IZoesteriaJavaModule;
import tk.valoeghese.zoesteria.api.ZFGUtils;
import tk.valoeghese.zoesteria.api.biome.BiomeDecorations;
import tk.valoeghese.zoesteria.api.biome.IBiomeProperties;
import tk.valoeghese.zoesteria.api.biome.IZoesteriaBiome;
import tk.valoeghese.zoesteria.api.feature.FeatureSerialisers;
import tk.valoeghese.zoesteria.api.feature.IFeatureConfigSerialiser;
import tk.valoeghese.zoesteria.api.feature.IPlacementConfigSerialiser;
import tk.valoeghese.zoesteria.api.surface.ISurfaceBuilderTemplate;
import tk.valoeghese.zoesteria.core.ZoesteriaMod;
import tk.valoeghese.zoesteria.core.ZoesteriaRegistryHandler;
import tk.valoeghese.zoesteria.core.pack.biome.BiomeFactory;
import tk.valoeghese.zoesteriaconfig.api.ZoesteriaConfig;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;
import tk.valoeghese.zoesteriaconfig.api.container.WritableConfig;

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
	}

	public void loadTweaks() {
		if (this.disabled) {
			return;
		}

		File tweaksDir = new File(this.packDir + "./tweaks/");

		if (!tweaksDir.isDirectory()) {
			return;
		}

		FileUtils.trailFilesOfExtension(tweaksDir, "cfg", (file, trail) -> {
			BiomeFactory.resolveTweaks(file, this.id);
		});
	}

	public void loadSurfaces(IForgeRegistry<SurfaceBuilder<?>> surfaceRegistry, Function<ResourceLocation, ISurfaceBuilderTemplate<?>> templateProvider) {
		if (this.disabled) {
			return;
		}

		File surfaceBuilderDir = new File(this.packDir + "./surfacebuilders/");

		if (!surfaceBuilderDir.isDirectory()) {
			return;
		}

		FileUtils.trailFilesOfExtension(surfaceBuilderDir, "cfg", (file, trail) -> {
			// get data
			Container surfaceBuilderConfig = ZoesteriaConfig.loadConfig(file);
			// get template
			ISurfaceBuilderTemplate<?> template = templateProvider.apply(new ResourceLocation(surfaceBuilderConfig.getStringValue("template")));
			// create surface builder
			surfaceRegistry.register(template.create(surfaceBuilderConfig).setRegistryName(new ResourceLocation(this.id, surfaceBuilderConfig.getStringValue("id"))));
		});
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

			Boolean enabled = ZFGUtils.getBooleanOrDefault(packManifest, "enabled", true);

			ZoesteriaMod.LOGGER.info("Zoesteria has detected module: " + id);
			PACKS.put(id, new GenModifierPack(id, packDir, enabled.booleanValue()));
			break;
		default:
			throw new RuntimeException("Unknown pack schemaVersion detected!");
		}
	}

	@SuppressWarnings("rawtypes")
	public static void addJavaModuleIfAbsent(IZoesteriaJavaModule module) {
		GenModifierPack.init(); // make sure configs have loaded before java modules

		String packId = module.packId();
		AtomicBoolean setIsLoaded = new AtomicBoolean(false); // in case of varying order
		AtomicBoolean isLoaded = new AtomicBoolean(false);
		final List<IZoesteriaBiome> moduleBiomes = module.createBiomes();

		ZoesteriaRegistryHandler.BIOME_PROCESSING.add(biomeRegistry -> {
			if (!setIsLoaded.get()) {
				setIsLoaded.set(true);
				isLoaded.set(isLoaded(packId));
			}

			if (!isLoaded.get()) {
				for (IZoesteriaBiome moduleBiome : moduleBiomes) {
					BiomeFactory.buildBiome(moduleBiome, module.packId(), biomeRegistry);
				}
			}
		});

		ZoesteriaRegistryHandler.SURFACE_PROCESSING.add((surfaceRegistry, templateIdProvider) -> {
			if (!setIsLoaded.get()) {
				setIsLoaded.set(true);
				isLoaded.set(isLoaded(packId));
			}

			if (!isLoaded.get()) {
				// create module dir
				String packDir = "./zoesteria/" + packId;
				File packDirFile = new File(packDir);
				packDirFile.mkdirs();
				new File(packDir, "surfacebuilders").mkdir();

				module.createSurfaceBuilders().forEach(surfaceBuilder -> {
					WritableConfig config = ZoesteriaConfig.createWritableConfig(new LinkedHashMap<>());
					config.putStringValue("id", surfaceBuilder.id);
					config.putStringValue("template", templateIdProvider.apply(surfaceBuilder.template).toString());

					// convert to data
					List<? extends IZFGSerialisable> stepsJava = surfaceBuilder.steps;
					List<Object> stepsData = new ArrayList<>();

					for (IZFGSerialisable serialisable : stepsJava) {
						// add data
						stepsData.add(serialisable.toZoesteriaConfig().asMap());
					}

					config.putList("steps", stepsData);
					File surfaceBuilderFile = new File(packDir + "/surfacebuilders/" + surfaceBuilder.id + ".cfg");
					config.writeToFile(surfaceBuilderFile);

					// create and register surface builder
					surfaceRegistry.register(surfaceBuilder.template.create(ZoesteriaConfig.loadConfig(surfaceBuilderFile)).setRegistryName(new ResourceLocation(packId, surfaceBuilder.id)));
				});
			}
		});

		ZoesteriaMod.COMMON_PROCESSING.add(() -> {
			if (!isLoaded.get()) {
				// create module dir
				String packDir = "./zoesteria/" + packId;
				File packDirFile = new File(packDir);
				packDirFile.mkdirs();

				// create manifest
				ZoesteriaConfig.createWritableConfig(module.createManifest().asMap()).writeToFile(new File(packDir + "/manifest.cfg"));

				new File(packDir, "biomes").mkdir();

				// create biome files
				for (IZoesteriaBiome biome : moduleBiomes) {
					Map<String, Object> fileData = Maps.newLinkedHashMap();

					fileData.put("id", biome.id());

					// create properties
					IBiomeProperties biomeProperties = biome.properties();
					Map<String, Object> biomePropertiesData = Maps.newLinkedHashMap();

					// add biome properties data to config
					biomePropertiesData.put("category", biomeProperties.category().name().toLowerCase());
					biomePropertiesData.put("precipitation", biomeProperties.precipitation().name().toLowerCase());
					biomePropertiesData.put("depth", biomeProperties.depth());
					biomePropertiesData.put("scale", biomeProperties.scale());
					biomePropertiesData.put("temperature", biomeProperties.temperature());
					biomePropertiesData.put("downfall", biomeProperties.downfall());
					biomeProperties.surfaceBuilder().ifPresent(id -> biomePropertiesData.put("surfaceBuilder", id));

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

					// handle default surface blocks
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
						addDecorations(features, fileData);
					}

					// final
					fileData.put("properties", biomePropertiesData);

					// river biome in config
					Optional<String> river = biome.getRiverBiome();

					if (river.isPresent()) {
						fileData.put("river", river.get());
					}

					// hills biomes in config
					Optional<List<String>> hills = biome.getHillsBiomes();

					if (hills.isPresent()) {
						fileData.put("hills", hills.get());
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

				// handle tweaks
				Map<BiomeDictionary.Type, BiomeDecorations> tweaks = new LinkedHashMap<>();
				module.addBiomeTweaks(tweaks);

				if (!tweaks.isEmpty()) {
					File tweaksDir = new File(packDirFile, "tweaks");
					tweaksDir.mkdir();

					tweaks.forEach((type, decorations) -> {
						// add to game
						for (Biome biome : BiomeDictionary.getBiomes(type)) {
							BiomeFactory.addDecorations(biome, decorations, false);
						}

						// store data

						String typeName = type.getName().toLowerCase();

						Map<String, Object> fileData = new LinkedHashMap<>();
						// target selector data
						Map<String, Object> targetData = new LinkedHashMap<>();
						targetData.put("selector", "biome_dictionary");
						targetData.put("biomeType", typeName);
						fileData.put("target", targetData);
						// decorations
						addDecorations(decorations.toImmutableList(), fileData);

						// file storage
						File file = new File(tweaksDir, typeName + "_tweaks.cfg");
						ZoesteriaConfig.createWritableConfig(fileData).writeToFile(file);
					});

				}

				// ONLY ADD IT AFTER THE DATA ONES HAVE BEEN RUN
				// THIS PREVENTS STUFF BEING CONSTRUCTED TWICE
				// Why add it then? I might make a way to view loaded packs in the future.
				GenModifierPack.addIfAbsent(packDir);
			}
		});

		ZoesteriaRegistryHandler.addJavaModule(module);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void addDecorations(Iterable<Tuple<GenerationStage.Decoration, ConfiguredFeature>> features, Map<String, Object> fileData) {
		List<Object> decorations = new ArrayList<>();

		for (Tuple<GenerationStage.Decoration, ConfiguredFeature> decoration : features) {
			ConfiguredFeature feature = decoration.getB();
			DecoratedFeatureConfig dfc;

			if (feature.feature instanceof DecoratedFeature || feature.feature instanceof DecoratedFlowerFeature) {
				dfc = (DecoratedFeatureConfig) feature.config;
			} else {
				ZoesteriaMod.LOGGER.warn("Can only serialise decorated features and decorated flower features in configs! Defaulting to a Passthrough placement?");
				feature = feature.withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG));
				dfc = (DecoratedFeatureConfig) feature.config;
			}

			Map<String, Object> entry = new LinkedHashMap<>();
			entry.put("step", decoration.getA().name());
			serialiseConfiguredFeature(entry, dfc.feature); // serialise the configured feature
			entry.put("placementType", ForgeRegistries.DECORATORS.getKey(dfc.decorator.decorator).toString());

			// haha funni raw type go brr
			addPlacement((ConfiguredPlacement) dfc.decorator, entry);

			// add to decorations list
			decorations.add(entry);
		}

		fileData.put("decorations", decorations);
	}

	public static <T extends IFeatureConfig> void serialiseConfiguredFeature(Map<String, Object> map, ConfiguredFeature<T, ? extends Feature<T>> feature) {
		map.put("feature", ForgeRegistries.FEATURES.getKey(feature.feature).toString());

		// feature config
		IFeatureConfigSerialiser<T> fc = FeatureSerialisers.getFeatureSettings(feature.feature);
		fc = fc.loadFrom(feature.config); // load from config

		EditableContainer settings = ZoesteriaConfig.createWritableConfig(new LinkedHashMap<>());
		fc.serialise(settings);
		map.put("settings", settings.asMap());
	}

	private static <T extends IPlacementConfig> void addPlacement(ConfiguredPlacement<T> placement, Map<String, Object> map) {
		IPlacementConfigSerialiser<T> fc = FeatureSerialisers.getPlacement(placement.decorator);
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

	public static boolean isLoaded(String packId) {
		return PACKS.containsKey(packId);
	}

	private static boolean initialised = false;

	private static final Map<String, GenModifierPack> PACKS = Maps.newHashMap();
	public static final File ROOT_DIR = new File("./zoesteria");
}
