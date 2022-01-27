package valoeghese.zoesteria.common.surface;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class FillToSeaLevelSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {
	public FillToSeaLevelSurfaceBuilder() {
		super(SurfaceBuilderConfig::deserialize);
	}

	@Override
	@SuppressWarnings("deprecation") // f*ck you forge I can do what I want
	public void buildSurface(Random random, IChunk chunk, Biome biome, int x, int z, int startHeight, double noise,
			BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config) {
		BlockPos.Mutable pos = new BlockPos.Mutable(x & 0xF, 0, z & 0xF);
		BlockPos.Mutable realPos = new BlockPos.Mutable(x, 0, z);
		boolean iceOver = defaultFluid == Blocks.WATER.getDefaultState();
		int fillDepth = (int) (noise / 3.0D + 3.0D + random.nextDouble() * 0.25D); // I think this takes -8 to 8 and transforms it to 0 to 5

		BlockState topBlock = config.getTop();
		BlockState fillerBlock = config.getUnder();
		BlockState underwaterBlock = config.getUnderWaterMaterial();

		int depth = 0;

		for (int y = startHeight; y >= 0; --y) {
			pos.setY(y);

			BlockState current = chunk.getBlockState(pos);

			if (current.isAir()) {
				depth = 0;
			} else {
				depth++;

				if (current == defaultFluid || y < seaLevel) {
					realPos.setY(y);

					if (iceOver && biome.getTemperature(realPos) < 0.15f) {
						chunk.setBlockState(pos, Blocks.ICE.getDefaultState(), false);
					} else {
						chunk.setBlockState(pos, defaultFluid, false); // should be unneccesary but meh
					}
				}
				else if (current.getBlock() == defaultBlock.getBlock()) {
					if (fillDepth <= 0) {
						topBlock = Blocks.AIR.getDefaultState();
						fillerBlock = defaultBlock;
					} else if (y >= seaLevel - 4 && y <= seaLevel + 1) {
						topBlock = config.getTop();
						fillerBlock = config.getUnder();
					}

					if (y < seaLevel - 7 - fillDepth) {
						topBlock = Blocks.AIR.getDefaultState();
						fillerBlock = underwaterBlock;
					}

					if (depth == 1) {
						chunk.setBlockState(pos, topBlock, false);
					} else if (y >= seaLevel || depth < fillDepth) { // depth must be > 1
						chunk.setBlockState(pos, fillerBlock, false);
					}
				}
			}
		}
	}
}
