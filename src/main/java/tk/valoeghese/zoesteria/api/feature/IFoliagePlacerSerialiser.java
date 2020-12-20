package tk.valoeghese.zoesteria.api.feature;

import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import tk.valoeghese.zoesteria.api.ZoesteriaSerialisers;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

/**
 * Serialisation and Deserialisation handler for Foliage Placers. Register in {@link ZoesteriaSerialisers}.
 */
public interface IFoliagePlacerSerialiser<T extends FoliagePlacer> {
	/**
	 * Load the foliage placer data from an existing foliage placer into a new instance.
	 */
	IFoliagePlacerSerialiser<T> loadFrom(T placer);
	/**
	 * Deserialise the foliage placer data into a new instance.
	 */
	IFoliagePlacerSerialiser<T> deserialise(Container settings);
	/**
	 * Serialise the foliage placer data of this instance into the settings.
	 */
	void serialise(EditableContainer settings);
	/**
	 * @return an instance of the foliage placer, with the loaded settings of this instance.
	 */
	T create();
}
