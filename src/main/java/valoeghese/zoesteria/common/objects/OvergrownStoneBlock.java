package valoeghese.zoesteria.common.objects;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LayerLightEngine;

public class OvergrownStoneBlock extends SnowyDirtBlock {
	public OvergrownStoneBlock(Block spreadsTo, Block.Properties properties) {
		super(properties);
		this.spreadsTo = spreadsTo;
	}

	private final Block spreadsTo;

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random rand) {
		if (!checkLight(state, world, pos)) {
			if (!world.isAreaLoaded(pos, 3)) {
				return;
			}

			world.setBlock(pos, this.spreadsTo.defaultBlockState(), 3);
		} else {
			BlockState ourState = this.defaultBlockState();

			if (world.getMaxLocalRawBrightness(pos.above()) >= 9) {
				for(int i = 0; i < 4; ++i) {
					BlockPos blockpos = pos.offset(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
					Block blocc = world.getBlockState(blockpos).getBlock();
					boolean stone = blocc == this.spreadsTo;
					boolean dirt = blocc == Blocks.DIRT;

					if (stone) {
						// spreading to stone is 3x rarer.
						if (rand.nextInt(3) == 0 && checkFluidAndLight(ourState, world, blockpos)) {
							world.setBlock(blockpos, ourState, 3);
						}
					} else if (dirt) {
						if (checkFluidAndLight(ourState, world, blockpos)) {
							world.setBlock(blockpos, Blocks.GRASS.defaultBlockState(), 3);
						}
					}
				}
			}

		}
	}

	private static boolean checkFluidAndLight(BlockState state, LevelReader worldReader, BlockPos pos) {
		BlockPos upPos = pos.above();
		return checkLight(state, worldReader, pos) && !worldReader.getFluidState(upPos).is(FluidTags.WATER);
	}

	private static boolean checkLight(BlockState state, LevelReader worldReader, BlockPos pos) {
		BlockPos upPos = pos.above();
		BlockState upState = worldReader.getBlockState(upPos);
		int lightLevel = LayerLightEngine.getLightBlockInto(worldReader, state, pos, upState, upPos, Direction.UP, upState.getLightBlock(worldReader, upPos));
		return lightLevel < worldReader.getMaxLightLevel();
	}
}