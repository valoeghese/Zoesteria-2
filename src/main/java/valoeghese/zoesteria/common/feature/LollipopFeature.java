package valoeghese.zoesteria.common.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;

import java.util.Random;

public class LollipopFeature extends AbstractTreeFeature<TreeFeatureConfig> {
	public LollipopFeature() {
		super(TreeFeatureConfig.CODEC);
	}

	@Override
	protected boolean place(WorldGenLevel world, ChunkGenerator generator, Random rand, BlockPos origin, TreeFeatureConfig config) {
		int height = this.getHeight(world, rand, origin, config);

		if (!this.canPlace(world, origin, height)) return false;

		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos().set(origin);
		final int startY = origin.getY();

		for (int yo = 0; yo < height; ++yo) {
			pos.setY(startY + yo);

			if (!canBeReplaced(world, pos)) {
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

		if (this.isValidSoil(world, origin.below())) {
			// Generate the tree
			final int startX = origin.getX();
			final int startZ = origin.getZ();
			pos.set(origin); // initialise position

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
}
