package valoeghese.zoesteria.common.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;

import java.util.Random;

public class BluffPineFeature extends AbstractTreeFeature<TreeFeatureConfig> {
	private static final int DO_NOT_GENERATE = -1;

	public BluffPineFeature(boolean natGen) {
		super(TreeFeatureConfig.CODEC);
		this.natGen = natGen;
	}

	private final boolean natGen;

	@Override
	protected boolean place(WorldGenLevel world, ChunkGenerator generator, Random rand, BlockPos pos, TreeFeatureConfig config) {
		final int startY = pos.getY();
		int height = this.getHeight(world, rand, pos, config);

		if (!this.canPlace(world, pos, height)) {
			return false;
		}

		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos().set(pos);
		final int startX = pos.getX();
		final int startZ = pos.getZ();

		// trunk height thus indicates the height of the bare trunk, and the rest is foliage-covered
		final int trunkHeight = height - (config.trunkHeight + rand.nextInt(config.trunkHeightRandom + 1));

		// trunk top offset in the config thus indicates the amount of bare foliage at the end
		final int foliageDepth = height - (config.trunkTopOffset + rand.nextInt(config.trunkTopOffsetRandom + 1));

		// final xo
		int fxo = 0;
		// final zo
		int fzo = 0;

		// test if it can generate and set fxo/fzo where possible
		for (int yo = 0; yo < height; ++yo) {
			mutablePos.setY(startY + yo);

			if (!canBeReplaced(world, mutablePos)) {
				return false;
			}

			if (yo == 2) {
				// epic leaning direction code
				BlockPos.MutableBlockPos mutablePos2 = new BlockPos.MutableBlockPos().set(mutablePos);
				int count = 0;

				for (int xo = -2; xo <= 2; ++xo) {
					mutablePos2.setX(startX + xo);

					for (int zo = -2; zo <= 2; ++zo) {
						mutablePos2.setZ(startZ + zo);

						if (!canBeReplaced(world, mutablePos2)) {
							fxo += xo;
							fzo += zo;
							count++;
						}
					}
				}

				if (count > 0) {
					fxo /= count;
					fzo /= count;
					int tempOldFxo = fxo;

					fxo = fzo > fxo ? 0 : -signum(fxo);
					fzo = tempOldFxo > fzo ? 0 : -signum(fzo);
				}
			}
		}

		mutablePos.setY(startY - 1);

		// check gen conditions

		if (this.isValidSoil(world, mutablePos)) {
			final int foliageStart = height - foliageDepth;

			for (int yo = foliageStart; yo <= height; ++yo) {
				mutablePos.setY(startY + yo);

				if (yo < trunkHeight) {
					switch ((yo - foliageStart) & 0b11) { // (yo - (foliageStart - 1) - 1) % 4
					case 0:
						if (height > 11 && (yo < foliageStart + 3)) {
							this.growBigLeaves1(world, rand, mutablePos, mutablePos.getX(), mutablePos.getZ(), config);
						} else {
							this.growBigLeaves2(world, rand, mutablePos, mutablePos.getX(), mutablePos.getZ(), config);
						}
						break;
					case 2:
						if (rand.nextBoolean()) {
							this.growMediumLeaves(world, rand, mutablePos, mutablePos.getX(), mutablePos.getZ(), config); // "small leaves 1"
						} else {
							this.growSmallLeaves(world, rand, mutablePos, mutablePos.getX(), mutablePos.getZ(), config); // "small leaves 2"
						}
						break;
					default:
						this.growTinyLeaves(world, rand, mutablePos, mutablePos.getX(), mutablePos.getZ(), config); // "small leaves 3"
						break;
					}
				} else if (yo < height) {
					switch ((height - yo) & 0b1) {
					case 0:
						this.growSmallLeaves(world, rand, mutablePos, mutablePos.getX(), mutablePos.getZ(), config);
						break;
					case 1:
						this.growTinyLeaves(world, rand, mutablePos, mutablePos.getX(), mutablePos.getZ(), config);
						break;
					}
				} else {
					this.setLeaf(world, rand, mutablePos, config);
				}

				mutablePos.setX(yo > 2 ? startX + fxo : startX);
				mutablePos.setZ(yo > 2 ? startZ + fzo : startZ);
			}

			mutablePos.setX(startX);
			mutablePos.setZ(startZ);

			for (int yo = 0; yo <= trunkHeight; ++yo) {
				if (yo == 3) {
					mutablePos.setX(startX + fxo);
					mutablePos.setZ(startZ + fzo);
				}

				mutablePos.setY(startY + yo);
				this.setLog(world, rand, mutablePos, config);
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	protected int getHeight(WorldGenLevel level, Random rand, BlockPos origin, TreeFeatureConfig config) {
		final int startY = origin.getY();
		int height = super.getHeight(level, rand, origin, config);

		// START extreme altitude climate modification
		if (this.natGen) {
			final int randNum = rand.nextInt(12);

			if (startY > 172) {
				if (startY > 196) {
					if (randNum > 0) { // 1/12 success
						return DO_NOT_GENERATE;
					}
				} else if (randNum > 1) {
					if (randNum > 1) { // 2/12 success
						return DO_NOT_GENERATE;
					}
				}

				// shape mod from height
				height = Math.max(Mth.ceil(config.baseHeight / 2.0), height - 4);
			} else if (startY > 132) {
				if (randNum > 2) { // 3/12 success.
					return DO_NOT_GENERATE;
				}

				// shape mod from height
				height = Math.max(Math.max(1, config.baseHeight - 1), height - 2);
			} else if (startY > 98) {
				if (randNum > 4) { // 5/12 success.
					return DO_NOT_GENERATE;
				}
			} else {
				// else 5/5 success && more prosperous height.
				height++;
			}
		} else if (startY > 172) { // sapling grown height mod
			height = Math.max(Mth.ceil(config.baseHeight / 2.0), height - 4);
		} else if (startY > 132) { // sapling grown height mod
			height = Math.max(Math.max(1, config.baseHeight - 1), height - 2);
		}

		// END extreme altitude climate modification

		return height;
	}

	@Override
	protected boolean isValidSoil(WorldGenLevel level, BlockPos pos) {
		return super.isValidSoil(level, pos) || level.isStateAtPosition(pos, state -> state.getBlock() == Blocks.MOSSY_COBBLESTONE);
	}

	private void growBigLeaves1(WorldGenLevel world, Random rand, BlockPos.MutableBlockPos mutablePos, int startX, int startZ, TreeFeatureConfig config) {
		this.growBigLeaves2(world, rand, mutablePos, startX, startZ, config);

		// apparently "outer x, outer z"
		for (int lilScumbag = -1; lilScumbag <= 1; ++lilScumbag) {
			// x 3
			mutablePos.setZ(startZ + lilScumbag);
			mutablePos.setX(startX + 3);
			this.setLeaf(world, rand, mutablePos, config);
			mutablePos.setX(startX - 3);
			this.setLeaf(world, rand, mutablePos, config);

			// z 3
			mutablePos.setX(startX + lilScumbag);
			mutablePos.setZ(startZ + 3);
			this.setLeaf(world, rand, mutablePos, config);
			mutablePos.setZ(startZ - 3);
			this.setLeaf(world, rand, mutablePos, config);
		}

		// 2-out diagonals
		mutablePos.setZ(startZ + 2);
		mutablePos.setX(startX + 2);
		this.setLeaf(world, rand, mutablePos, config);
		mutablePos.setX(startX - 2);
		this.setLeaf(world, rand, mutablePos, config);

		mutablePos.setX(startX - 2);
		mutablePos.setZ(startZ + 2);
		this.setLeaf(world, rand, mutablePos, config);
		mutablePos.setZ(startZ - 2);
		this.setLeaf(world, rand, mutablePos, config);
	}

	private void growBigLeaves2(WorldGenLevel world, Random rand, BlockPos.MutableBlockPos mutablePos, int startX, int startZ, TreeFeatureConfig config) {
		// I am too tired at like nearly 12 am to make a proper for loop to replace what I did in 1.12
		// I mean this is still 10x better because I don't do new BlockPos twice a blockset

		// inner, outer
		for (int i = 1; i <= 2; ++i) {
			mutablePos.setX(startX - i);
			this.setLeaf(world, rand, mutablePos, config);
			mutablePos.setX(startX + i);
			this.setLeaf(world, rand, mutablePos, config);
			mutablePos.setX(startX);
			mutablePos.setZ(startZ - i);
			this.setLeaf(world, rand, mutablePos, config);
			mutablePos.setZ(startZ + i);
			this.setLeaf(world, rand, mutablePos, config);
			mutablePos.setZ(startZ);
		}

		// knight's move z
		mutablePos.setZ(startZ + 2);
		mutablePos.setX(startX - 1);
		this.setLeaf(world, rand, mutablePos, config);
		mutablePos.setX(startX + 1);
		this.setLeaf(world, rand, mutablePos, config);

		mutablePos.setZ(startZ - 2);
		mutablePos.setX(startX - 1);
		this.setLeaf(world, rand, mutablePos, config);
		mutablePos.setX(startX + 1);
		this.setLeaf(world, rand, mutablePos, config);

		// knight's move x
		mutablePos.setZ(startZ + 1);
		mutablePos.setX(startX - 2);
		this.setLeaf(world, rand, mutablePos, config);
		mutablePos.setX(startX + 2);
		this.setLeaf(world, rand, mutablePos, config);

		mutablePos.setZ(startZ - 1);
		mutablePos.setX(startX - 2);
		this.setLeaf(world, rand, mutablePos, config);
		mutablePos.setX(startX + 2);
		this.setLeaf(world, rand, mutablePos, config);
	}

	private void growMediumLeaves(WorldGenLevel world, Random rand, BlockPos.MutableBlockPos mutablePos, int startX, int startZ, TreeFeatureConfig config) {
		for (int xo = -2; xo <= 2; ++xo) {
			final int x = startX + xo;
			mutablePos.setX(x);

			final int zRange = 2 - Mth.abs(xo);
			final int endZ = startZ + zRange;

			for (int z = startZ - zRange; z <= endZ; ++z) {
				mutablePos.setZ(z);

				if (x != startX || z != startZ) {
					this.setLeaf(world, rand, mutablePos, config);
				}
			}
		}
	}

	private void growSmallLeaves(WorldGenLevel world, Random rand, BlockPos.MutableBlockPos mutablePos, int startX, int startZ, TreeFeatureConfig config) {
		final int endX = startX + 2;
		final int endZ = startZ + 2;

		for (int x = startX - 1; x < endX; ++x) {
			mutablePos.setX(x);

			for (int z = startZ - 1; z < endZ; ++z) {
				mutablePos.setZ(z);

				if (x != startX || z != startZ) {
					this.setLeaf(world, rand, mutablePos, config);
				}
			}
		}
	}

	private void growTinyLeaves(WorldGenLevel world, Random rand, BlockPos.MutableBlockPos mutablePos, int startX, int startZ, TreeFeatureConfig config) {
		mutablePos.setX(startX - 1);
		this.setLeaf(world, rand, mutablePos, config);
		mutablePos.setX(startX + 1);
		this.setLeaf(world, rand, mutablePos, config);
		mutablePos.setX(startX);
		mutablePos.setZ(startZ - 1);
		this.setLeaf(world, rand, mutablePos, config);
		mutablePos.setZ(startZ + 1);
		this.setLeaf(world, rand, mutablePos, config);
	}

	private static int signum(int x) {
		if (x == 0) {
			return 0;
		} else {
			return x > 0 ? 1 : -1;
		}
	}
}
