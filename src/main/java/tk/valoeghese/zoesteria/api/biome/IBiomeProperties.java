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
	Optional<String> topBlock();
	Optional<String> fillerBlock();
	Optional<String> underwaterBlock();

	public static interface IExtendedBiomeProperties extends IBiomeProperties {
		int waterColour();
		int waterFogColour();
	}
}
