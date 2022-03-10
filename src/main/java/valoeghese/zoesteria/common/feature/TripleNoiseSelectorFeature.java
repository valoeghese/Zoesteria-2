package valoeghese.zoesteria.common.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import valoeghese.zoesteria.common.util.OpenSimplexNoise;

import java.util.Random;

public class TripleNoiseSelectorFeature extends OldStyleFeature<TripleFeatureConfig> {
	public TripleNoiseSelectorFeature() {
		super(TripleFeatureConfig.CODEC);
	}

	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(new Random(0));

	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator generator, Random rand, BlockPos pos, TripleFeatureConfig config) {
		int iseed = (int) (world.getSeed() & 0x7fffffffL);
		int iseed2 = (int) ((world.getSeed() >> 32) & 0x7fffffffL);
		int xoff = 100 * (int) (10 * (double) iseed / (double) Integer.MAX_VALUE);
		int zoff = 100 * (int) (10 * (double) iseed2 / (double) Integer.MAX_VALUE);
		double noise = NOISE.sample(0.0012D * (pos.getX() + xoff) + config.offset, 0.0012D * (pos.getZ() + zoff));

		if (noise > 0.251) {
			return config.feature0.place(world, generator, rand, pos);
		} else if (noise < -0.251) {
			return config.feature2.place(world, generator, rand, pos);
		} else {
			return config.feature1.place(world, generator, rand, pos);
		}
	}
}
