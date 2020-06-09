package tk.valoeghese.zoesteria.api.biome;

import net.minecraft.world.gen.feature.IFeatureConfig;
import tk.valoeghese.zoesteriaconfig.api.container.Container;

public interface IZoesteriaFeatureConfig<T extends IFeatureConfig> {
	void load(Container settings);
	T create();
}
