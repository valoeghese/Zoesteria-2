package tk.valoeghese.zoesteria.core.serialisers.foliage;

import net.minecraft.world.gen.foliageplacer.AcaciaFoliagePlacer;
import tk.valoeghese.zoesteria.api.feature.IFoliagePlacerSerialiser;

public class AcaciaFoliagePlacerSerialiser extends BasicFoliagePlacerSerialiser<AcaciaFoliagePlacer> {
	protected AcaciaFoliagePlacerSerialiser(int radius, int radiusRandom) {
		super(radius, radiusRandom);
	}

	@Override
	protected IFoliagePlacerSerialiser<AcaciaFoliagePlacer> of(int radius, int radiusRandom) {
		return new AcaciaFoliagePlacerSerialiser(radius, radiusRandom);
	}

	@Override
	public AcaciaFoliagePlacer create() {
		return new AcaciaFoliagePlacer(this.radius, this.radiusRandom);
	}

	public static final AcaciaFoliagePlacerSerialiser BASE = new AcaciaFoliagePlacerSerialiser(0, 0);
}
