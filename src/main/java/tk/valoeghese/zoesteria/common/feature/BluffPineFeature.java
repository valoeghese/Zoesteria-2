package tk.valoeghese.zoesteria.common.feature;

import java.util.Random;
import java.util.Set;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

public class BluffPineFeature extends AbstractTreeFeature<TreeFeatureConfig> {
	public BluffPineFeature() {
		super(TreeFeatureConfig::deserializeFoliage);
	}

	@Override
	protected boolean place(IWorldGenerationReader world, Random rand, BlockPos pos, Set<BlockPos> logs, Set<BlockPos> leaves, MutableBoundingBox box, TreeFeatureConfig config) {
		final int startY = pos.getY();
		final int height = config.baseHeight + rand.nextInt(config.heightRandA) + rand.nextInt(config.heightRandB);

		// check height
		if (startY < 1 || startY + height >= world.getMaxHeight()) {
			return false;
		}

		BlockPos.Mutable mutablePos = new BlockPos.Mutable(pos);
		final int startX = pos.getX();
		final int startZ = pos.getZ();
		
		// trunk height thus indicates the height of the bare trunk, and the rest is foliage-covered
		final int trunkHeight = height - (config.trunkHeight + rand.nextInt(config.trunkHeightRandom));

		// trunk top offset in the config thus indicates the amount of bare foliage at the end
		final int foliageDepth = height - (config.trunkTopOffset + rand.nextInt(config.trunkTopOffsetRandom));

		// final xo
		int fxo = 0;
		// final zo
		int fzo = 0;

		// test if it can generate and set fxo/fzo where possible
		for (int yo = 0; yo < trunkHeight; ++yo) {
			mutablePos.setY(startY + yo);

			if (!canBeReplacedByLogs(world, mutablePos)) {
				return false;
			}

			if (yo == 2) {
				// epic leaning direction code
				BlockPos.Mutable mutablePos2 = new BlockPos.Mutable(mutablePos);
				int count = 0;

				for (int xo = -2; xo <= 2; ++xo) {
					mutablePos2.setX(startX + xo);

					for (int zo = -2; zo <= 2; ++zo) {
						mutablePos2.setZ(startZ + zo);

						if (!canBeReplacedByLogs(world, mutablePos2)) {
							fxo += xo;
							fzo += zo;
							count++;
						}
					}
				}

				fxo /= count;
				fzo /= count;
				int tempOldFxo = fxo;

				fxo = fzo > fxo ? 0 : signum(fxo);
				fzo = tempOldFxo > fzo ? 0 : signum(fzo);
			}
		}

		mutablePos.setY(startY - 1);

		// check gen conditions

		if (isSoil(world, mutablePos, null) || world.hasBlockState(mutablePos, state -> state.getBlock() == Blocks.MOSSY_COBBLESTONE)) {
			final int foliageStart = height - foliageDepth;

			for (int yo = foliageStart; yo <= height; ++yo) {
				mutablePos.setY(startY + yo);

				if (yo < trunkHeight) {
					switch (yo - foliageStart) { // yo - (foliageStart - 1) - 1
					case 0:
						if (height > 11 && (yo < foliageStart + 3)) {
							this.growBigLeaves1(world, rand, mutablePos, startX, startZ, leaves, box, config);
						} else {
							this.growBigLeaves2(world, rand, mutablePos, startX, startZ, leaves, box, config);
						}
						break;
					case 2:
						if (rand.nextBoolean()) {
							this.growMediumLeaves(world, rand, mutablePos, startX, startZ, leaves, box, config); // "small leaves 1"
						} else {
							this.growSmallLeaves(world, rand, mutablePos, startX, startZ, leaves, box, config); // "small leaves 2"
						}
						break;
					default:
						this.growTinyLeaves(world, rand, mutablePos, startX, startZ, leaves, box, config); // "small leaves 3"
						break;
					}
				} else if (yo < height) {
					switch ((height - yo) & 0b1) {
					case 0:
						this.growSmallLeaves(world, rand, mutablePos, startX, startZ, leaves, box, config);
						break;
					case 1:
						this.growTinyLeaves(world, rand, mutablePos, startX, startZ, leaves, box, config);
						break;
					}
				} else {
					this.setLeaf(world, rand, mutablePos, leaves, box, config);
				}

				mutablePos.setX(startX);
				mutablePos.setZ(startZ);
			}

			for (int yo = 0; yo <= trunkHeight; ++yo) {
				mutablePos.setY(startY + yo);
				this.setLog(world, rand, mutablePos, logs, box, config);
			}

			return true;
		} else {
			return false;
		}
	}

	private void growBigLeaves1(IWorldGenerationReader world, Random rand, Mutable mutablePos, int startX, int startZ, Set<BlockPos> leaves, MutableBoundingBox box, TreeFeatureConfig config) {
		this.growBigLeaves2(world, rand, mutablePos, startX, startZ, leaves, box, config);
		
		// apparently "outer x, outer z"
		for (int lilScumbag = -1; lilScumbag <= 1; ++lilScumbag) {
			// x 3
			mutablePos.setZ(startZ + lilScumbag);
			mutablePos.setX(startX + 3);
			this.setLeaf(world, rand, mutablePos, leaves, box, config);
			mutablePos.setX(startX - 3);
			this.setLeaf(world, rand, mutablePos, leaves, box, config);

			// z 3
			mutablePos.setX(startX + lilScumbag);
			mutablePos.setZ(startZ + 3);
			this.setLeaf(world, rand, mutablePos, leaves, box, config);
			mutablePos.setZ(startZ - 3);
			this.setLeaf(world, rand, mutablePos, leaves, box, config);
		}

		// 2-out diagonals
		mutablePos.setZ(startZ + 2);
		mutablePos.setX(startX + 2);
		this.setLeaf(world, rand, mutablePos, leaves, box, config);
		mutablePos.setX(startX - 2);
		this.setLeaf(world, rand, mutablePos, leaves, box, config);

		mutablePos.setX(startX - 2);
		mutablePos.setZ(startZ + 2);
		this.setLeaf(world, rand, mutablePos, leaves, box, config);
		mutablePos.setZ(startZ - 2);
		this.setLeaf(world, rand, mutablePos, leaves, box, config);
	}

	private void growBigLeaves2(IWorldGenerationReader world, Random rand, Mutable mutablePos, int startX, int startZ, Set<BlockPos> leaves, MutableBoundingBox box, TreeFeatureConfig config) {
		// I am too tired at like nearly 12 am to make a proper for loop to replace what I did in 1.12
		// I mean this is still 10x better because I don't do new BlockPos twice a blockset

		// inner, outer
		for (int i = 1; i <= 2; ++i) {
			mutablePos.setX(startX - i);
			this.setLeaf(world, rand, mutablePos, leaves, box, config);
			mutablePos.setX(startX + i);
			this.setLeaf(world, rand, mutablePos, leaves, box, config);
			mutablePos.setX(startX);
			mutablePos.setZ(startZ - i);
			this.setLeaf(world, rand, mutablePos, leaves, box, config);
			mutablePos.setZ(startZ + i);
			this.setLeaf(world, rand, mutablePos, leaves, box, config);
			mutablePos.setZ(startZ);
		}

		// knight's move z
		mutablePos.setZ(startZ + 2);
		mutablePos.setX(startX - 1);
		this.setLeaf(world, rand, mutablePos, leaves, box, config);
		mutablePos.setX(startX + 1);
		this.setLeaf(world, rand, mutablePos, leaves, box, config);

		mutablePos.setZ(startZ - 2);
		mutablePos.setX(startX - 1);
		this.setLeaf(world, rand, mutablePos, leaves, box, config);
		mutablePos.setX(startX + 1);
		this.setLeaf(world, rand, mutablePos, leaves, box, config);

		// knight's move x
		mutablePos.setZ(startZ + 1);
		mutablePos.setX(startX - 2);
		this.setLeaf(world, rand, mutablePos, leaves, box, config);
		mutablePos.setX(startX + 2);
		this.setLeaf(world, rand, mutablePos, leaves, box, config);

		mutablePos.setZ(startZ - 1);
		mutablePos.setX(startX - 2);
		this.setLeaf(world, rand, mutablePos, leaves, box, config);
		mutablePos.setX(startX + 2);
		this.setLeaf(world, rand, mutablePos, leaves, box, config);
	}

	private void growMediumLeaves(IWorldGenerationReader world, Random rand, Mutable mutablePos, int startX, int startZ, Set<BlockPos> leaves, MutableBoundingBox box, TreeFeatureConfig config) {
		for (int xo = -2; xo <= 2; ++xo) {
			final int x = startX + xo;
			mutablePos.setX(x);

			final int zRange = 2 - MathHelper.abs(xo);
			final int endZ = startZ + zRange;

			for (int z = startZ - zRange; z <= endZ; ++z) {
				mutablePos.setZ(z);

				if (x != startX || z != startZ) {
					this.setLeaf(world, rand, mutablePos, leaves, box, config);
				}
			}
		}
	}

	private void growSmallLeaves(IWorldGenerationReader world, Random rand, Mutable mutablePos, int startX, int startZ, Set<BlockPos> leaves, MutableBoundingBox box, TreeFeatureConfig config) {
		final int endX = startX + 2;
		final int endZ = startZ + 2;

		for (int x = startX - 1; x < endX; ++x) {
			mutablePos.setX(x);

			for (int z = startZ - 1; z < endZ; ++z) {
				mutablePos.setZ(z);

				if (x != startX || z != startZ) {
					this.setLeaf(world, rand, mutablePos, leaves, box, config);
				}
			}
		}
	}

	private void growTinyLeaves(IWorldGenerationReader world, Random rand, Mutable mutablePos, int startX, int startZ, Set<BlockPos> leaves, MutableBoundingBox box, TreeFeatureConfig config) {
		mutablePos.setX(startX - 1);
		this.setLeaf(world, rand, mutablePos, leaves, box, config);
		mutablePos.setX(startX + 1);
		this.setLeaf(world, rand, mutablePos, leaves, box, config);
		mutablePos.setX(startX);
		mutablePos.setZ(startZ - 1);
		this.setLeaf(world, rand, mutablePos, leaves, box, config);
		mutablePos.setZ(startZ + 1);
		this.setLeaf(world, rand, mutablePos, leaves, box, config);
	}

	private static int signum(int x) {
		if (x == 0) {
			return 0;
		} else {
			return x > 0 ? 1 : -1;
		}
	}
}
