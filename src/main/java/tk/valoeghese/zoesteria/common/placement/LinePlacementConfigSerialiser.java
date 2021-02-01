package tk.valoeghese.zoesteria.common.placement;

import tk.valoeghese.zoesteria.api.feature.IPlacementConfigSerialiser;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class LinePlacementConfigSerialiser implements IPlacementConfigSerialiser<LinePlacementConfig> {
	private LinePlacementConfigSerialiser(int count, double freq, double thresh, double off) {
		this.count = count;
		this.freq = freq;
		this.thresh = thresh;
		this.off = off;
	}

	private int count;
	private double thresh;
	private double freq;
	private double off;

	@Override
	public IPlacementConfigSerialiser<LinePlacementConfig> loadFrom(LinePlacementConfig config) {
		return new LinePlacementConfigSerialiser(config.count, config.frequency, config.threshold, config.offset);
	}

	@Override
	public IPlacementConfigSerialiser<LinePlacementConfig> deserialise(Container settings) {
		return new LinePlacementConfigSerialiser(
				settings.getIntegerValue("count"),
				settings.getDoubleValue("frequency"),
				settings.getDoubleValue("threshold"),
				settings.getDoubleValue("offset"));
	}

	@Override
	public void serialise(EditableContainer settings) {
		settings.putIntegerValue("count", this.count);
		settings.putDoubleValue("frequency", this.freq);
		settings.putDoubleValue("threshold", this.thresh);
		settings.putDoubleValue("offset", this.off);
	}

	@Override
	public LinePlacementConfig create() {
		return new LinePlacementConfig(this.count, this.freq, this.thresh);
	}

	public static final LinePlacementConfigSerialiser BASE = new LinePlacementConfigSerialiser(0, 0, 0, 0);
}
