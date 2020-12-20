package tk.valoeghese.zoesteria.api.biome;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public interface IBiomePredicate {
	/**
	 * @return the id of this biome target.
	 */
	ResourceLocation id();
	/**
	 * Deserialise the biome predicate data into a new instance.
	 */
	IBiomePredicate deserialise(Container settings);
	/**
	 * Serialise the data of this biome predicate instance into the settings.
	 */
	void serialise(EditableContainer settings);
	/**
	 * @param biome the biome to test.
	 * @return whether the predicate should be met.
	 */
	boolean test(Biome biome);
}
