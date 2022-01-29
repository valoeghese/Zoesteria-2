package valoeghese.zoesteria.common.feature.sapling;

import java.util.Random;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import valoeghese.zoesteria.common.ZoesteriaFeatures;

public class BluffPineSaplingTree extends Tree {
	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean p_225546_2_) {
		// TODO data driven
		return ZoesteriaFeatures.CONFIGURED_BLUFF_PINE_SAPLING;
	}
}
