package tk.valoeghese.zoesteria.common.feature;

import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import tk.valoeghese.zoesteria.common.ZoesteriaCommonEventHandler;
import tk.valoeghese.zoesteria.common.objects.ZoesteriaBlocks;

public class BluffPineSaplingTree extends Tree {
	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean p_225546_2_) {
		// TODO data generated
		return ZoesteriaCommonEventHandler.BLUFF_PINE_SAPLING.withConfiguration(new TreeFeatureConfig.Builder(
				new SimpleBlockStateProvider(
						Blocks.SPRUCE_LOG.getDefaultState()),
				new SimpleBlockStateProvider(ZoesteriaBlocks.BLUFF_PINE_LEAVES.get().getDefaultState()),
				new BlobFoliagePlacer(1, 1)) // unused
				.baseHeight(11)
				.heightRandA(4)
				.heightRandB(5)
				.trunkTopOffset(1)
				.trunkTopOffsetRandom(1)
				.trunkHeight(1)
				.trunkHeightRandom(1)
				.build());
	}
}
