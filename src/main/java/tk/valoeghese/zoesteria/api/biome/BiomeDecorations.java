package tk.valoeghese.zoesteria.api.biome;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.util.Tuple;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;

@SuppressWarnings("rawtypes")
public class BiomeDecorations {
	private BiomeDecorations() {
	}

	private List<Tuple<GenerationStage.Decoration, ConfiguredFeature>> decorations = new ArrayList<>();

	public BiomeDecorations addDecoration(GenerationStage.Decoration stage, ConfiguredFeature feature) {
		this.decorations.add(new Tuple<>(stage, feature));
		return this;
	}

	public ImmutableList<Tuple<GenerationStage.Decoration, ConfiguredFeature>> toImmutableList() {
		return ImmutableList.copyOf(this.decorations);
	}

	public static BiomeDecorations create() {
		return new BiomeDecorations();
	}
}
