package tk.valoeghese.zoesteria.core.serialisers.foliage;

import net.minecraft.world.gen.foliageplacer.SpruceFoliagePlacer;
import tk.valoeghese.zoesteria.api.feature.IFoliagePlacerSerialiser;

public class SpruceFoliagePlacerSerialiser extends BasicFoliagePlacerSerialiser<SpruceFoliagePlacer> {
	protected SpruceFoliagePlacerSerialiser(int radius, int radiusRandom) {
		super(radius, radiusRandom);
	}

	@Override
	protected IFoliagePlacerSerialiser<SpruceFoliagePlacer> of(int radius, int radiusRandom) {
		return new SpruceFoliagePlacerSerialiser(radius, radiusRandom);
	}

	@Override
	public SpruceFoliagePlacer create() {
		return new SpruceFoliagePlacer(this.radius, this.radiusRandom);
	}

	public static final SpruceFoliagePlacerSerialiser BASE = new SpruceFoliagePlacerSerialiser(0, 0);
}
