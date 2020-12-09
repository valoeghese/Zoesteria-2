package tk.valoeghese.zoesteria.api.feature;

import net.minecraft.world.gen.placement.IPlacementConfig;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

/**
 * Serialisation and Deserialisation handler for Placement Configs. Register in {@link FeatureSerialisers}.
 */
public interface IPlacementConfigSerialiser<T extends IPlacementConfig> {
	/**
	 * Load the placement config data from an existing config into a new instance.
	 */
	IPlacementConfigSerialiser<T> loadFrom(T config);
	/**
	 * Deserialise the placement config data into a new instance.
	 */
	IPlacementConfigSerialiser<T> deserialise(Container settings);
	/**
	 * Serialise the placement config data of this instance into the settings.
	 */
	void serialise(EditableContainer settings);
	/**
	 * @return an instance of the placement config this creates, with the loaded settings of this instance.
	 */
	T create();
}
