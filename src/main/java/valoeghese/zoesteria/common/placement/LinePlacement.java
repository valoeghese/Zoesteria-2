package valoeghese.zoesteria.common.placement;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import net.minecraft.core.BlockPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import valoeghese.zoesteria.common.util.OpenSimplexNoise;

// how the HECK am I meant to translate this to 1.18??
public class LinePlacement extends PlacementModifier<LinePlacementConfig> {
	public LinePlacement() {
		super(LinePlacementConfig::deserialize);
	}

	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(new Random(69420L));

	private Stream<BlockPos> getPositions(WorldGenLevel worldIn, ChunkGenerator generatorIn, Random random, LinePlacementConfig config, BlockPos pos) {
		int count = config.count;

		return IntStream.range(0, count).mapToObj($ -> {
			int x = random.nextInt(16) + pos.getX();
			int z = random.nextInt(16) + pos.getZ();
			int y = worldIn.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
			return new BlockPos(x, y, z);
		}).filter(bpos -> {
			double noise = NOISE.sample(bpos.getX() * config.frequency, bpos.getZ() * config.frequency) + config.offset;
			return noise > -config.threshold && noise < config.threshold;
		});
	}

	@Override
	public Stream<BlockPos> getPositions(PlacementContext ctx, Random rand, BlockPos pos) {
		feature -> this.getPositions(ctx.getLevel(), ctx.generator(), rand, feature));
	}
}
