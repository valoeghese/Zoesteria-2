package tk.valoeghese.zoesteria.common.feature.sapling;

import java.util.Random;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import tk.valoeghese.zoesteria.common.ZoesteriaCommonEventHandler;

public final class AspenSaplingTree extends Tree {
	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random var1, boolean var2) {
		return ZoesteriaCommonEventHandler.CONFIGURED_ASPEN;
	}
}
