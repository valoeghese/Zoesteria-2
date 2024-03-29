package valoeghese.zoesteria.common.biome;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager.BiomeType;
import tk.valoeghese.zoesteria.api.biome.BiomeDecorations;
import tk.valoeghese.zoesteria.api.biome.BiomeDefaultFeatures;
import tk.valoeghese.zoesteria.api.biome.IBiomeProperties;
import tk.valoeghese.zoesteria.api.biome.IBiome;
import tk.valoeghese.zoesteria.api.biome.SpawnEntry;
import valoeghese.zoesteria.common.ZoesteriaFeatures;
import valoeghese.zoesteria.common.feature.TreeLikeFeatureConfig;
import valoeghese.zoesteria.common.feature.TripleFeatureConfig;

public class Woodlands implements IBiome {
	public Woodlands(String id, int tpc, float baseHeight, float heightVariation) {
		this(id, tpc, baseHeight, heightVariation, false);
	}

	public Woodlands(String id, int tpc, float baseHeight, float heightVariation, boolean subBiome) {
		this.id = id;
		this.tpc = tpc;
		this.baseHeight = baseHeight;
		this.heightVariation = heightVariation;
		this.high = this.baseHeight > 1.0f;
		this.hills = subBiome;
	}

	private final String id;
	private final int tpc;
	private final float baseHeight;
	private final float heightVariation;
	private final boolean high;
	private final boolean hills;

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
				.surfaceBuilder("zoesteria:woodlands")
				.build();
	}

	@Override
	public void addPlacement(Object2IntMap<BiomeType> biomePlacement) {
		if (!this.hills) {
			biomePlacement.put(BiomeType.WARM, this.high ? 6 : 7); // with both major variations, this adds up to 13. Rather common.
		}
	}

	@Override
	public boolean canSpawnInBiome() {
		return true;
	}

	@Override
	public List<SpawnEntry> mobSpawns() {
		return ImmutableList.of(
				new SpawnEntry(EntityType.PIG).spawnWeight(12).spawnGroupCount(4, 4),
				new SpawnEntry(EntityType.CHICKEN).spawnWeight(10).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.RABBIT).spawnWeight(10).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.SHEEP).spawnWeight(8).spawnGroupCount(4, 4),
				new SpawnEntry(EntityType.WOLF).spawnWeight(4).spawnGroupCount(4, 4),

				new SpawnEntry(EntityType.BAT).spawnWeight(10).spawnGroupCount(8, 8),

				new SpawnEntry(EntityType.SPIDER).spawnWeight(100).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.ZOMBIE).spawnWeight(95).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.ZOMBIE_VILLAGER).spawnWeight(5).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.SKELETON).spawnWeight(100).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.CREEPER).spawnWeight(100).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.SLIME).spawnWeight(100).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.ENDERMAN).spawnWeight(10).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.WITCH).spawnWeight(5).spawnGroupCount(2, 4));
	}

	@Override
	public List<Type> biomeTypes() {
		List<Type> result = Lists.newArrayList(
				Type.OVERWORLD,
				Type.FOREST);

		if (this.high) {
			result.add(Type.PLATEAU);
		} else if (this.hills) {
			result.add(Type.HILLS);
		}

		return result;
	}

	@Override
	public BiomeDecorations getDecorations() {
		BiomeDecorations decorations = BiomeDecorations.create();

		BiomeDefaultFeatures.addWaterLakes(decorations, Blocks.WATER.getDefaultState(), 3);
		BiomeDefaultFeatures.addLavaLakes(decorations, Blocks.LAVA.getDefaultState(), 98);

		decorations.addDecoration(Decoration.VEGETAL_DECORATION,
				Feature.NORMAL_TREE
				.withConfiguration(
						new TreeFeatureConfig.Builder(
								new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState()),
								new SimpleBlockStateProvider(Blocks.OAK_LEAVES.getDefaultState()),
								new BlobFoliagePlacer(2, 1))
						.baseHeight(5)
						.heightRandA(2)
						.foliageHeight(3)
						.ignoreVines()
						.build()
						)
				.withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(this.tpc - 1, 0.1f, 7))))
		.addDecoration(Decoration.VEGETAL_DECORATION, ZoesteriaFeatures.TRIPLE_NOISE_SELECTOR
				.withConfiguration(new TripleFeatureConfig(
						Feature.FANCY_TREE.withConfiguration(DefaultBiomeFeatures.FANCY_TREE_CONFIG),
						ZoesteriaFeatures.CONFIGURED_ASPEN,
						Feature.NORMAL_TREE.withConfiguration(DefaultBiomeFeatures.PINE_TREE_CONFIG)))
				.withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(2, 0.1f, 1))))
		.addDecoration(Decoration.VEGETAL_DECORATION,
				ZoesteriaFeatures.FALLEN_LOG
				.withConfiguration(new TreeLikeFeatureConfig(
						new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState()),
						4, 
						3))
				.withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(1, 0.1f, 1))));

		BiomeDefaultFeatures.addOres(decorations);
		BiomeDefaultFeatures.addSedimentDisks(decorations);
		BiomeDefaultFeatures.addStoneVariants(decorations);
		BiomeDefaultFeatures.addGrass(decorations, 5);
		BiomeDefaultFeatures.addMushrooms(decorations, 2, 1);

		if (this.high) {
			decorations.addStructure(Feature.VILLAGE.withConfiguration(new VillageConfig("village/plains/town_centers", 4)));
		}

		return decorations;
	}

	@Override
	public Optional<List<String>> getHillsBiomes() {
		return (this.high || this.hills) ? Optional.empty() : Optional.of(ImmutableList.of("zoesteria:woodlands_hills"));
	}
}
