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
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

public class ZoesteriaPlantBlock extends BushBlock implements IPlantable {
	public ZoesteriaPlantBlock(Block.Properties properties, double height, Block.OffsetType offsetType, @Nullable Predicate<Block> canSurviveOn) {
		super(properties);

		this.canSurviveOn = canSurviveOn;
		this.aabb = makeAABB(height);
		this.offsetType = offsetType;
	}

	@Nullable
	private final Predicate<Block> canSurviveOn;
	private final VoxelShape aabb;
	private final Block.OffsetType offsetType;

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return this.aabb;
	}

	@Override
	protected boolean isValidGround(BlockState state, IBlockReader world, BlockPos pos) {
		return this.canSurviveOn == null ? super.isValidGround(state, world, pos) : this.canSurviveOn.test(state.getBlock());
	}

	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		BlockPos blockpos = pos.down();
		return this.isValidGround(world.getBlockState(blockpos), world, blockpos);
	}

	@Override
	public PlantType getPlantType(IBlockReader world, BlockPos pos) {
		return PlantType.Desert; // closest to what we want.. but we only want sand. still, this is closest.
	}

	@Override
	public Block.OffsetType getOffsetType() {
		return this.offsetType;
	}

	private static VoxelShape makeAABB(double height) {
		return Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, height, 14.0D);
	}
}
