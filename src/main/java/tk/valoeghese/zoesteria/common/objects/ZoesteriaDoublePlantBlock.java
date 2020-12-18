package tk.valoeghese.zoesteria.common.objects;

import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.PlantType;

public class ZoesteriaDoublePlantBlock extends DoublePlantBlock {
	public ZoesteriaDoublePlantBlock(Block.Properties properties, double height, Block.OffsetType offsetType, @Nullable Predicate<Block> canSurviveOn) {
		super(properties);

		this.canSurviveOn = canSurviveOn;
		this.aabb = makeAABB(height > 16.0 ? height - 16.0 : height);
		this.offsetType = offsetType;
	}

	@Nullable
	private final Predicate<Block> canSurviveOn;
	private static final VoxelShape AABB_LOW = makeAABB(16.0);
	private final VoxelShape aabb;
	private final Block.OffsetType offsetType;

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return state.get(HALF) == DoubleBlockHalf.UPPER ? this.aabb : AABB_LOW;
	}

	@Override
	protected boolean isValidGround(BlockState state, IBlockReader world, BlockPos pos) {
		return this.canSurviveOn == null ? super.isValidGround(state, world, pos) : this.canSurviveOn.test(state.getBlock());
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		if (state.get(HALF) != DoubleBlockHalf.UPPER) {
			return this.isValidBasePosition(state, worldIn, pos);
		} else {
			BlockState blockstate = worldIn.getBlockState(pos.down());
			if (state.getBlock() != this) return super.isValidPosition(state, worldIn, pos); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
			return blockstate.getBlock() == this && blockstate.get(HALF) == DoubleBlockHalf.LOWER;
		}
	}

	private boolean isValidBasePosition(BlockState state, IWorldReader world, BlockPos pos) {
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
