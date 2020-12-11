package tk.valoeghese.zoesteria.core.serialisers.placement;

import net.minecraft.world.gen.placement.CountRangeConfig;
import tk.valoeghese.zoesteria.api.feature.IPlacementConfigSerialiser;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class CountRangeConfigSerialiser implements IPlacementConfigSerialiser<CountRangeConfig> {
	private CountRangeConfigSerialiser(CountRangeConfig config) {
		this(config.topOffset, config.bottomOffset, config.count, config.maximum);
	}

	private CountRangeConfigSerialiser(int topOffset, int bottomOffset, int count, int maximum) {
		this.topOffset = topOffset;
		this.bottomOffset = bottomOffset;
		this.count = count;
		this.maximum = maximum;
	}

	private final int topOffset;
	private final int bottomOffset;
	private final int count;
	private final int maximum;

	@Override
	public IPlacementConfigSerialiser<CountRangeConfig> loadFrom(CountRangeConfig config) {
		return new CountRangeConfigSerialiser(config);
	}

	@Override
	public IPlacementConfigSerialiser<CountRangeConfig> deserialise(Container settings) {
		return new CountRangeConfigSerialiser(
				settings.getIntegerValue("topOffset"),
				settings.getIntegerValue("bottomOffset"),
				settings.getIntegerValue("count"),
				settings.getIntegerValue("maximum"));
	}

	@Override
	public void serialise(EditableContainer settings) {
		// this order is the order in CountRangeConfig so using this order for serialisation
		settings.putIntegerValue("count", this.count);
		settings.putIntegerValue("bottomOffset", this.bottomOffset);
		settings.putIntegerValue("topOffset", this.topOffset);
		settings.putIntegerValue("maximum", this.maximum);
	}

	@Override
	public CountRangeConfig create() {
		return new CountRangeConfig(this.count, this.bottomOffset, this.topOffset, this.maximum);
	}

	public static final CountRangeConfigSerialiser BASE = new CountRangeConfigSerialiser(0, 0, 0, 0);
}
