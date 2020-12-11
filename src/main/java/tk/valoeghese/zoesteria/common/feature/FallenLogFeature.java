package tk.valoeghese.zoesteria.common.feature;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;

public class FallenLogFeature extends Feature<TreeLikeFeatureConfig> {
	public FallenLogFeature() {
		super(TreeLikeFeatureConfig::deserialize);
	}

	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, TreeLikeFeatureConfig config) {
		int size = config.baseSize + rand.nextInt(config.sizeRandom + 1);
		
		return false;
	}
}
