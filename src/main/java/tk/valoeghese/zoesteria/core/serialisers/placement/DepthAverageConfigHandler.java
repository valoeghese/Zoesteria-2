package tk.valoeghese.zoesteria.core.serialisers.placement;

import net.minecraft.world.gen.placement.DepthAverageConfig;
import tk.valoeghese.zoesteria.api.feature.IZoesteriaPlacementConfig;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class DepthAverageConfigHandler implements IZoesteriaPlacementConfig<DepthAverageConfig> {
	private DepthAverageConfigHandler(DepthAverageConfig config) {
		this(config.count, config.baseline, config.spread);
	}

	private DepthAverageConfigHandler(int count, int baseline, int spread) {
		this.count = count;
		this.baseline = baseline;
		this.spread = spread;
	}

	private final int count;
	private final int baseline;
	private final int spread;

	@Override
	public IZoesteriaPlacementConfig<DepthAverageConfig> loadFrom(DepthAverageConfig config) {
		return new DepthAverageConfigHandler(config);
	}

	@Override
	public IZoesteriaPlacementConfig<DepthAverageConfig> deserialise(Container settings) {
		return new DepthAverageConfigHandler(
				settings.getIntegerValue("count"),
				settings.getIntegerValue("baseline"),
				settings.getIntegerValue("spread"));
	}

	@Override
	public void serialise(EditableContainer settings) {
		settings.putIntegerValue("count", this.count);
		settings.putIntegerValue("baseline", this.baseline);
		settings.putIntegerValue("spread", this.spread);
	}

	@Override
	public DepthAverageConfig create() {
		return new DepthAverageConfig(this.count, this.baseline, this.spread);
	}

	public static final DepthAverageConfigHandler BASE = new DepthAverageConfigHandler(0, 0, 0);
}
