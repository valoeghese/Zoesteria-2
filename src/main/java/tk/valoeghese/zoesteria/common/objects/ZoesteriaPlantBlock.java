package tk.valoeghese.zoesteria.common.objects;

import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class ZoesteriaPlantBlock extends BushBlock {
	public ZoesteriaPlantBlock(Block.Properties properties, double height, @Nullable Predicate<Block> canSurviveOn) {
		super(properties);

		this.canSurviveOn = canSurviveOn;
		this.aabb = makeAABB(height);
	}

	private final Predicate<Block> canSurviveOn;
	private final VoxelShape aabb;

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return this.aabb;
	}

	@Override
	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return this.canSurviveOn == null ? super.isValidGround(state, worldIn, pos) : this.canSurviveOn.test(state.getBlock());
	}

	@Override
	public Block.OffsetType getOffsetType() {
		return Block.OffsetType.XYZ;
	}

	private static VoxelShape makeAABB(double height) {
		return Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, height, 14.0D);
	}
}
