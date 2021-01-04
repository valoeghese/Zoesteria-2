package tk.valoeghese.zoesteria.common.placement;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import tk.valoeghese.zoesteria.common.util.OpenSimplexNoise;

public class LinePlacement extends Placement<FrequencyConfig>{
	public LinePlacement() {
		super(FrequencyConfig::deserialize);
	}

	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(new Random(69420L));

	@Override
	public Stream<BlockPos> getPositions(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generatorIn, Random random, FrequencyConfig configIn, BlockPos pos) {
		int i = configIn.count;

		return IntStream.range(0, i).mapToObj((p_227444_3_) -> {
			int j = random.nextInt(16) + pos.getX();
			int k = random.nextInt(16) + pos.getZ();
			int l = worldIn.getHeight(Heightmap.Type.MOTION_BLOCKING, j, k);
			return new BlockPos(j, l, k);
		}).filter(bpos -> {
			double noise = NOISE.sample(bpos.getX() * 0.008D, bpos.getZ() * 0.008D);
			return noise > -0.05 && noise < 0.05;
		});
	}

}
