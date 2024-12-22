package valoeghese.zoesteria.common.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;

import java.util.Random;

public abstract class AbstractTreeFeature<FC extends TreeFeatureConfig> extends OldStyleFeature<FC> {
	public AbstractTreeFeature(Codec<FC> pCodec) {
		super(pCodec);
	}

	/*
	// Recommended Generation Code:

	int height = this.getHeight(level, random, origin, config);

	if (!this.canPlace(level, random, origin, height, config)) return false;

	// Extra Prep Here

	if (this.isValidSoil(level, origin.below())) {
		// GEN CODE HERE
	}

	return false;
	*/

	protected int getHeight(WorldGenLevel level, Random rand, BlockPos origin, FC config) {
		return config.baseHeight + rand.nextInt(config.heightRandA + 1) + rand.nextInt(config.heightRandB + 1);
	}

	/**
	 * Verify, using the provided parameters, whether the given feature can generate here.
	 * @return whether the feature can generate here.
	 */
	protected boolean canPlace(WorldGenLevel level, BlockPos start, int height) {
		final int startY = start.getY();

		// check height
		if (startY <= level.getMinBuildHeight() || startY + height >= level.getMaxBuildHeight()) {
			return false;
		}

		return true;
	}

	// Test Stuff

	protected boolean isValidSoil(WorldGenLevel level, BlockPos pos) {
		return level.isStateAtPosition(pos, state -> BlockTags.DIRT.contains(state.getBlock()));
	}

	protected static boolean canBeReplaced(WorldGenLevel level, BlockPos pos) {
		return level.isStateAtPosition(pos, state -> {
			// air, leaves, water, and replaceable plants can be replaced.
			return state.isAir()
					|| state.is(BlockTags.LEAVES)
					|| state.is(Blocks.WATER)
					|| state.is(BlockTags.REPLACEABLE_PLANTS);
		});
	}

	// Gen Stuff

	protected void setLeaf(WorldGenLevel world, Random rand, BlockPos.MutableBlockPos pos, TreeFeatureConfig config) {
		world.setBlock(pos, config.leavesProvider.getState(rand, pos), 19);
	}

	protected void setLog(WorldGenLevel world, Random rand, BlockPos.MutableBlockPos pos, TreeFeatureConfig config) {
		world.setBlock(pos, config.logProvider.getState(rand, pos), 19);
	}
}
