package tk.valoeghese.zoesteria.common.feature;

import java.util.Random;
import java.util.Set;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

public class LolipopFeature extends AbstractTreeFeature<TreeFeatureConfig> {
	public LolipopFeature() {
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

		// trunk height thus indicates the height of the bare trunk, and the rest is foliage-covered
		final int trunkHeight = height - (config.trunkHeight + rand.nextInt(config.trunkHeightRandom + 1));

		// trunk top offset in the config thus indicates the amount of bare foliage at the end
		final int foliageDepth = height - (config.trunkTopOffset + rand.nextInt(config.trunkTopOffsetRandom + 1));

		pos.setPos(start);

		if (isSoil(world, start.down(), null)) {
			// this code was adapted from an old mod I wrote last year
			for (int yOff = 0; yOff <= height; ++yOff) {
				pos.setY(startY + yOff);

				if (yOff == height) {
					this.setLeaf(world, rand, pos, leaves, box, config);
				} else if (yOff == height - 1 || yOff == 0) {
					this.plusShape(world, rand, pos, leaves, box, startX, startZ, config);
				} else {
					for (int xOff = -1; xOff < 2; ++xOff) {
						pos.setX(startX + xOff);

						for (int zOff = -1; zOff < 2; ++zOff) {
							pos.setZ(startZ + zOff);

							if(xOff != 0 && zOff !=0) {
								this.setLeaf(world, rand, pos, leaves, box, config);
							}
						}
					}

					if (yOff == 1 || yOff == height - 2) {
						generator.setBlock(origin.add(0, yOff, -2), LEAVES, true); // It happened here once
						// appears to crash either with leaves or at a certain distance away? :thonkjang:
						generator.setBlock(origin.add(0, yOff, 2), LEAVES, true);
						generator.setBlock(origin.add(-2, yOff, 0), LEAVES, true);
						generator.setBlock(origin.add(2, yOff, 0), LEAVES, true);
					}
				}
			}

			for (int i = 0; i < height; ++i) {
				generator.setBlock(pos.add(0, i, 0), LOG, false);
			}

			return true;
		}

		return false;
	}

	private void plusShape(IWorldGenerationReader world, Random rand, Mutable pos, Set<BlockPos> leaves, MutableBoundingBox box, int startX, int startZ, int offset, TreeFeatureConfig config) {
		pos.setZ(startZ - offset);
		this.setLeaf(world, rand, pos, leaves, box, config);
		pos.setZ(startZ + offset);
		this.setLeaf(world, rand, pos, leaves, box, config);
		pos.setZ(startZ);

		pos.setX(startX - offset);
		this.setLeaf(world, rand, pos, leaves, box, config);
		pos.setX(startX - offset);
		this.setLeaf(world, rand, pos, leaves, box, config);
		pos.setX(startX);
	}
}
