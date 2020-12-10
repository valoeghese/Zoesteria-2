package tk.valoeghese.zoesteria.common.feature;

import net.minecraft.world.gen.feature.ConfiguredFeature;
import tk.valoeghese.zoesteria.api.feature.IFeatureConfigSerialiser;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class TripleFeatureConfigSerialiser implements IFeatureConfigSerialiser<TripleFeatureConfig> {
	private TripleFeatureConfigSerialiser(ConfiguredFeature<?, ?> feature0, ConfiguredFeature<?, ?> feature1, ConfiguredFeature<?, ?> feature2) {
		this.feature0 = feature0;
		this.feature1 = feature1;
		this.feature2 = feature2;
	}

	private TripleFeatureConfigSerialiser(TripleFeatureConfig config) {
		this(config.feature0, config.feature1, config.feature2);
	}

	private final ConfiguredFeature<?, ?> feature0;
	private final ConfiguredFeature<?, ?> feature1;
	private final ConfiguredFeature<?, ?> feature2;

	@Override
	public IFeatureConfigSerialiser<TripleFeatureConfig> loadFrom(TripleFeatureConfig config) {
		return new TripleFeatureConfigSerialiser(config);
	}

	@Override
	public IFeatureConfigSerialiser<TripleFeatureConfig> deserialise(Container settings) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void serialise(EditableContainer settings) {
		// TODO Auto-generated method stub

	}

	@Override
	public TripleFeatureConfig create() {
		// TODO Auto-generated method stub
		return null;
	}

}
