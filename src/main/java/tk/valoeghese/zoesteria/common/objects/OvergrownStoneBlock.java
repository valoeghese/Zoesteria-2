package tk.valoeghese.zoesteria.common.objects;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowyDirtBlock;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.lighting.LightEngine;
import net.minecraft.world.server.ServerWorld;

public class OvergrownStoneBlock extends SnowyDirtBlock/* implements IGrowable*/ {
	public OvergrownStoneBlock(Block.Properties properties) {
		super(properties);

	}

	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		if (!checkLight(state, world, pos)) {
			if (!world.isAreaLoaded(pos, 3)) {
				return;
			}

			world.setBlockState(pos, Blocks.STONE.getDefaultState());
		} else {
			BlockState ourState = this.getDefaultState();
			BlockState grassState = Blocks.GRASS.getDefaultState();

			if (world.getLight(pos.up()) >= 9) {
				for(int i = 0; i < 4; ++i) {
					BlockPos blockpos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
					Block blocc = world.getBlockState(blockpos).getBlock();
					boolean stone = blocc == Blocks.STONE;
					boolean dirt = blocc == Blocks.DIRT;

					if (stone) {
						// spreading to stone is 3x rarer.
						if (rand.nextInt(3) == 0 && checkFluidAndLight(ourState, world, blockpos)) {
							world.setBlockState(blockpos, ourState);
						}
					} else if (dirt) {
						if (checkFluidAndLight(ourState, world, blockpos)) {
							world.setBlockState(blockpos, grassState);
						}
					}
				}
			}

		}
	}

	private static boolean checkFluidAndLight(BlockState state, IWorldReader worldReader, BlockPos pos) {
		BlockPos upPos = pos.up();
		return checkLight(state, worldReader, pos) && !worldReader.getFluidState(upPos).isTagged(FluidTags.WATER);
	}

	private static boolean checkLight(BlockState state, IWorldReader worldReader, BlockPos pos) {
		BlockPos upPos = pos.up();
		BlockState upState = worldReader.getBlockState(upPos);
		int lightLevel = LightEngine.func_215613_a(worldReader, state, pos, upState, upPos, Direction.UP, upState.getOpacity(worldReader, upPos));
		return lightLevel < worldReader.getMaxLightLevel();
	}
}