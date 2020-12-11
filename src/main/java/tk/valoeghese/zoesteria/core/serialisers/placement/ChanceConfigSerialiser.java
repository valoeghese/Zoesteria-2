package tk.valoeghese.zoesteria.core.serialisers.placement;

import net.minecraft.world.gen.placement.ChanceConfig;
import tk.valoeghese.zoesteria.api.feature.IPlacementConfigSerialiser;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class ChanceConfigSerialiser implements IPlacementConfigSerialiser<ChanceConfig> {
	private ChanceConfigSerialiser(ChanceConfig config) {
		this(config.chance);
	}

	private ChanceConfigSerialiser(int rarity) {
		this.rarity = rarity;
	}

	private final int rarity;

	@Override
	public IPlacementConfigSerialiser<ChanceConfig> loadFrom(ChanceConfig config) {
		return new ChanceConfigSerialiser(config);
	}

	@Override
	public IPlacementConfigSerialiser<ChanceConfig> deserialise(Container settings) {
		return new ChanceConfigSerialiser(settings.getIntegerValue("rarity"));
	}

	@Override
	public void serialise(EditableContainer settings) {
		settings.putIntegerValue("rarity", this.rarity);
	}

	@Override
	public ChanceConfig create() {
		return new ChanceConfig(this.rarity);
	}

	public static final ChanceConfigSerialiser BASE = new ChanceConfigSerialiser(0);
}
