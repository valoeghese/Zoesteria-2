package tk.valoeghese.zoesteria.api.biome;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.util.TriConsumer;

public final class BiomeTweaks {
	private final List<Triple<String, IBiomePredicate, BiomeDecorations>> tweaks = new ArrayList<>();

	public void addTweak(String fileName, IBiomePredicate predicate, BiomeDecorations decorations) {
		this.tweaks.add(new ImmutableTriple<>(fileName, predicate, decorations));
	}

	public void forEach(TriConsumer<String, IBiomePredicate, BiomeDecorations> callback) {
		this.tweaks.forEach(val -> callback.accept(val.getLeft(), val.getMiddle(), val.getRight()));
	}
	
	public boolean isEmpty() {
		return this.tweaks.isEmpty();
	}
}
