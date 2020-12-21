package tk.valoeghese.zoesteria.core.pack.biome;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import tk.valoeghese.zoesteria.api.ZFGUtils;
import tk.valoeghese.zoesteria.api.ZoesteriaSerialisers;
import tk.valoeghese.zoesteria.api.biome.BiomeDecorations;
import tk.valoeghese.zoesteria.api.biome.IBiomePredicate;
import tk.valoeghese.zoesteria.api.biome.IBiomeProperties;
import tk.valoeghese.zoesteria.api.biome.IZoesteriaBiome;
import tk.valoeghese.zoesteria.api.biome.SpawnEntry;
import tk.valoeghese.zoesteria.api.feature.IFeatureConfigSerialiser;
import tk.valoeghese.zoesteria.api.feature.IPlacementConfigSerialiser;
import tk.valoeghese.zoesteria.core.ZoesteriaMod;
import tk.valoeghese.zoesteriaconfig.api.ZoesteriaConfig;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.WritableConfig;
import tk.valoeghese.zoesteriaconfig.api.template.ConfigTemplate;

public final class BiomeFactory {
	@SuppressWarnings("unchecked")
	public static Biome buildBiome(IZoesteriaBiome biome, String packId, IForgeRegistry<Biome> biomeRegistry) {
		String id = biome.id();
		IBiomeProperties properties = biome.properties();
		BiomeDecorations decorations = biome.getDecorations();
		Optional<String> surfaceBuilder = properties.surfaceBuilder();

		SurfaceBuilder<SurfaceBuilderConfig> sb;

		boolean flag = false; // flag for whether to replace custom surface builder in load

		if (surfaceBuilder.isPresent()) {
			ResourceLocation location = new ResourceLocation(surfaceBuilder.get());
			sb = (SurfaceBuilder<SurfaceBuilderConfig>) ForgeRegistries.SURFACE_BUILDERS.getValue(location);

			if (sb == null) {
				sb = SurfaceBuilder.DEFAULT;
				flag = true;
			}
		} else {
			sb = SurfaceBuilder.DEFAULT;
		}

		Biome.Builder propertiesBuilder = new Biome.Builder()
				.category(properties.category()) // required
				.precipitation(properties.precipitation())
				.depth(properties.depth())
				.scale(properties.scale())
				.temperature(properties.temperature())
				.downfall(properties.downfall())
				.waterColor(properties.waterColour())
				.waterFogColor(properties.waterFogColour())
				.surfaceBuilder(sb, getSurfaceConfig(properties))
				.parent(null);

		Details details = new Details();
		details.skyColour = biome.customSkyColour().orElse(null);
		details.river = biome.getRiverBiome().orElse(null);
		details.hills = biome.getHillsBiomes().orElse(null);
		details.biomeTypes = biome.biomeTypes();

		// create map to store data of biome placement in the world
		Object2IntMap<BiomeManager.BiomeType> biomePlacement = new Object2IntArrayMap<>();
		// add the data from the java module to the biome placement
		biome.addPlacement(biomePlacement);
		// use the data to add the biome placement data to the details
		addGeneration(details, biomePlacement, biome.canSpawnInBiome());

		// create the biome instance
		CURRENT_LOAD_ORDER.add(packId + ":" + id);
		ZoesteriaBiome result = new ZoesteriaBiome(packId, id, propertiesBuilder, details, biomeRegistry, properties.getEntitySpawnChance());

		if (flag) {
			ZoesteriaMod.COMMON_PROCESSING.add(() -> {
				ResourceLocation location = new ResourceLocation(surfaceBuilder.get());
				result.setSurfaceBuilder((SurfaceBuilder<SurfaceBuilderConfig>) ForgeRegistries.SURFACE_BUILDERS.getValue(location));
			});
		}

		if (decorations != null) {
			ZoesteriaMod.LOGGER.info("Decorating biome " + id);
			addDecorations(result, decorations, true);
		}

		for (SpawnEntry entry : biome.mobSpawns()) {
			EntityType<?> type = entry.getEntityType();
			result.addSpawn(type.getClassification(), new SpawnListEntry(
					type,
					entry.getSpawnWeight(),
					entry.getMinGroupCount(),
					entry.getMaxGroupCount()));
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public static Biome buildBiome(File file, String packId, IForgeRegistry<Biome> biomeRegistry) {
		Container biomeConfig = ZoesteriaConfig.loadConfigWithDefaults(file, biomeDefaults);
		Container properties = biomeConfig.getContainer("properties");
		Container biomePlacement = biomeConfig.getContainer("biomePlacement");
		List<Object> decorations = biomeConfig.getList("decorations");

		String id = biomeConfig.getStringValue("id"); // required

		String surfaceBuilder = properties.getStringValue("surfaceBuilder");

		SurfaceBuilder<SurfaceBuilderConfig> sb;

		boolean flag = false; // flag for whether to replace custom surface builder in load

		if (surfaceBuilder == null) {
			sb = SurfaceBuilder.DEFAULT;
		} else {
			ResourceLocation location = new ResourceLocation(surfaceBuilder);
			sb = (SurfaceBuilder<SurfaceBuilderConfig>) ForgeRegistries.SURFACE_BUILDERS.getValue(location);

			if (sb == null) {
				sb = SurfaceBuilder.DEFAULT;
				flag = true;
			}
		}

		Biome.Builder propertiesBuilder = new Biome.Builder()
				.category(Biome.Category.valueOf(properties.getStringValue("category").toUpperCase(Locale.ROOT))) // required
				.precipitation(Biome.RainType.valueOf(properties.getStringValue("precipitation").toUpperCase(Locale.ROOT)))
				.depth(properties.getFloatValue("depth"))
				.scale(properties.getFloatValue("scale"))
				.temperature(properties.getFloatValue("temperature"))
				.downfall(properties.getFloatValue("downfall"))
				.waterColor(properties.getIntegerValue("waterColor"))
				.waterFogColor(properties.getIntegerValue("waterFogColor"))
				.surfaceBuilder(sb, getSurfaceConfig(properties))
				.parent(null);

		Details details = new Details();
		details.skyColour = properties.getIntegerValue("skyColor");
		details.river = biomeConfig.getStringValue("river");
		details.hills = biomeConfig.getList("hills");

		details.biomeTypes = biomeConfig.getList("biomeTypes")
				.stream()
				.map(str -> BiomeDictionary.Type.getType(((String) str).toUpperCase(Locale.ROOT)))
				.collect(Collectors.toList());

		// transfer loaded data about the placement of the biome in the world to the Details of the biome
		addGeneration(details, biomePlacement);

		CURRENT_LOAD_ORDER.add(packId + ":" + id);
		ZoesteriaBiome result = new ZoesteriaBiome(packId, id, propertiesBuilder, details, biomeRegistry, properties.getFloatValue("entitySpawnChance"));

		if (flag) {
			ZoesteriaMod.COMMON_PROCESSING.add(() -> {
				ResourceLocation location = new ResourceLocation(surfaceBuilder);
				result.setSurfaceBuilder((SurfaceBuilder<SurfaceBuilderConfig>) ForgeRegistries.SURFACE_BUILDERS.getValue(location));
			});
		}

		if (decorations != null) {
			ZoesteriaMod.COMMON_PROCESSING.add(() -> {
				ZoesteriaMod.LOGGER.info("Decorating biome " + id);
				addDecorations(ImmutableSet.of(result), decorations, true);

				List<Object> spawnEntries = biomeConfig.getList("mobSpawns");

				// Add spawn entries
				for (Object o : spawnEntries) {
					Map<String, Object> data = (Map<String, Object>) o;
					EntityType<?> type = ForgeRegistries.ENTITIES.getValue(new ResourceLocation((String) data.get("type")));
					result.addSpawn(type.getClassification(), new SpawnListEntry(
							type,
							Integer.parseInt((String) data.get("spawnWeight")),
							Integer.parseInt((String) data.get("minGroupCount")),
							Integer.parseInt((String) data.get("maxGroupCount"))));
				}
			});
		}

		return result;
	}

	private static void addGeneration(Details details, Object2IntMap<BiomeManager.BiomeType> biomePlacement, boolean spawnBiome) {
		if (biomePlacement.isEmpty()) {
			return;
		}

		int desert = biomePlacement.getInt(BiomeType.DESERT);
		int warm = biomePlacement.getInt(BiomeType.WARM);
		int cool = biomePlacement.getInt(BiomeType.COOL);
		int icy = biomePlacement.getInt(BiomeType.ICY);

		if (desert > 0) {
			details.placement.put(BiomeManager.BiomeType.DESERT, desert);
		}

		if (warm > 0) {
			details.placement.put(BiomeManager.BiomeType.WARM, warm);
		}

		if (cool > 0) {
			details.placement.put(BiomeManager.BiomeType.COOL, cool);
		}

		if (icy > 0) {
			details.placement.put(BiomeManager.BiomeType.ICY, icy);
		}

		details.spawnBiome = spawnBiome;
	}

	private static void addGeneration(Details details, Container biomePlacement) {
		if (biomePlacement == null) {
			return;
		}

		Integer desert = biomePlacement.getIntegerValue("desert");
		Integer warm = biomePlacement.getIntegerValue("warm");
		Integer cool = biomePlacement.getIntegerValue("cool");
		Integer icy = biomePlacement.getIntegerValue("icy");

		if (desert != null) {
			details.placement.put(BiomeManager.BiomeType.DESERT, desert.intValue());
		}

		if (warm != null) {
			details.placement.put(BiomeManager.BiomeType.WARM, warm.intValue());
		}

		if (cool != null) {
			details.placement.put(BiomeManager.BiomeType.COOL, cool.intValue());
		}

		if (icy != null) {
			details.placement.put(BiomeManager.BiomeType.ICY, icy.intValue());
		}

		details.spawnBiome = ZFGUtils.getBooleanOrDefault(biomePlacement, "canSpawnInBiome", false);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void addDecorations(Biome biome, BiomeDecorations decorations, boolean addDefaults) {
		if (addDefaults) {
			DefaultBiomeFeatures.addCarvers(biome);
			DefaultBiomeFeatures.addStructures(biome);
			DefaultBiomeFeatures.addFreezeTopLayer(biome);
		}

		for (Tuple<Decoration, ConfiguredFeature> entry : decorations.toImmutableList()) {
			ConfiguredFeature feature = entry.getB();

			if (feature.feature instanceof Structure) {
				biome.addStructure(feature);
			} else {
				biome.addFeature(entry.getA(), feature);
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static BiomeDecorations deserialiseDecorations(@Nullable int[] counters, List<Object> decorations) {
		int decorationCounter = 0;
		int entryCounter = 0;

		BiomeDecorations biomeDecorations = BiomeDecorations.create();

		for (Object rawEntry : decorations) {
			entryCounter++;

			if (rawEntry == null) {
				throw new NullPointerException("Decoration entry found to be null where a decoration was expected!");
			}

			if (rawEntry instanceof Map) {
				decorationCounter++;
				Map<String, Object> entry = (Map<String, Object>) rawEntry;

				ConfiguredFeature configuredFeature = deserialiseConfiguredFeature(entry);

				if (configuredFeature.feature instanceof Structure) {
					biomeDecorations.addStructure(configuredFeature);
				} else {
					Placement placementType = ForgeRegistries.DECORATORS.getValue(new ResourceLocation((String) entry.get("placementType")));

					IPlacementConfigSerialiser placement = ZoesteriaSerialisers.getPlacement(placementType)
							.deserialise(ZoesteriaConfig.createWritableConfig((Map<String, Object>) entry.get("placement")));

					biomeDecorations.addDecoration(GenerationStage.Decoration.valueOf((String) entry.get("step")),
							configuredFeature.withPlacement(placementType.configure(placement.create())));
				}
			}
		}

		if (counters != null) {
			counters[0] = decorationCounter;
			counters[1] = entryCounter;
		}

		return biomeDecorations;
	}

	private static void addDecorations(Set<Biome> biomes, List<Object> decorations, boolean addDefaults) {
		int[] counters = new int[2];

		BiomeDecorations biomeDecorations = deserialiseDecorations(counters, decorations);

		for (Biome biome : biomes) {
			addDecorations(biome, biomeDecorations, addDefaults);
		}

		if (addDefaults) {
			ZoesteriaMod.LOGGER.info("Decorated biome: " + counters[0] + " decorations / " + counters[1] + " entries.");
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ConfiguredFeature deserialiseConfiguredFeature(Map<String, Object> entry) {
		ResourceLocation featureResource = new ResourceLocation((String) entry.get("feature"));
		Feature feature = ForgeRegistries.FEATURES.getValue(featureResource);

		if (feature == null) {
			throw new NullPointerException("Invalid or unregistered feature given in decorations!");
		}

		IFeatureConfigSerialiser config = ZoesteriaSerialisers.getFeatureSettings(feature)
				.deserialise(ZoesteriaConfig.createWritableConfig((Map<String, Object>) entry.get("settings")));

		return feature.withConfiguration(config.create());
	}

	private static SurfaceBuilderConfig getSurfaceConfig(IBiomeProperties properties) {
		BlockState topMaterial = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(properties.topBlock().orElse("minecraft:grass_block"))).getDefaultState();
		BlockState underMaterial = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(properties.fillerBlock().orElse("minecraft:dirt"))).getDefaultState();
		BlockState underWaterMaterial = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(properties.underwaterBlock().orElse("minecraft:gravel"))).getDefaultState();

		return new SurfaceBuilderConfig(topMaterial, underMaterial, underWaterMaterial);
	}

	private static SurfaceBuilderConfig getSurfaceConfig(Container properties) {
		Container surface = properties.getContainer("surface");

		if (surface == null) {
			return SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG;
		} else {
			BlockState topBlock = ZFGUtils.getBlock(surface.getStringValue("topBlock"), Blocks.GRASS_BLOCK).getDefaultState();
			BlockState fillerBlock = ZFGUtils.getBlock(surface.getStringValue("fillerBlock"), Blocks.DIRT).getDefaultState();
			BlockState underwaterBlock = ZFGUtils.getBlock(surface.getStringValue("underwaterBlock"), Blocks.GRAVEL).getDefaultState();

			return new SurfaceBuilderConfig(topBlock, fillerBlock, underwaterBlock);
		}
	}

	// Tweaks

	public static void resolveTweaks(File file, String id) {
		Container data = ZoesteriaConfig.loadConfig(file);
		Container target = data.getContainer("target");

		IBiomePredicate predicate = ZoesteriaSerialisers.getPredicate(new ResourceLocation(target.getStringValue("selector"))).deserialise(target);
		ZoesteriaMod.addTweak(new Tuple<>(predicate, deserialiseDecorations(null, data.getList("decorations"))));
	}

	public static void writeLoadOrder(File file) {
		WritableConfig data = ZoesteriaConfig.createWritableConfig(new LinkedHashMap<>());
		data.putList("lastLoadOrder", CURRENT_LOAD_ORDER);
		data.writeToFile(file);
	}

	private static final ConfigTemplate biomeDefaults = ConfigTemplate.builder()
			.addContainer("properties", container -> {
				container.addDataEntry("precipitation", Biome.RainType.RAIN.name());
				container.addDataEntry("depth", "0.15");
				container.addDataEntry("scale", "0.125");
				container.addDataEntry("temperature", "0.5");
				container.addDataEntry("downfall", "0.5");
				container.addDataEntry("waterColor", "4159204");
				container.addDataEntry("waterFogColor", "329011");
			})
			.build();

	private static final List<Object> CURRENT_LOAD_ORDER = new ArrayList<>();

	static final class Details {
		Integer skyColour;
		String river;
		List<? extends Object> hills;
		List<BiomeDictionary.Type> biomeTypes;
		Object2IntMap<BiomeManager.BiomeType> placement = new Object2IntArrayMap<>();
		boolean spawnBiome;
	}
}
