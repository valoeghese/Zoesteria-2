package tk.valoeghese.zoesteria.api.feature;

import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

/**
 * Serialisation and Deserialisation handler for Foliage Placers. Register in {@link FeatureSerialisers}.
 */
public interface IZoesteriaFoliagePlacer<T extends FoliagePlacer> {
	/**
	 * Load the foliage placer data from an existing config into a new instance.
	 */
	IZoesteriaFoliagePlacer<T> loadFrom(T config);
	/**
	 * Deserialise the foliage placer data into a new instance.
	 */
	IZoesteriaFoliagePlacer<T> deserialise(Container settings);
	/**
	 * Serialise the foliage placer data of this instance into the settings.
	 */
	void serialise(EditableContainer settings);
	/**
	 * @return an instance of the foliage placer, with the loaded settings of this instance.
	 */
	T create();
}
