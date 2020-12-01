package tk.valoeghese.zoesteria.core.genmodifierpack.biome;

import java.io.File;
import java.util.List;
import java.util.Map;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import tk.valoeghese.zoesteria.api.biome.BiomeDecorations;
import tk.valoeghese.zoesteria.api.biome.IBiomeProperties;
import tk.valoeghese.zoesteria.api.biome.IZoesteriaBiome;
import tk.valoeghese.zoesteria.api.feature.FeatureSerialisers;
import tk.valoeghese.zoesteria.api.feature.IZoesteriaFeatureConfig;
import tk.valoeghese.zoesteria.api.feature.IZoesteriaPlacementConfig;
import tk.valoeghese.zoesteria.core.ZFGUtils;
import tk.valoeghese.zoesteria.core.ZoesteriaMod;
import tk.valoeghese.zoesteriaconfig.api.ZoesteriaConfig;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.template.ConfigTemplate;

public final class BiomeFactory {
	public static Biome buildBiome(IZoesteriaBiome biome, String packId, IForgeRegistry<Biome> biomeRegistry) {
		String id = biome.id();
		IBiomeProperties properties = biome.properties();
		BiomeDecorations decorations = biome.getDecorations();

		Biome.Builder propertiesBuilder = new Biome.Builder()
				.category(properties.category()) // required
				.precipitation(properties.precipitation())
				.depth(properties.depth())
				.scale(properties.scale())
				.temperature(properties.temperature())
				.downfall(properties.downfall())
				.waterColor(properties.waterColour())
				.waterFogColor(properties.waterFogColour())
				.surfaceBuilder(SurfaceBuilder.DEFAULT, getSurfaceConfig(properties))
				.parent(null);

		Details details = new Details();
		details.skyColour = biome.customSkyColour().orElse(null);
		details.river = biome.getRiver().orElse(null);

		Object2IntMap<BiomeManager.BiomeType> biomePlacement = new Object2IntArrayMap<>();
		biome.addPlacement(biomePlacement);
		addGeneration(details, biomePlacement, biome.canSpawnInBiome());
		
		Biome result = new ZoesteriaBiome(packId, id, propertiesBuilder, details, biomeRegistry);

		if (decorations != null) {
			ZoesteriaMod.LOGGER.info("Decorating biome " + id);
			addDecorations(result, decorations);
		}

		return result;
	}

	public static Biome buildBiome(File file, String packId, IForgeRegistry<Biome> biomeRegistry) {
		Container biomeConfig = ZoesteriaConfig.loadConfigWithDefaults(file, biomeDefaults);
		Container properties = biomeConfig.getContainer("properties");
		Container biomePlacement = biomeConfig.getContainer("biomePlacement");
		List<Object> decorations = biomeConfig.getList("decorations");

		String id = biomeConfig.getStringValue("id"); // required

		Biome.Builder propertiesBuilder = new Biome.Builder()
				.category(Biome.Category.valueOf(properties.getStringValue("category").toUpperCase())) // required
				.precipitation(Biome.RainType.valueOf(properties.getStringValue("precipitation").toUpperCase()))
				.depth(properties.getFloatValue("depth"))
				.scale(properties.getFloatValue("scale"))
				.temperature(properties.getFloatValue("temperature"))
				.downfall(properties.getFloatValue("downfall"))
				.waterColor(properties.getIntegerValue("waterColor"))
				.waterFogColor(properties.getIntegerValue("waterFogColor"))
				.surfaceBuilder(SurfaceBuilder.DEFAULT, getSurfaceConfig(properties))
				.parent(null);

		Details details = new Details();
		details.skyColour = properties.getIntegerValue("skyColor");
		details.river = biomeConfig.getStringValue("river");

		addGeneration(details, biomePlacement);

		Biome result = new ZoesteriaBiome(packId, id, propertiesBuilder, details, biomeRegistry);

		if (decorations != null) {
			ZoesteriaMod.COMMON_PROCESSING.add(() -> {
				ZoesteriaMod.LOGGER.info("Decorating biome " + id);
				addDecorations(result, decorations);
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

	@SuppressWarnings("rawtypes")
	private static void addDecorations(Biome biome, BiomeDecorations decorations) {
		for (Tuple<Decoration, ConfiguredFeature> entry : decorations.toImmutableList()) {
			biome.addFeature(entry.getA(), entry.getB());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void addDecorations(Biome biome, List<Object> decorations) {
		int decorationCounter = 0;
		int entryCounter = 0;

		DefaultBiomeFeatures.addCarvers(biome);
		DefaultBiomeFeatures.addStructures(biome);

		for (Object rawEntry : decorations) {
			entryCounter++;

			if (rawEntry == null) {
				throw new NullPointerException("Decoration entry found to be null where a decoration was expected!");
			}

			if (rawEntry instanceof Map) {
				decorationCounter++;
				Map<String, Object> entry = (Map<String, Object>) rawEntry;
				ResourceLocation featureResource = new ResourceLocation((String) entry.get("feature"));
				Feature feature = ForgeRegistries.FEATURES.getValue(featureResource);

				if (feature == null) {
					throw new NullPointerException("Invalid or unregistered feature given in decorations!");
				}

				IZoesteriaFeatureConfig config = FeatureSerialisers.getFeatureSettings(feature)
						.deserialise(ZoesteriaConfig.createWritableConfig((Map<String, Object>) entry.get("settings")));

				Placement placementType = ForgeRegistries.DECORATORS.getValue(new ResourceLocation((String) entry.get("placementType")));

				IZoesteriaPlacementConfig placement = FeatureSerialisers.getPlacement(placementType)
						.deserialise(ZoesteriaConfig.createWritableConfig((Map<String, Object>) entry.get("placement")));

				biome.addFeature(GenerationStage.Decoration.valueOf((String) entry.get("step")),
						feature.withConfiguration(config.create())
						.withPlacement(placementType.configure(placement.create())));
			}
		}

		DefaultBiomeFeatures.addFreezeTopLayer(biome);
		ZoesteriaMod.LOGGER.info("Decorated biome: " + decorationCounter + " decorations / " + entryCounter + " entries.");
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

	static final class Details {
		Integer skyColour;
		String river;
		Object2IntMap<BiomeManager.BiomeType> placement = new Object2IntArrayMap<>();
		boolean spawnBiome;
	}
}
