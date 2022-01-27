package valoeghese.zoesteria.common.objects;

import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.PlantType;
import valoeghese.zoesteria.abstr.Soil;

public class ZoesteriaPlantBlock extends BushBlock {
	public ZoesteriaPlantBlock(Block.Properties properties, double height, Block.OffsetType offsetType, Soil soil) {
		super(properties);

		this.soil = soil;
		this.aabb = makeAABB(height);
		this.offsetType = offsetType;
	}

	private final Soil soil;
	private final VoxelShape aabb;
	private final Block.OffsetType offsetType;

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return this.aabb;
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter world, BlockPos pos) {
		return this.soil.isValidSoil(state);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
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
