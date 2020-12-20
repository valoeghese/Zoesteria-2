package tk.valoeghese.zoesteria.common.predicate;


import java.util.Locale;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import tk.valoeghese.zoesteria.api.biome.IBiomePredicate;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public final class OverworldBiomeDictionaryPredicate implements IBiomePredicate {
	public OverworldBiomeDictionaryPredicate(BiomeDictionary.Type type) {
		this.type = type;
	}

	private final BiomeDictionary.Type type;

	@Override
	public ResourceLocation id() {
		return new ResourceLocation("zoesteria", "overworld_biome_dictionary");
	}

	@Override
	public IBiomePredicate deserialise(Container settings) {
		return new OverworldBiomeDictionaryPredicate(BiomeDictionary.Type.getType(settings.getStringValue("biomeType").toUpperCase(Locale.ROOT)));
	}

	@Override
	public void serialise(EditableContainer settings) {
		settings.putStringValue("biomeType", this.type.getName().toLowerCase(Locale.ROOT));
	}

	@Override
	public boolean test(Biome biome) {
		return BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD) && BiomeDictionary.hasType(biome, this.type);
	}
}
