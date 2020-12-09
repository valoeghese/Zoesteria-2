package tk.valoeghese.zoesteria.core.serialisers.foliage;

import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import tk.valoeghese.zoesteria.api.feature.IFoliagePlacerSerialiser;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class BlobFoliagePlacerSerialiser implements IFoliagePlacerSerialiser<BlobFoliagePlacer> {
	private BlobFoliagePlacerSerialiser(BlobFoliagePlacer placer) {
		this(placer.field_227381_a_, placer.field_227382_b_);
	}

	private BlobFoliagePlacerSerialiser(int radius, int radiusRandom) {
		this.radius = radius;
		this.radiusRandom = radiusRandom;
	}

	private final int radius;
	private final int radiusRandom;

	@Override
	public IFoliagePlacerSerialiser<BlobFoliagePlacer> loadFrom(BlobFoliagePlacer placer) {
		return new BlobFoliagePlacerSerialiser(placer);
	}

	@Override
	public IFoliagePlacerSerialiser<BlobFoliagePlacer> deserialise(Container settings) {
		return new BlobFoliagePlacerSerialiser(settings.getIntegerValue("radius"), settings.getIntegerValue("radiusRandom"));
	}

	@Override
	public void serialise(EditableContainer settings) {
		settings.putIntegerValue("radius", this.radius);
		settings.putIntegerValue("radiusRandom", this.radiusRandom);
	}

	@Override
	public BlobFoliagePlacer create() {
		return new BlobFoliagePlacer(this.radius, this.radiusRandom);
	}

	public static final BlobFoliagePlacerSerialiser BASE = new BlobFoliagePlacerSerialiser(0, 0);
}
