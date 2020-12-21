package tk.valoeghese.zoesteria.common.feature;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.feature.Feature;

public class SimpleShrubFeature extends Feature<ShrubFeatureConfig> {
	public SimpleShrubFeature() {
		super(ShrubFeatureConfig::deserialize);
	}

	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, ShrubFeatureConfig config) {
		BlockState down = world.getBlockState(pos.down());

		if (isDirt(down.getBlock()) && world.getBlockState(pos).isAir(world, pos)) {
			if (rand.nextInt(3) == 0) {
				this.trySet(world, rand, pos, config.logProvider);
				pos = pos.up();
			}

			this.trySet(world, rand, pos, config.logProvider);
			this.trySet(world, rand, pos.up(), config.leavesProvider);
			this.trySet(world, rand, pos.north(), config.leavesProvider);
			this.trySet(world, rand, pos.east(), config.leavesProvider);
			this.trySet(world, rand, pos.south(), config.leavesProvider);
			this.trySet(world, rand, pos.west(), config.leavesProvider);
			return true;
		}

		return false;
	}

	private void trySet(IWorld world, Random rand, BlockPos pos, BlockStateProvider state) {
		if (isAirOrLeaves(world, pos) || isTallPlants(world, pos)) {
			setBlockState(world, pos, state.getBlockState(rand, pos));
		}
	}

	public static boolean isAirOrLeaves(IWorld world, BlockPos pos) {
		return world.hasBlockState(pos, state -> state.canBeReplacedByLeaves(world, pos));
	}

	public static boolean isTallPlants(IWorldGenerationBaseReader world, BlockPos pos) {
		return world.hasBlockState(pos, state -> state.getMaterial() == Material.TALL_PLANTS);
	}
}
