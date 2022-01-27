package valoeghese.zoesteria.abstr.biome;

import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/**
 * A class containing a representation of the biome's properties.
 */
public class BiomeProperties {
	private BiomeProperties(BiomeCategory category, Precipitation precipitation, float temperature, float rainfall,
							@Nullable BlockState topBlock, @Nullable BlockState fillerBlock) {
		this.category = category;
		this.precipitation = precipitation;
		this.temperature = temperature;
		this.rainfall = rainfall;
		this.topBlock = topBlock;
		this.fillerBlock = fillerBlock;
	}

	public final BiomeCategory category;
	public final Precipitation precipitation;
	public final float temperature;
	public final float rainfall;
	@Nullable private final BlockState topBlock;
	@Nullable private final BlockState fillerBlock;

	public static class Builder {
		public Builder(BiomeCategory category) {
			this.category = category;
		}

		private final BiomeCategory category;
		@Nullable private Precipitation precipitation;
		private float temperature = 0.5f;
		private float rainfall = 0.5f;
		@Nullable private BlockState topBlock;
		@Nullable private BlockState fillerBlock;

		public Builder temperature(float temp) {
			this.temperature = temp;
			return this;
		}

		public Builder rainfall(float rainfall) {
			this.rainfall = rainfall;
			return this;
		}

		public Builder precipitation(Precipitation precipitation) {
			this.precipitation = precipitation;
			return this;
		}

		public Builder simpleSurface(BlockState topBlock, BlockState fillerBlock) {
			this.topBlock = topBlock;
			this.fillerBlock = fillerBlock;
			return this;
		}

		public BiomeProperties build() {
			if (this.precipitation == null) this.precipitation = this.rainfall <= 0.0f ? Precipitation.NONE : (this.temperature < 0 ? Precipitation.SNOW : Precipitation.RAIN);
			return new BiomeProperties(this.category, this.precipitation, this.temperature, this.rainfall,
					this.topBlock, this.fillerBlock);
		}
	}
}
