package tk.valoeghese.zoesteria.common.feature;

import java.util.Random;

import net.minecraft.block.LogBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;

public class FallenLogFeature extends Feature<TreeLikeFeatureConfig> {
	public FallenLogFeature() {
		super(TreeLikeFeatureConfig::deserialize);
	}

	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos start, TreeLikeFeatureConfig config) {
		final int size = config.baseSize + rand.nextInt(config.sizeRandom + 1);
		final Direction direction = Direction.byHorizontalIndex(rand.nextInt(4));
		final int xoff = direction.getXOffset();
		final int zoff = direction.getZOffset();
		final int startX = start.getX();
		final int startZ = start.getZ();
		final Axis axis = direction.getAxis();

		BlockPos.Mutable pos = new BlockPos.Mutable(start);

		// start at start and increase at the end of each time. So 1-size instead of 0-(size-1)
		for (int i = 1; i <= size; ++i) {
			this.setBlockState(world, pos, config.logProvider.getBlockState(rand, pos).with(LogBlock.AXIS, axis));
			pos.setX(xoff * i + startX);
			pos.setZ(zoff * i + startZ);
		}

		return true;
	}
}
