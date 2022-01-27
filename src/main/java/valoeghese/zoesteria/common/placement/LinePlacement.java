package valoeghese.zoesteria.common.placement;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.placement.Placement;
import valoeghese.zoesteria.common.util.OpenSimplexNoise;

public class LinePlacement extends Placement<LinePlacementConfig> {
	public LinePlacement() {
		super(LinePlacementConfig::deserialize);
	}

	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(new Random(69420L));

	@Override
	public Stream<BlockPos> getPositions(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generatorIn, Random random, LinePlacementConfig config, BlockPos pos) {
		int count = config.count;

		return IntStream.range(0, count).mapToObj($ -> {
			int x = random.nextInt(16) + pos.getX();
			int z = random.nextInt(16) + pos.getZ();
			int y = worldIn.getHeight(Heightmap.Type.MOTION_BLOCKING, x, z);
			return new BlockPos(x, y, z);
		}).filter(bpos -> {
			double noise = NOISE.sample(bpos.getX() * config.frequency, bpos.getZ() * config.frequency) + config.offset;
			return noise > -config.threshold && noise < config.threshold;
		});
	}

}
