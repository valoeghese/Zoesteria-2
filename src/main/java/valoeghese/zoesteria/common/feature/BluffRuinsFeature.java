package valoeghese.zoesteria.common.feature;

import net.minecraft.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class BluffRuinsFeature extends OldStyleFeature<NoneFeatureConfiguration> {
	public BluffRuinsFeature() {
		super(NoneFeatureConfiguration.CODEC);
	}

	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator generator, Random rand, BlockPos pos, NoneFeatureConfiguration config) {
		final int startY = pos.getY() - 1;

		if (startY > 0 && startY < world.getMaxBuildHeight() - 10) {
			final int height = Mth.clamp(world.getMaxBuildHeight() - startY, 10, 20) - 2 + ((startY < world.getMaxBuildHeight() - 20) ? rand.nextInt(5) : 0);
			final int threeQuarterHeight = 3 * height / 4;

			BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos().set(pos);
			mutablePos.setY(startY - 1);

			final int startX = pos.getX();
			final int startZ = pos.getZ();

			// test ground suitability
			for (int xo = -1; xo <= 1; ++xo) {
				mutablePos.setX(startX + xo);

				for (int zo = -1; zo <= 1; ++zo) {
					mutablePos.setZ(startZ + zo);

					if (!world.isStateAtPosition(mutablePos, state -> state.isSolid())) {
						return false;
					}
				}				
			}

			final int radius = 3 + rand.nextInt(3) + rand.nextInt(2);
			final int upperRadius = radius + 1;
			final Supplier<BlockState> walls = () -> WALLS[rand.nextInt(WALLS.length)];
			final Supplier<BlockState> floor = () -> FLOOR[rand.nextInt(FLOOR.length)];

			// battlement
			AtomicBoolean flip = new AtomicBoolean(false);

			final Supplier<BlockState> battlement = () -> {
				if (flip.getAndSet(!flip.get())) {
					return BATTLEMENT[rand.nextInt(BATTLEMENT.length)];
				} else {
					return null;
				}
			};
			final Supplier<BlockState> battlementTop = () -> {
				if (flip.getAndSet(!flip.get())) {
					return BATTLEMENT_TOP[rand.nextInt(BATTLEMENT_TOP.length)];
				} else {
					return null;
				}
			};

			final int lastHeight = height - 1;

			// build structure
			for (int i = 0; i < height; ++i) {
				mutablePos.setY(startY + i);

				if (i == 0) {
					fillRingAround(world, mutablePos, radius, floor, walls);
				} if (i < threeQuarterHeight) {
					createRingAround(world, mutablePos, radius, walls);
				} else if (i == threeQuarterHeight) {
					fillRingAround(world, mutablePos, upperRadius, floor, walls);
				} else if (i == lastHeight) {
					flip.set(false);
					createRingAround(world, mutablePos, upperRadius, battlementTop);
				} else {
					flip.set(false);
					createRingAround(world, mutablePos, upperRadius, battlement);
				}
			}

			return true;
		} else {
			return false;
		}
	}

	private static void fillRingAround(LevelWriter world, BlockPos.MutableBlockPos pos, final int radius, Supplier<BlockState> centre, Supplier<BlockState> outside) {
		createRingAround(world, pos, radius, outside);
		final int run = radius - 1;
		final int startX = pos.getX();
		final int startZ = pos.getZ();

		for (int xo = -run; xo <= run; ++xo) {
			pos.setX(startX + xo);

			for (int zo = -run; zo <= run; ++zo) {
				pos.setZ(startZ + zo);

				BlockState state = centre.get();

				if (state != null) {
					world.setBlock(pos, state, 3);
				}
			}
		}

		pos.setX(startX);
		pos.setZ(startZ);
	}

	private static void createRingAround(LevelWriter world, BlockPos.MutableBlockPos pos, final int radius, Supplier<BlockState> stateSupplier) {
		final int run = radius - 1;
		final int startX = pos.getX();
		final int startZ = pos.getZ();

		// -z line
		pos.setZ(startZ - radius);

		for (int i = -run; i <= run; ++i) {
			pos.setX(startX + i);
			BlockState state = stateSupplier.get();

			if (state != null) {
				world.setBlock(pos, state, 3);
			}
		}

		// +z line
		pos.setZ(startZ + radius);

		for (int i = -run; i <= run; ++i) {
			pos.setX(startX + i);
			BlockState state = stateSupplier.get();

			if (state != null) {
				world.setBlock(pos, state, 3);
			}
		}

		// -x line
		pos.setX(startX - radius);

		for (int i = -run; i <= run; ++i) {
			pos.setZ(startZ + i);
			BlockState state = stateSupplier.get();

			if (state != null) {
				world.setBlock(pos, state, 3);
			}
		}

		// +x line
		pos.setX(startX + radius);

		for (int i = -run; i <= run; ++i) {
			pos.setZ(startZ + i);
			BlockState state = stateSupplier.get();

			if (state != null) {
				world.setBlock(pos, state, 3);
			}
		}

		pos.setX(startX);
		pos.setZ(startZ);
	}

	private static final BlockState[] WALLS = new BlockState[] {
			Blocks.MOSSY_COBBLESTONE.defaultBlockState(),
			Blocks.COBBLESTONE.defaultBlockState(),
			Blocks.STONE_BRICKS.defaultBlockState(),
			null
	};
	private static final BlockState[] FLOOR = new BlockState[] {
			Blocks.SPRUCE_PLANKS.defaultBlockState(),
			Blocks.SPRUCE_PLANKS.defaultBlockState(),
			Blocks.SPRUCE_PLANKS.defaultBlockState(),
			Blocks.SPRUCE_SLAB.defaultBlockState(),
			null,
			null
	};
	private static final BlockState[] BATTLEMENT_TOP = new BlockState[] {
			Blocks.MOSSY_COBBLESTONE.defaultBlockState(),
			Blocks.COBBLESTONE.defaultBlockState(),
			Blocks.STONE_BRICKS.defaultBlockState(),
			Blocks.STONE_BRICK_SLAB.defaultBlockState(),
			Blocks.COBBLESTONE_SLAB.defaultBlockState(),
			null
	};
	private static final BlockState[] BATTLEMENT = new BlockState[] {
			Blocks.MOSSY_COBBLESTONE.defaultBlockState(),
			Blocks.COBBLESTONE.defaultBlockState(),
			Blocks.STONE_BRICKS.defaultBlockState()
	};
}
