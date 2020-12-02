package tk.valoeghese.zoesteria.api.biome;

import java.util.Optional;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.Biome.RainType;

public final class BiomePropertiesBuilder {
	BiomePropertiesBuilder(Biome.Category category) {
		this.category = category;
	}

	private final Biome.Category category;

	private float depth = 0.15f;
	private float scale = 0.125f;
	private float temperature = 0.5f;
	private float downfall = 0.5f;
	private Biome.RainType precipitation = Biome.RainType.RAIN;
	private Optional<String> topBlock = Optional.empty();
	private Optional<String> fillerBlock = Optional.empty();
	private Optional<String> underwaterBlock = Optional.empty();
	private int waterColour = 4159204;
	private int waterFogColour = 329011;
	private Optional<String> surfaceBuilder = Optional.empty();

	public BiomePropertiesBuilder depth(float depth) {
		this.depth = depth;
		return this;
	}

	public BiomePropertiesBuilder scale(float scale) {
		this.scale = scale;
		return this;
	}

	public BiomePropertiesBuilder temperature(float temperature) {
		this.temperature = temperature;
		return this;
	}

	public BiomePropertiesBuilder downfall(float downfall) {
		this.downfall = downfall;
		return this;
	}

	public BiomePropertiesBuilder precipitation(Biome.RainType precipitation) {
		this.precipitation = precipitation;
		return this;
	}

	public BiomePropertiesBuilder topBlock(String topBlock) {
		this.topBlock = Optional.of(topBlock);
		return this;
	}

	public BiomePropertiesBuilder fillerBlock(String fillerBlock) {
		this.fillerBlock = Optional.of(fillerBlock);
		return this;
	}

	public BiomePropertiesBuilder underwaterBlock(String underwaterBlock) {
		this.underwaterBlock = Optional.of(underwaterBlock);
		return this;
	}

	public BiomePropertiesBuilder surfaceBuilder(String surfaceBuilder) {
		this.surfaceBuilder = Optional.of(surfaceBuilder);
		return this;
	}

	public BiomePropertiesBuilder waterColour(int colour) {
		this.waterColour = colour;
		return this;
	}

	public BiomePropertiesBuilder waterFogColour(int colour) {
		this.waterFogColour = colour;
		return this;
	}

	public IBiomeProperties build() {
		return new IBiomeProperties() {
			@Override
			public float depth() {
				return depth;
			}
			@Override
			public float scale() {
				return scale;
			}
			@Override
			public float temperature() {
				return temperature;
			}
			@Override
			public float downfall() {
				return downfall;
			}
			@Override
			public Category category() {
				return category;
			}
			@Override
			public RainType precipitation() {
				return precipitation;
			}
			@Override
			public Optional<String> topBlock() {
				return topBlock;
			}
			@Override
			public Optional<String> fillerBlock() {
				return fillerBlock;
			}
			@Override
			public Optional<String> underwaterBlock() {
				return underwaterBlock;
			}
			@Override
			public int waterColour() {
				return waterColour;
			}
			@Override
			public int waterFogColour() {
				return waterFogColour;
			}
			@Override
			public Optional<String> surfaceBuilder() {
				return surfaceBuilder;
			}
		};
	}
}
