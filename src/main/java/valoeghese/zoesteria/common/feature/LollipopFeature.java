package valoeghese.zoesteria.common.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

import java.util.Random;

import static net.minecraft.world.level.levelgen.feature.TreeFeature.validTreePos;

public class LollipopFeature extends OldStyleFeature<TreeFeatureConfig> {
	public LollipopFeature() {
		super(TreeFeatureConfig.CODEC);
	}

	@Override
	protected boolean place(WorldGenLevel world, ChunkGenerator generator, Random rand, BlockPos start, TreeFeatureConfig config) {
		final int startY = start.getY();
		int height = config.baseHeight + rand.nextInt(config.heightRandA + 1) + rand.nextInt(config.heightRandB + 1);

		// check height
		if (startY <= world.getMinBuildHeight() || startY + height >= world.getMaxBuildHeight()) {
			return false;
		}

		final int startX = start.getX();
		final int startZ = start.getZ();
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos().set(start);

		for (int yo = 0; yo < height; ++yo) {
			pos.setY(startY + yo);

			if (!validTreePos(world, pos)) {
				return false;
			}
		}

		// trunk height thus indicates the height of the bare trunk, and the rest is foliage-covered.
		final int foliageStart = 1 + (config.trunkHeight + rand.nextInt(config.trunkHeightRandom + 1));

		if (foliageStart > height) {
			return false;
		}

		// trunk top offset in the config thus indicates the amount of bare foliage at the end
		final int trunkEnd = height - (config.trunkTopOffset + rand.nextInt(config.trunkTopOffsetRandom + 1));

		pos.set(start);

		if (isSoil(world, start.below())) {
			// this code was adapted from an old mod I wrote last year
			final int largeStart = foliageStart + 2;
			final int largeEnd = height - 2;

			for (int yOff = foliageStart; yOff <= height; ++yOff) {
				pos.setY(startY + yOff);

				if (yOff == height) {
					this.setLeaf(world, rand, pos, config);
				} else if (yOff == height - 1 || yOff == foliageStart) {
					this.plusShape(world, rand, pos, startX, startZ, 1, config);
				} else {
					for (int xOff = -1; xOff < 2; ++xOff) {
						pos.setX(startX + xOff);

						for (int zOff = -1; zOff < 2; ++zOff) {
							pos.setZ(startZ + zOff);

							this.setLeaf(world, rand, pos, config);
						}
					}

					if (yOff >= largeStart && yOff < largeEnd) {
						this.plusShape(world, rand, pos, startX, startZ, 2, config);
					}
				}
			}

			pos.setX(startX);
			pos.setZ(startZ);

			for (int yOff = 0; yOff < trunkEnd; ++yOff) {
				pos.setY(startY + yOff);
				this.setLog(world, rand, pos, config);
			}

			return true;
		}

		return false;
	}

	private boolean isSoil(WorldGenLevel world, BlockPos below) {
		return world.isStateAtPosition(below, state -> BlockTags.DIRT.contains(state.getBlock()));
	}

	private void plusShape(WorldGenLevel world, Random rand, BlockPos.MutableBlockPos pos, int startX, int startZ, int offset, TreeFeatureConfig config) {
		pos.setX(startX);
		pos.setZ(startZ - offset);
		this.setLeaf(world, rand, pos, config);
		pos.setZ(startZ + offset);
		this.setLeaf(world, rand, pos, config);
		pos.setZ(startZ);

		pos.setX(startX - offset);
		this.setLeaf(world, rand, pos, config);
		pos.setX(startX + offset);
		this.setLeaf(world, rand, pos, config);
		pos.setX(startX);
		this.setLeaf(world, rand, pos, config);
	}

	private void setLeaf(WorldGenLevel world, Random rand, BlockPos.MutableBlockPos pos, TreeFeatureConfig config) {
		world.setBlock(pos, config.leavesProvider.getState(rand, pos), 19);
	}

	private void setLog(WorldGenLevel world, Random rand, BlockPos.MutableBlockPos pos, TreeFeatureConfig config) {
		world.setBlock(pos, config.logProvider.getState(rand, pos), 19);
	}
}
