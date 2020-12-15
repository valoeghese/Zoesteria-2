package tk.valoeghese.zoesteria.core.serialisers.foliage;

import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import tk.valoeghese.zoesteria.api.feature.IFoliagePlacerSerialiser;

public class BlobFoliagePlacerSerialiser extends BasicFoliagePlacerSerialiser<BlobFoliagePlacer> {
	protected BlobFoliagePlacerSerialiser(int radius, int radiusRandom) {
		super(radius, radiusRandom);
	}

	@Override
	protected IFoliagePlacerSerialiser<BlobFoliagePlacer> of(int radius, int radiusRandom) {
		return new BlobFoliagePlacerSerialiser(radius, radiusRandom);
	}

	@Override
	public BlobFoliagePlacer create() {
		return new BlobFoliagePlacer(this.radius, this.radiusRandom);
	}

	public static final BlobFoliagePlacerSerialiser BASE = new BlobFoliagePlacerSerialiser(0, 0);
}
