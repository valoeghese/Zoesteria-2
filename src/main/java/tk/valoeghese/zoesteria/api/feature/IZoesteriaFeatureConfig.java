package tk.valoeghese.zoesteria.api.feature;

import net.minecraft.world.gen.feature.IFeatureConfig;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

/**
 * Serialisation and Deserialisation handler for Feature Configs. Register in {@link FeatureSerialisers}.
 */
public interface IZoesteriaFeatureConfig<T extends IFeatureConfig> {
	/**
	 * Load the placement config data from an existing config into a new instance.
	 */
	IZoesteriaFeatureConfig<T> loadFrom(T config);
	/**
	 * Deserialise the feature config data into a new instance.
	 */
	IZoesteriaFeatureConfig<T> deserialise(Container settings);
	/**
	 * Serialise the feature config data of this instance into the settings.
	 */
	void serialise(EditableContainer settings);
	/**
	 * @return an instance of the feature config this creates, with the loaded settings of this instance.
	 */
	T create();
}
