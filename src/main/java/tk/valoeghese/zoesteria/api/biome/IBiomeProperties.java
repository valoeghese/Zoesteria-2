package tk.valoeghese.zoesteria.api.biome;

import net.minecraft.world.biome.Biome;

public interface IBiomeProperties {
	float depth();
	float scale();
	float temperature();
	float downfall();
	Biome.Category category();
	Biome.RainType precipitation();

	public static interface IExtendedBiomeProperties extends IBiomeProperties {
		int waterColour();
		int waterFogColour();
	}
}
