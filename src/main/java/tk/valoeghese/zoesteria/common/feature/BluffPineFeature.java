package tk.valoeghese.zoesteria.common.feature;

import java.util.Random;
import java.util.Set;

import net.minecraft.util.math.BlockPos;
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
		final int trunkHeight = config.trunkHeight + rand.nextInt(config.trunkHeightRandom);

		// final xo
		int fxo = 0;
		// final zo
		int fzo = 0;

		// test if it can generate and set fxo/fzo where possible
		for (int i = 0; i < trunkHeight; ++i) {
			mutablePos.setY(startY + i);

			if (!canBeReplacedByLogs(world, mutablePos)) {
				return false;
			}

			if (i == 2) {
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

		mutablePos.setY(startY);

		// check gen conditions
		if (isSoil(world, mutablePos, null)) {
			
		}
	}

	private static int signum(int x) {
		if (x == 0) {
			return 0;
		} else {
			return x > 0 ? 1 : -1;
		}
	}
}
