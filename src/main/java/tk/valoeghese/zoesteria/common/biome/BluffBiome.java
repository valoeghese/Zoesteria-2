package tk.valoeghese.zoesteria.common.biome;

import java.util.Optional;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeManager.BiomeType;
import tk.valoeghese.zoesteria.api.biome.BiomeDecorations;
import tk.valoeghese.zoesteria.api.biome.BiomeDefaultFeatures;
import tk.valoeghese.zoesteria.api.biome.IBiomeProperties;
import tk.valoeghese.zoesteria.api.biome.IZoesteriaBiome;
import tk.valoeghese.zoesteria.common.ZoesteriaCommonEventHandler;
import tk.valoeghese.zoesteria.common.objects.ZoesteriaBlocks;

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
		BiomeDecorations decorations = BiomeDecorations.create()
				.addDecoration(Decoration.VEGETAL_DECORATION, ZoesteriaCommonEventHandler.BLUFF_PINE
						.withConfiguration(new TreeFeatureConfig.Builder(
								new SimpleBlockStateProvider(
										Blocks.SPRUCE_LOG.getDefaultState()),
								new SimpleBlockStateProvider(ZoesteriaBlocks.BLUFF_PINE_LEAVES.get().getDefaultState()),
								new BlobFoliagePlacer(1, 1))
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
		BiomeDefaultFeatures.addStoneVariants(decorations);
		BiomeDefaultFeatures.addSparseGrass(decorations);
		return decorations;
	}
}
