package tk.valoeghese.zoesteria.core.serialisers.foliage;

import net.minecraft.world.gen.foliageplacer.PineFoliagePlacer;
import tk.valoeghese.zoesteria.api.feature.IFoliagePlacerSerialiser;

public class PineFoliagePlacerSerialiser extends BasicFoliagePlacerSerialiser<PineFoliagePlacer> {
	protected PineFoliagePlacerSerialiser(int radius, int radiusRandom) {
		super(radius, radiusRandom);
	}

	@Override
	protected IFoliagePlacerSerialiser<PineFoliagePlacer> of(int radius, int radiusRandom) {
		return new PineFoliagePlacerSerialiser(radius, radiusRandom);
	}

	@Override
	public PineFoliagePlacer create() {
		return new PineFoliagePlacer(this.radius, this.radiusRandom);
	}

	public static final PineFoliagePlacerSerialiser BASE = new PineFoliagePlacerSerialiser(0, 0);
}
