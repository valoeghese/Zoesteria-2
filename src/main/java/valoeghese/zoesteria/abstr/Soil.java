package valoeghese.zoesteria.abstr;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Represent's a plant's preferred soil.
 */
public enum Soil {
	/**
	 * For normal plants.
	 */
	DIRTY() {
		@Override
		public boolean isValidSoil(BlockState soil) {
			return soil.is(BlockTags.DIRT) || soil.is(Blocks.FARMLAND);
		}
	},
	/**
	 * For sand-based plants that grow in a desert.
	 */
	SANDY_DESERT() {
		@Override
		public boolean isValidSoil(BlockState soil) {
			return soil.is(BlockTags.SAND);
		}
	},
	/**
	 * For sand-based plants that grow on a beach.
	 */
	SANDY_BEACH() {
		@Override
		public boolean isValidSoil(BlockState soil) {
			return soil.is(BlockTags.SAND);
		}
	};

	Soil() {
	}

	public abstract boolean isValidSoil(BlockState soil);
}
