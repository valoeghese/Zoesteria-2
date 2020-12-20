package tk.valoeghese.zoesteria.common.predicate;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;
import tk.valoeghese.zoesteria.api.biome.IBiomePredicate;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public final class BiomeListPredicate implements IBiomePredicate {
	public BiomeListPredicate(List<Biome> biomes) {
		this.biomes = biomes;
	}

	private final List<Biome> biomes;

	@Override
	public ResourceLocation id() {
		return new ResourceLocation("zoesteria", "biome_list");
	}

	@Override
	public IBiomePredicate deserialise(Container settings) {
		List<Object> serialised = settings.getList("biomes");
		// Using streams bc I'm too lazy to do it procedurally lol
		// I'm sure it won't have that bad of a performance impact
		List<Biome> result = serialised.stream()
				.map(obj -> ForgeRegistries.BIOMES.getValue(new ResourceLocation((String) obj)))
				.collect(Collectors.toList());
		return new BiomeListPredicate(result);
	}

	@Override
	public void serialise(EditableContainer settings) {
		settings.putList("biomes", this.biomes.stream()
				.map(biome -> (Object) ForgeRegistries.BIOMES.getKey(biome).toString())
				.collect(Collectors.toList()));
	}

	@Override
	public boolean test(Biome biome) {
		return this.biomes.contains(biome);
	}
}
