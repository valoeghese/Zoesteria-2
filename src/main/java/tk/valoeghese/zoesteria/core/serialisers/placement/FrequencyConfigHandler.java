package tk.valoeghese.zoesteria.core.serialisers.placement;

import net.minecraft.world.gen.placement.FrequencyConfig;
import tk.valoeghese.zoesteria.api.feature.IPlacementConfigSerialiser;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class FrequencyConfigHandler implements IPlacementConfigSerialiser<FrequencyConfig> {
	private FrequencyConfigHandler(FrequencyConfig config) {
		this(config.count);
	}

	private FrequencyConfigHandler(int count) {
		this.count = count;
	}

	private final int count;

	@Override
	public IPlacementConfigSerialiser<FrequencyConfig> loadFrom(FrequencyConfig config) {
		return new FrequencyConfigHandler(config);
	}

	@Override
	public IPlacementConfigSerialiser<FrequencyConfig> deserialise(Container settings) {
		return new FrequencyConfigHandler(settings.getIntegerValue("count"));
	}

	@Override
	public void serialise(EditableContainer settings) {
		settings.putIntegerValue("count", this.count);
	}

	@Override
	public FrequencyConfig create() {
		return new FrequencyConfig(this.count);
	}

	public static final FrequencyConfigHandler BASE = new FrequencyConfigHandler(0);
}
