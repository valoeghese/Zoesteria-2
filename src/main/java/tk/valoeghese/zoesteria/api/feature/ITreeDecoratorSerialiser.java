package tk.valoeghese.zoesteria.api.feature;

import net.minecraft.world.gen.treedecorator.TreeDecorator;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

/**
 * Serialiser and Deserialiser for Tree Decorators.
 */
public interface ITreeDecoratorSerialiser<T extends TreeDecorator> {
	/**
	 * Load the tree decorator data from an existing tree decorator into a new instance.
	 */
	ITreeDecoratorSerialiser<T> loadFrom(T decorator);
	/**
	 * Deserialise the tree decorator data into a new instance.
	 */
	ITreeDecoratorSerialiser<T> deserialise(Container settings);
	/**
	 * Serialise the tree decorator data of this instance into the settings.
	 */
	void serialise(EditableContainer settings);
	/**
	 * @return an instance of the tree decorator this creates, with the loaded settings of this instance.
	 */
	T create();
}
