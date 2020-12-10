package tk.valoeghese.zoesteria.common.biome;

import java.util.Optional;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeManager.BiomeType;
import tk.valoeghese.zoesteria.api.biome.BiomeDecorations;
import tk.valoeghese.zoesteria.api.biome.BiomeDefaultFeatures;
import tk.valoeghese.zoesteria.api.biome.IBiomeProperties;
import tk.valoeghese.zoesteria.api.biome.IZoesteriaBiome;

public class Woodlands implements IZoesteriaBiome {
	public Woodlands(String id, int tpc, float baseHeight, float heightVariation) {
		this.id = id;
		this.tpc = tpc;
		this.baseHeight = baseHeight;
		this.heightVariation = heightVariation;
	}

	private final String id;
	private final int tpc;
	private final float baseHeight;
	private final float heightVariation;

	@Override
	public String id() {
		return this.id;
	}

	@Override
	public IBiomeProperties properties() {
		return IBiomeProperties.builder(Biome.Category.FOREST)
				.depth(this.baseHeight)
				.scale(this.heightVariation)
				.temperature(0.5F)
				.downfall(0.68F)
				.build();
	}

	@Override
	public void addPlacement(Object2IntMap<BiomeType> biomePlacement) {
		biomePlacement.put(BiomeType.WARM, 10);
	}

	@Override
	public Optional<Integer> customSkyColour() {
		return Optional.empty();
	}

	@Override
	public Optional<String> getRiver() {
		return Optional.empty();
	}

	@Override
	public boolean canSpawnInBiome() {
		return true;
	}

	@Override
	public BiomeDecorations getDecorations() {
		// TODO make BiomeDecorations able to handle structures
		BiomeDecorations decorations = BiomeDecorations.create()
				.addDecoration(Decoration.VEGETAL_DECORATION,
						Feature.NORMAL_TREE
						.withConfiguration(DefaultBiomeFeatures.OAK_TREE_CONFIG)
						.withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(this.tpc, 0.1f, 5))));

		BiomeDefaultFeatures.addOres(decorations);
		BiomeDefaultFeatures.addStoneVariants(decorations);
		BiomeDefaultFeatures.addSparseGrass(decorations);
		return decorations;
	}

}
