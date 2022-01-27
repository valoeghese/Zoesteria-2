package valoeghese.zoesteria.common.objects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import valoeghese.zoesteria.abstr.Soil;

public class ZoesteriaDoublePlantBlock extends DoublePlantBlock {
	public ZoesteriaDoublePlantBlock(Block.Properties properties, double height, Block.OffsetType offsetType, Soil soil) {
		super(properties);

		this.soil = soil;
		this.aabb = makeAABB(height > 16.0 ? height - 16.0 : height);
		this.offsetType = offsetType;
	}

	private final Soil soil;
	private static final VoxelShape AABB_LOW = makeAABB(16.0);
	private final VoxelShape aabb;
	private final Block.OffsetType offsetType;

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return state.getValue(HALF) == DoubleBlockHalf.UPPER ? this.aabb : AABB_LOW;
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter world, BlockPos pos) {
		return this.soil.isValidSoil(state);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
		if (state.getValue(HALF) != DoubleBlockHalf.UPPER) {
			return this.isValidBasePosition(worldIn, pos);
		} else {
			BlockState blockstate = worldIn.getBlockState(pos.below());
			if (state.getBlock() != this) return super.canSurvive(state, worldIn, pos); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
			return blockstate.getBlock() == this && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER;
		}
	}

	private final boolean isValidBasePosition(LevelReader world, BlockPos pos) {
		BlockPos blockpos = pos.below();
		return this.mayPlaceOn(world.getBlockState(blockpos), world, blockpos);
	}

	@Override
	public Block.OffsetType getOffsetType() {
		return this.offsetType;
	}

	private static VoxelShape makeAABB(double height) {
		return Block.box(2.0D, 0.0D, 2.0D, 14.0D, height, 14.0D);
	}
}
