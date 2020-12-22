package tk.valoeghese.zoesteria.common.biome;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.ImmutableList;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager.BiomeType;
import tk.valoeghese.zoesteria.api.biome.BiomeDecorations;
import tk.valoeghese.zoesteria.api.biome.BiomeDefaultFeatures;
import tk.valoeghese.zoesteria.api.biome.IBiomeProperties;
import tk.valoeghese.zoesteria.api.biome.IBiome;
import tk.valoeghese.zoesteria.api.biome.SpawnEntry;
import tk.valoeghese.zoesteria.common.NoneFoliagePlacer;
import tk.valoeghese.zoesteria.common.Zoesteria;
import tk.valoeghese.zoesteria.common.ZoesteriaCommonEventHandler;
import tk.valoeghese.zoesteria.common.objects.ZoesteriaBlocks;

public final class Bluff implements IBiome {
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
				.topBlock("zoesteria:overgrown_stone")
				.fillerBlock("minecraft:stone")
				.surfaceBuilder("zoesteria:bluff")
				.build();
	}

	@Override
	public void addPlacement(Object2IntMap<BiomeType> biomePlacement) {
		biomePlacement.put(BiomeType.COOL, 5);
	}

	@Override
	public boolean canSpawnInBiome() {
		return true;
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
	public List<Type> biomeTypes() {
		return ImmutableList.of(
				Type.OVERWORLD,
				Type.CONIFEROUS,
				Type.RARE,
				Type.MOUNTAIN,
				Zoesteria.AMPLIFIED);
	}

	@Override
	public BiomeDecorations getDecorations() {
		BiomeDecorations decorations = BiomeDecorations.create()
				.addDecoration(Decoration.VEGETAL_DECORATION, ZoesteriaCommonEventHandler.BLUFF_PINE
						.withConfiguration(new TreeFeatureConfig.Builder(
								new SimpleBlockStateProvider(
										Blocks.SPRUCE_LOG.getDefaultState()),
								new SimpleBlockStateProvider(ZoesteriaBlocks.BLUFF_PINE_LEAVES.get().getDefaultState()),
								new NoneFoliagePlacer())
								.baseHeight(11)
								.heightRandA(3)
								.heightRandB(4)
								.trunkTopOffset(1)
								.trunkTopOffsetRandom(0)
								.trunkHeight(1)
								.trunkHeightRandom(0)
								.build())
						.withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(3, 0.1f, 1))))
				.addDecoration(Decoration.SURFACE_STRUCTURES, ZoesteriaCommonEventHandler.BLUFF_RUINS
						.withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG)
						.withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(420))));

		BiomeDefaultFeatures.addOres(decorations);
		BiomeDefaultFeatures.addLessSedimentDisks(decorations);
		BiomeDefaultFeatures.addStoneVariants(decorations);
		BiomeDefaultFeatures.addGrass(decorations, 1);
		return decorations;
	}

	@Override
	public Optional<String> getRiverBiome() {
		return Optional.of("zoesteria:bluff");
	}
}
