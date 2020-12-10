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
		this(id, tpc, baseHeight, heightVariation, false);
	}

	public Woodlands(String id, int tpc, float baseHeight, float heightVariation, boolean subBiome) {
		this.id = id;
		this.tpc = tpc;
		this.baseHeight = baseHeight;
		this.heightVariation = heightVariation;
		this.high = this.baseHeight > 1.0f;
		this.subBiome = subBiome;
	}

	private final String id;
	private final int tpc;
	private final float baseHeight;
	private final float heightVariation;
	private final boolean high;
	private final boolean subBiome;

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
		if (!this.subBiome) {
			biomePlacement.put(BiomeType.WARM, this.high ? 7 : 8); // with both major variations, this adds up to 15. Rather common.
		}
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
						.withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(this.tpc, 0.1f, 7))));

		BiomeDefaultFeatures.addOres(decorations);
		BiomeDefaultFeatures.addStoneVariants(decorations);
		BiomeDefaultFeatures.addGrass(decorations, 5);
		return decorations;
	}
}
