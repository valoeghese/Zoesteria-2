package tk.valoeghese.zoesteria.core.genmodifierpack.biome;

import java.io.File;
import java.util.List;
import java.util.Map;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import tk.valoeghese.zoesteria.api.feature.FeatureSerialisers;
import tk.valoeghese.zoesteria.api.feature.IZoesteriaFeatureConfig;
import tk.valoeghese.zoesteria.api.feature.IZoesteriaPlacementConfig;
import tk.valoeghese.zoesteria.core.ZoesteriaMod;
import tk.valoeghese.zoesteria.core.genmodifierpack.Utils;
import tk.valoeghese.zoesteriaconfig.api.ZoesteriaConfig;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.template.ConfigTemplate;

public final class BiomeFactory {
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
			ZoesteriaMod.LOGGER.info("Decorating biome " + id);
			addDecorations(result, decorations);
		}

		return result;
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

		details.spawnBiome = Utils.getBoolean(biomePlacement, "canSpawnInBiome", false);
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
				Feature feature = ForgeRegistries.FEATURES.getValue(new ResourceLocation((String) entry.get("feature")));

				if (feature == null) {
					throw new NullPointerException("Invalid feature given in decorations!");
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

	private static SurfaceBuilderConfig getSurfaceConfig(Container properties) {
		Container surface = properties.getContainer("surface");

		if (surface == null) {
			return SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG;
		} else {
			BlockState topBlock = Utils.getBlock(surface.getStringValue("topBlock"), Blocks.GRASS_BLOCK).getDefaultState();
			BlockState fillerBlock = Utils.getBlock(surface.getStringValue("fillerBlock"), Blocks.DIRT).getDefaultState();
			BlockState underwaterBlock = Utils.getBlock(surface.getStringValue("underwaterBlock"), Blocks.GRAVEL).getDefaultState();

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
