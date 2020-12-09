package tk.valoeghese.zoesteria.core.serialisers.feature;

import net.minecraft.world.gen.feature.NoFeatureConfig;
import tk.valoeghese.zoesteria.api.feature.IFeatureConfigSerialiser;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public enum NoFeatureConfigHandler implements IFeatureConfigSerialiser<NoFeatureConfig> {
	INSTANCE;

	@Override
	public IFeatureConfigSerialiser<NoFeatureConfig> loadFrom(NoFeatureConfig config) {
		return INSTANCE;
	}

	@Override
	public IFeatureConfigSerialiser<NoFeatureConfig> deserialise(Container settings) {
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
