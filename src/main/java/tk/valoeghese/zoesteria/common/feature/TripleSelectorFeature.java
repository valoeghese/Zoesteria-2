package tk.valoeghese.zoesteria.common.feature;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import tk.valoeghese.zoesteria.common.util.LossyDoubleCache;
import tk.valoeghese.zoesteria.common.util.OpenSimplexNoise;

public class TripleSelectorFeature extends Feature<TripleFeatureConfig>{
	public TripleSelectorFeature() {
		super(TripleFeatureConfig::deserialize);
	}

	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(new Random(0));
	private static final LossyDoubleCache SAMPLER = new LossyDoubleCache(256, (x, z) -> NOISE.sample((double) x * 0.001D, (double) z * 0.001D));

	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, TripleFeatureConfig config) {
		int iseed = (int) (world.getSeed() & 0x7fffffffL);
		int iseed2 = (int) ((world.getSeed() >> 32) & 0x7fffffffL);
		int xoff = 100 * (int) (10 * (double) iseed / (double) Integer.MAX_VALUE);
		int zoff = 100 * (int) (10 * (double) iseed2 / (double) Integer.MAX_VALUE);
		double noise = SAMPLER.get(pos.getX() + xoff, pos.getZ() + zoff);

		if (noise > 0.22) {
			return config.feature0.place(world, generator, rand, pos);
		} else if (noise < -0.22) {
			return config.feature2.place(world, generator, rand, pos);
		} else {
			return config.feature1.place(world, generator, rand, pos);
		}
	}
}
