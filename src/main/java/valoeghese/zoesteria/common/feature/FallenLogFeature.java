package valoeghese.zoesteria.common.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.chunk.ChunkGenerator;
import valoeghese.zoesteria.abstr.Proxy;

import java.util.Random;

public class FallenLogFeature extends OldStyleFeature<TreeLikeFeatureConfig> {
	public FallenLogFeature() {
		super(TreeLikeFeatureConfig.CODEC);
	}

	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator generator, Random rand, BlockPos start, TreeLikeFeatureConfig config) {
		final int size = config.baseSize + rand.nextInt(config.sizeRandom + 1);
		final Direction direction = Direction.from2DDataValue(rand.nextInt(4));
		final int xoff = direction.getStepX();
		final int zoff = direction.getStepZ();
		final int startX = start.getX();
		final int startZ = start.getZ();
		final Direction.Axis axis = direction.getAxis();

		MutableBlockPos pos = new MutableBlockPos().set(start);
		BlockPos down = start.below();

		if (world.isStateAtPosition(down, state -> state.canSustainPlant()Proxy.getInstance().canSustainPlant(world, down, Direction.UP))) {
			// start at start and increase at the end of each time. So 1-size instead of 0-(size-1)
			for (int i = 1; i <= size; ++i) {
				this.setBlock(world, pos, config.logProvider.getState(rand, pos).setValue(RotatedPillarBlock.AXIS, axis));
				pos.setX(xoff * i + startX);
				pos.setZ(zoff * i + startZ);
			}

			return true;
		} else {
			return false;
		}
	}
}
