package tk.valoeghese.zoesteria.common.biome;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager.BiomeType;
import tk.valoeghese.zoesteria.api.biome.BiomeDecorations;
import tk.valoeghese.zoesteria.api.biome.BiomeDefaultFeatures;
import tk.valoeghese.zoesteria.api.biome.IBiomeProperties;
import tk.valoeghese.zoesteria.api.biome.IZoesteriaBiome;
import tk.valoeghese.zoesteria.api.biome.SpawnEntry;
import tk.valoeghese.zoesteria.common.ZoesteriaCommonEventHandler;
import tk.valoeghese.zoesteria.common.feature.ShrubFeatureConfig;
import tk.valoeghese.zoesteria.common.objects.ZoesteriaBlocks;

public class AustralianOutback implements IZoesteriaBiome {
	public AustralianOutback(boolean plateau) {
		this.plateau = plateau;
	}

	private final boolean plateau;

	@Override
	public String id() {
		return this.plateau ? "uluru": "australian_outback";
	}

	@Override
	public IBiomeProperties properties() {
		IBiomeProperties.Builder builder = IBiomeProperties.builder(Category.DESERT)
				.depth(this.plateau ? 1.8f : 0.3f)
				.scale(this.plateau ? 0.04f : 0.11f)
				.temperature(1.6f)
				.downfall(0.1f)
				.fillerBlock("minecraft:red_sandstone")
				.underwaterBlock("minecraft:red_sand");

		if (this.plateau) {
			builder.surfaceBuilder("zoesteria:fill_to_sea_level").topBlock("minecraft:red_sandstone");
		} else {
			builder.surfaceBuilder("zoesteria:outback").topBlock("minecraft:red_sand");
		}

		return builder.build();
	}

	@Override
	public void addPlacement(Object2IntMap<BiomeType> biomePlacement) {
		if (!this.plateau) {
			biomePlacement.put(BiomeType.DESERT, 50); // 10 in game. 50 for testing.
			biomePlacement.put(BiomeType.COOL, 50); // 0 in game. 50 for testing.
			biomePlacement.put(BiomeType.ICY, 50); // 0 in game. 50 for testing.
			biomePlacement.put(BiomeType.WARM, 50); // 0 in game. 50 for testing.
		}
	}

	@Override
	public boolean canSpawnInBiome() {
		return true;
	}

	@Override
	public BiomeDecorations getDecorations() {
		BiomeDecorations result = BiomeDecorations.create()
				.addDecoration(Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(
						new BlockClusterFeatureConfig.Builder(
								new SimpleBlockStateProvider(ZoesteriaBlocks.BLUE_FLAX_LILY.get().getDefaultState()),
								new SimpleBlockPlacer()).tries(16).build()
						)
						.withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(1))))
				.addDecoration(Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(
						new BlockClusterFeatureConfig.Builder(
								new SimpleBlockStateProvider(ZoesteriaBlocks.SANDHILL_CANEGRASS.get().getDefaultState()),
								new SimpleBlockPlacer()).tries(this.plateau ? 16 : 32).build()
						)
						.withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(this.plateau ? 1 : 3))))
				.addDecoration(Decoration.VEGETAL_DECORATION, ZoesteriaCommonEventHandler.SIMPLE_SHRUB
						.withConfiguration(new ShrubFeatureConfig(
								new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState()), 
								new SimpleBlockStateProvider(Blocks.OAK_LEAVES.getDefaultState())))
						.withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(2, 0.1f, 2))));

		BiomeDefaultFeatures.addOres(result);
		BiomeDefaultFeatures.addSedimentDisks(result);
		BiomeDefaultFeatures.addStoneVariants(result);
		BiomeDefaultFeatures.addMushrooms(result, 1, 1);
		BiomeDefaultFeatures.addGrass(result, 2);

		if (this.plateau) {
			result.addStructure(Feature.PILLAGER_OUTPOST.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
		} else {
			result.addStructure(Feature.VILLAGE.withConfiguration(new VillageConfig("village/plains/town_centers", 3)));
		}
		return result;
	}

	@Override
	public List<Type> biomeTypes() {
		List<Type> result = Lists.newArrayList(
				Type.OVERWORLD,
				Type.DRY,
				Type.HOT,
				Type.SPARSE);

		if (this.plateau) {
			result.add(Type.PLATEAU);
		} else {
			result.add(Type.SANDY);
		}

		return result;
	}

	@Override
	public Optional<List<String>> getHillsBiomes() {
		if (this.plateau) {
			return Optional.empty();
		} else {
			return Optional.of(ImmutableList.of("zoesteria:australian_outback", "zoesteria:uluru"));
		}
	}

	@Override
	public List<SpawnEntry> mobSpawns() {
		return ImmutableList.of(
				new SpawnEntry(EntityType.HORSE).spawnWeight(1).spawnGroupCount(1, 3),
				new SpawnEntry(EntityType.RABBIT).spawnWeight(20).spawnGroupCount(2, 4),

				new SpawnEntry(EntityType.BAT).spawnWeight(10).spawnGroupCount(8, 8),

				new SpawnEntry(EntityType.SPIDER).spawnWeight(100).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.HUSK).spawnWeight(50).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.ZOMBIE).spawnWeight(45).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.ZOMBIE_VILLAGER).spawnWeight(5).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.SKELETON).spawnWeight(100).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.CREEPER).spawnWeight(100).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.SLIME).spawnWeight(100).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.ENDERMAN).spawnWeight(10).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.WITCH).spawnWeight(5).spawnGroupCount(2, 4));
	}
}
