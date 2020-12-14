package tk.valoeghese.zoesteria.common.feature;

import java.util.Random;
import java.util.Set;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

public class LollipopFeature extends AbstractTreeFeature<TreeFeatureConfig> {
	public LollipopFeature() {
		super(TreeFeatureConfig::deserializeFoliage);
	}

	@Override
	protected boolean place(IWorldGenerationReader world, Random rand, BlockPos start, Set<BlockPos> logs, Set<BlockPos> leaves, MutableBoundingBox box, TreeFeatureConfig config) {
		final int startY = start.getY();
		int height = config.baseHeight + rand.nextInt(config.heightRandA + 1) + rand.nextInt(config.heightRandB + 1);

		// check height
		if (startY < 1 || startY + height >= world.getMaxHeight()) {
			return false;
		}

		final int startX = start.getX();
		final int startZ = start.getZ();
		BlockPos.Mutable pos = new BlockPos.Mutable(start);

		for (int yo = 0; yo < height; ++yo) {
			pos.setY(startY + yo);

			if (!canBeReplacedByLogs(world, pos)) {
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

		pos.setPos(start);

		if (isSoil(world, start.down(), null)) {
			// this code was adapted from an old mod I wrote last year
			final int largeStart = foliageStart + 2;
			final int largeEnd = height - 2;

			for (int yOff = foliageStart; yOff <= height; ++yOff) {
				pos.setY(startY + yOff);

				if (yOff == height) {
					this.setLeaf(world, rand, pos, leaves, box, config);
				} else if (yOff == height - 1 || yOff == foliageStart) {
					this.plusShape(world, rand, pos, leaves, box, startX, startZ, 1, config);
				} else {
					for (int xOff = -1; xOff < 2; ++xOff) {
						pos.setX(startX + xOff);

						for (int zOff = -1; zOff < 2; ++zOff) {
							pos.setZ(startZ + zOff);

							this.setLeaf(world, rand, pos, leaves, box, config);
						}
					}

					if (yOff >= largeStart && yOff < largeEnd) {
						this.plusShape(world, rand, pos, leaves, box, startX, startZ, 2, config);
					}
				}
			}

			pos.setX(startX);
			pos.setZ(startZ);

			for (int yOff = 0; yOff < trunkEnd; ++yOff) {
				pos.setY(startY + yOff);
				this.setLog(world, rand, pos, logs, box, config);
			}

			return true;
		}

		return false;
	}

	private void plusShape(IWorldGenerationReader world, Random rand, Mutable pos, Set<BlockPos> leaves, MutableBoundingBox box, int startX, int startZ, int offset, TreeFeatureConfig config) {
		pos.setX(startX);
		pos.setZ(startZ - offset);
		this.setLeaf(world, rand, pos, leaves, box, config);
		pos.setZ(startZ + offset);
		this.setLeaf(world, rand, pos, leaves, box, config);
		pos.setZ(startZ);

		pos.setX(startX - offset);
		this.setLeaf(world, rand, pos, leaves, box, config);
		pos.setX(startX + offset);
		this.setLeaf(world, rand, pos, leaves, box, config);
		pos.setX(startX);
		this.setLeaf(world, rand, pos, leaves, box, config);
	}
}
