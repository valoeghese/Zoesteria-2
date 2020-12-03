package tk.valoeghese.zoesteria.core.serialisers;

import net.minecraft.world.gen.placement.CountRangeConfig;
import tk.valoeghese.zoesteria.api.feature.IZoesteriaPlacementConfig;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class CountRangeConfigHandler implements IZoesteriaPlacementConfig<CountRangeConfig> {
	private CountRangeConfigHandler(CountRangeConfig config) {
		this(config.topOffset, config.bottomOffset, config.count, config.maximum);
	}

	private CountRangeConfigHandler(int topOffset, int bottomOffset, int count, int maximum) {
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
	public IZoesteriaPlacementConfig<CountRangeConfig> loadFrom(CountRangeConfig config) {
		return new CountRangeConfigHandler(config);
	}

	@Override
	public IZoesteriaPlacementConfig<CountRangeConfig> deserialise(Container settings) {
		return new CountRangeConfigHandler(
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

	public static final CountRangeConfigHandler BASE = new CountRangeConfigHandler(0, 0, 0, 0);
}
