package tk.valoeghese.zoesteria.core.serialisers.foliage;

import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import tk.valoeghese.zoesteria.api.feature.IFoliagePlacerSerialiser;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public abstract class BasicFoliagePlacerSerialiser<T extends FoliagePlacer> implements IFoliagePlacerSerialiser<T> {
	protected BasicFoliagePlacerSerialiser(int radius, int radiusRandom) {
		this.radius = radius;
		this.radiusRandom = radiusRandom;
	}

	protected final int radius;
	protected final int radiusRandom;

	protected abstract IFoliagePlacerSerialiser<T> of(int radius, int radiusRandom);

	@Override
	public IFoliagePlacerSerialiser<T> loadFrom(T placer) {
		return this.of(placer.field_227381_a_, placer.field_227382_b_);
	}

	@Override
	public IFoliagePlacerSerialiser<T> deserialise(Container settings) {
		return this.of(settings.getIntegerValue("radius"), settings.getIntegerValue("radiusRandom"));
	}

	@Override
	public void serialise(EditableContainer settings) {
		settings.putIntegerValue("radius", this.radius);
		settings.putIntegerValue("radiusRandom", this.radiusRandom);
	}
}
