package tk.valoeghese.zoesteria.core.genmodifierpack.biome;

import java.io.File;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.common.BiomeManager;
import tk.valoeghese.zoesteria.core.genmodifierpack.Utils;
import tk.valoeghese.zoesteriaconfig.api.ZoesteriaConfig;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.template.ConfigTemplate;

public final class BiomeFactory {
	public static Biome buildBiome(File file, String packId) {
		Container biomeConfig = ZoesteriaConfig.loadConfigWithDefaults(file, biomeDefaults);
		Container properties = biomeConfig.getContainer("properties");
		Container biomePlacement = biomeConfig.getContainer("biomePlacement");

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

		addGeneration(details, biomePlacement);

		return new ZoesteriaBiome(packId, id, propertiesBuilder, details);
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
	}

	private static SurfaceBuilderConfig getSurfaceConfig(Container properties) {
		Container surface = properties.getContainer("surface");
		if (surface == null) {
			return SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG;
		} else {
			BlockState topBlock = Utils.getBlock(properties.getStringValue("topBlock"), Blocks.GRASS_BLOCK).getDefaultState();
			BlockState fillerBlock = Utils.getBlock(properties.getStringValue("fillerBlock"), Blocks.DIRT).getDefaultState();
			BlockState underwaterBlock = Utils.getBlock(properties.getStringValue("underwaterBlock"), Blocks.GRAVEL).getDefaultState();

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
		Object2IntMap<BiomeManager.BiomeType> placement = new Object2IntArrayMap<>();
	}
}
