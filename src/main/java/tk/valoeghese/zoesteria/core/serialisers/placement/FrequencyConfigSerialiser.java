package tk.valoeghese.zoesteria.core.serialisers.placement;

import net.minecraft.world.gen.placement.FrequencyConfig;
import tk.valoeghese.zoesteria.api.feature.IPlacementConfigSerialiser;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class FrequencyConfigSerialiser implements IPlacementConfigSerialiser<FrequencyConfig> {
	private FrequencyConfigSerialiser(FrequencyConfig config) {
		this(config.count);
	}

	private FrequencyConfigSerialiser(int count) {
		this.count = count;
	}

	private final int count;

	@Override
	public IPlacementConfigSerialiser<FrequencyConfig> loadFrom(FrequencyConfig config) {
		return new FrequencyConfigSerialiser(config);
	}

	@Override
	public IPlacementConfigSerialiser<FrequencyConfig> deserialise(Container settings) {
		return new FrequencyConfigSerialiser(settings.getIntegerValue("count"));
	}

	@Override
	public void serialise(EditableContainer settings) {
		settings.putIntegerValue("count", this.count);
	}

	@Override
	public FrequencyConfig create() {
		return new FrequencyConfig(this.count);
	}

	public static final FrequencyConfigSerialiser BASE = new FrequencyConfigSerialiser(0);
}
