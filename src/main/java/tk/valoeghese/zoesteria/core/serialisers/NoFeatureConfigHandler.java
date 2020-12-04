package tk.valoeghese.zoesteria.core.serialisers;

import net.minecraft.world.gen.feature.NoFeatureConfig;
import tk.valoeghese.zoesteria.api.feature.IZoesteriaFeatureConfig;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public enum NoFeatureConfigHandler implements IZoesteriaFeatureConfig<NoFeatureConfig> {
	INSTANCE;

	@Override
	public IZoesteriaFeatureConfig<NoFeatureConfig> loadFrom(NoFeatureConfig config) {
		return INSTANCE;
	}

	@Override
	public IZoesteriaFeatureConfig<NoFeatureConfig> deserialise(Container settings) {
		return INSTANCE;
	}

	@Override
	public void serialise(EditableContainer settings) {
	}

	@Override
	public NoFeatureConfig create() {
		return NoFeatureConfig.NO_FEATURE_CONFIG;
	}
}
