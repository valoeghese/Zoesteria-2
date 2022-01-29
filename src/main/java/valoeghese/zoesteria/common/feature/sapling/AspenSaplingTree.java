package valoeghese.zoesteria.common.feature.sapling;

import java.util.Random;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import valoeghese.zoesteria.common.ZoesteriaFeatures;

public final class AspenSaplingTree extends Tree {
	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random var1, boolean var2) {
		return ZoesteriaFeatures.CONFIGURED_ASPEN;
	}
}
