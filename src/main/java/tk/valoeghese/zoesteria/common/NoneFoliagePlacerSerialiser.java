package tk.valoeghese.zoesteria.common;

import tk.valoeghese.zoesteria.api.feature.IFoliagePlacerSerialiser;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public enum NoneFoliagePlacerSerialiser implements IFoliagePlacerSerialiser<NoneFoliagePlacer> {
	INSTANCE;

	@Override
	public IFoliagePlacerSerialiser<NoneFoliagePlacer> loadFrom(NoneFoliagePlacer placer) {
		return INSTANCE;
	}

	@Override
	public IFoliagePlacerSerialiser<NoneFoliagePlacer> deserialise(Container settings) {
		return INSTANCE;
	}

	@Override
	public void serialise(EditableContainer settings) {
	}

	@Override
	public NoneFoliagePlacer create() {
		return new NoneFoliagePlacer();
	}
}
