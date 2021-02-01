package tk.valoeghese.zoesteria.core.serialisers.feature;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredRandomFeatureList;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.MultipleRandomFeatureConfig;
import tk.valoeghese.zoesteria.api.feature.IFeatureConfigSerialiser;
import tk.valoeghese.zoesteria.core.pack.GenModifierPack;
import tk.valoeghese.zoesteria.core.pack.biome.BiomeFactory;
import tk.valoeghese.zoesteriaconfig.api.ZoesteriaConfig;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class RandomSelectorConfigSerialiser implements IFeatureConfigSerialiser<MultipleRandomFeatureConfig> {
	private RandomSelectorConfigSerialiser(ConfiguredFeature<?,?> defaultFeature, List<ConfiguredRandomFeatureList<?>> features) {
		this.defaultFeature = defaultFeature;
		this.features = features;
	}

	private final ConfiguredFeature<?, ?> defaultFeature;
	private final List<ConfiguredRandomFeatureList<?>> features;

	@Override
	public IFeatureConfigSerialiser<MultipleRandomFeatureConfig> loadFrom(MultipleRandomFeatureConfig config) {
		return new RandomSelectorConfigSerialiser(config.defaultFeature, config.features);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IFeatureConfigSerialiser<MultipleRandomFeatureConfig> deserialise(Container settings) {
		ConfiguredFeature<?, ?> defaultFeature = BiomeFactory.deserialiseConfiguredFeature(settings.getMap("defaultFeature"));

		List<ConfiguredRandomFeatureList<? extends IFeatureConfig>> deserialised = new ArrayList<>();

		for (Object o : settings.getList("features")) {
			deserialised.add(toFeatureEntry((Map<String, Object>) o));
		}

		return new RandomSelectorConfigSerialiser(defaultFeature, deserialised);
	}

	@Override
	public void serialise(EditableContainer settings) {
		Map<String, Object> featureData = new LinkedHashMap<>();
		GenModifierPack.serialiseConfiguredFeature(featureData, this.defaultFeature);
		settings.putMap("defaultFeature", featureData);

		List<Object> serFeatures = new ArrayList<>();

		for (ConfiguredRandomFeatureList<?> entry : this.features) {
			serFeatures.add(fromFeatureEntry(entry).asMap());
		}

		settings.putList("features", serFeatures);
	}

	@Override
	public MultipleRandomFeatureConfig create() {
		return new MultipleRandomFeatureConfig(this.features, this.defaultFeature);
	}

	private static Container fromFeatureEntry(ConfiguredRandomFeatureList<? extends IFeatureConfig> item) {
		EditableContainer result = ZoesteriaConfig.createWritableConfig(new LinkedHashMap<>());

		Map<String, Object> featureData = new LinkedHashMap<>();
		GenModifierPack.serialiseConfiguredFeature(featureData, item.feature);
		result.putMap("feature", featureData);

		result.putFloatValue("weight", item.chance);
		return result;
	}

	private static ConfiguredRandomFeatureList<? extends IFeatureConfig> toFeatureEntry(Map<String, Object> data) {
		@SuppressWarnings("unchecked")
		ConfiguredFeature<?, ?> feature = BiomeFactory.deserialiseConfiguredFeature((Map<String, Object>) data.get("feature"));
		return new ConfiguredRandomFeatureList<>(feature, Float.valueOf((String) data.get("weight")));
	}

	public static final RandomSelectorConfigSerialiser BASE = new RandomSelectorConfigSerialiser(null, null);
}
