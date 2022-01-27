package valoeghese.zoesteria.common.biome;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.entity.EntityType;

import valoeghese.zoesteria.abstr.biome.ZoesteriaBiome;

import valoeghese.zoesteria.abstr.biome.BiomeProperties;
import valoeghese.zoesteria.abstr.biome.BiomeType;
import valoeghese.zoesteria.abstr.biome.SpawnEntry;

public final class Bluff implements ZoesteriaBiome {
	@Override
	public BiomeProperties properties() {
		return new BiomeProperties.Builder(BiomeCategory.EXTREME_HILLS)
				.temperature(0.3F)
				.rainfall(0.6F)
//				.topBlock("zoesteria:overgrown_stone")
//				.fillerBlock("minecraft:stone")
//				.surfaceBuilder("zoesteria:bluff")
				.build();
	}

	@Override
	public List<SpawnEntry> mobSpawns() {
		return ImmutableList.of(
				new SpawnEntry(EntityType.CHICKEN).spawnWeight(3).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.COW).spawnWeight(3).spawnGroupCount(2, 3),
				new SpawnEntry(EntityType.WOLF).spawnWeight(4).spawnGroupCount(2, 5),
				new SpawnEntry(EntityType.SHEEP).spawnWeight(10).spawnGroupCount(2, 5),
				new SpawnEntry(EntityType.RABBIT).spawnWeight(10).spawnGroupCount(2, 4),

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
	public List<BiomeType> biomeTypes() {
		return ImmutableList.of(
				BiomeType.CONIFEROUS,
				BiomeType.RARE,
				BiomeType.MOUNTAIN);
	}

//	@Override
//	public BiomeDecorations getDecorations() {
//		BiomeDecorations decorations = BiomeDecorations.create()
//				.addDecoration(Decoration.VEGETAL_DECORATION, ZoesteriaCommonEventHandler.BLUFF_PINE
//						.withConfiguration(new TreeFeatureConfig.Builder(
//								new SimpleBlockStateProvider(
//										Blocks.SPRUCE_LOG.getDefaultState()),
//								new SimpleBlockStateProvider(ZoesteriaBlocks.BLUFF_PINE_LEAVES.get().getDefaultState()),
//								new NoneFoliagePlacer())
//								.baseHeight(11)
//								.heightRandA(3)
//								.heightRandB(4)
//								.trunkTopOffset(1)
//								.trunkTopOffsetRandom(0)
//								.trunkHeight(1)
//								.trunkHeightRandom(0)
//								.build())
//						.withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(3, 0.1f, 1))))
//				.addDecoration(Decoration.SURFACE_STRUCTURES, ZoesteriaCommonEventHandler.BLUFF_RUINS
//						.withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG)
//						.withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(420))));
//
//		BiomeDefaultFeatures.addOres(decorations);
//		BiomeDefaultFeatures.addLessSedimentDisks(decorations);
//		BiomeDefaultFeatures.addStoneVariants(decorations);
//		BiomeDefaultFeatures.addGrass(decorations, 1);
//		return decorations;
//	}
}
