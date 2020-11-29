package tk.valoeghese.zoesteria.common.biome;

import java.util.Optional;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.HeightWithChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeManager.BiomeType;
import tk.valoeghese.zoesteria.api.biome.BiomeDecorations;
import tk.valoeghese.zoesteria.api.biome.IBiomeProperties;
import tk.valoeghese.zoesteria.api.biome.IZoesteriaBiome;

public final class BluffBiome implements IZoesteriaBiome {
	@Override
	public String id() {
		return "bluff";
	}

	@Override
	public IBiomeProperties properties() {
		return IBiomeProperties.builder(Biome.Category.EXTREME_HILLS)
				.depth(0.9F)
				.scale(1.7F)
				.temperature(0.3F)
				.downfall(0.6F)
				.fillerBlock("minecraft:stone")
				.build();
	}

	@Override
	public void addPlacement(Object2IntMap<BiomeType> biomePlacement) {
		biomePlacement.put(BiomeType.COOL, 20);
	}

	@Override
	public Optional<Integer> customSkyColour() {
		return Optional.empty();
	}

	@Override
	public Optional<String> getRiver() {
		return Optional.of("zoesteria:bluff");
	}

	@Override
	public boolean canSpawnInBiome() {
		return true;
	}

	@Override
	public BiomeDecorations getDecorations() {
		return BiomeDecorations.create()
				.addDecoration(Decoration.VEGETAL_DECORATION, Feature.NORMAL_TREE
						.withConfiguration(DefaultBiomeFeatures.OAK_TREE_CONFIG)
						.withPlacement(Placement.COUNT_CHANCE_HEIGHTMAP.configure(new HeightWithChanceConfig(1, 0.1f))));
	}
}
