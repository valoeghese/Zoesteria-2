package tk.valoeghese.zoesteria.common.feature.serialiser;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.world.gen.feature.ConfiguredFeature;
import tk.valoeghese.zoesteria.api.feature.IFeatureConfigSerialiser;
import tk.valoeghese.zoesteria.common.feature.TripleFeatureConfig;
import tk.valoeghese.zoesteria.core.pack.GenModifierPack;
import tk.valoeghese.zoesteria.core.pack.biome.BiomeFactory;
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
		ConfiguredFeature<?, ?> feature0 = BiomeFactory.deserialiseConfiguredFeature(settings.getMap("feature0"));
		ConfiguredFeature<?, ?> feature1 = BiomeFactory.deserialiseConfiguredFeature(settings.getMap("feature1"));
		ConfiguredFeature<?, ?> feature2 = BiomeFactory.deserialiseConfiguredFeature(settings.getMap("feature2"));
		return new TripleFeatureConfigSerialiser(feature0, feature1, feature2);
	}

	@Override
	public void serialise(EditableContainer settings) {
		Map<String, Object> feature0Data = new LinkedHashMap<>();
		Map<String, Object> feature1Data = new LinkedHashMap<>();
		Map<String, Object> feature2Data = new LinkedHashMap<>();

		GenModifierPack.serialiseConfiguredFeature(feature0Data, this.feature0);
		GenModifierPack.serialiseConfiguredFeature(feature1Data, this.feature1);
		GenModifierPack.serialiseConfiguredFeature(feature2Data, this.feature2);

		settings.putMap("feature0", feature0Data);
		settings.putMap("feature1", feature1Data);
		settings.putMap("feature2", feature2Data);
	}

	@Override
	public TripleFeatureConfig create() {
		return new TripleFeatureConfig(this.feature0, this.feature1, this.feature2);
	}

	public static final TripleFeatureConfigSerialiser BASE = new TripleFeatureConfigSerialiser(null, null, null);
}
