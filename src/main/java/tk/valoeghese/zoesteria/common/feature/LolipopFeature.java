package tk.valoeghese.zoesteria.common.feature;

import java.util.Random;
import java.util.Set;

import net.minecraft.util.math.BlockPos;
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

		int foliageDepth = config.foliageHeight + rand.nextInt(config.foliageHeightRandom);

		pos.setPos(start);

		if (isSoil(world, start.down(), null)) {
			
		}
	}
}
