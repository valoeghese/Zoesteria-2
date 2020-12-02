package tk.valoeghese.zoesteria.api.biome;

import java.util.Optional;

import net.minecraft.world.biome.Biome;

public interface IBiomeProperties {
	float depth();
	float scale();
	float temperature();
	float downfall();
	Biome.Category category();
	Biome.RainType precipitation();
	int waterColour();
	int waterFogColour();
	Optional<String> topBlock();
	Optional<String> fillerBlock();
	Optional<String> underwaterBlock();
	Optional<String> surfaceBuilder();

	static BiomePropertiesBuilder builder(Biome.Category category) {
		return new BiomePropertiesBuilder(category);
	}
}
