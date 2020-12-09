package tk.valoeghese.zoesteria.core.serialisers.placement;

import net.minecraft.world.gen.placement.ChanceConfig;
import tk.valoeghese.zoesteria.api.feature.IPlacementConfigSerialiser;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class ChanceConfigHandler implements IPlacementConfigSerialiser<ChanceConfig> {
	private ChanceConfigHandler(ChanceConfig config) {
		this(config.chance);
	}

	private ChanceConfigHandler(int rarity) {
		this.rarity = rarity;
	}

	private final int rarity;

	@Override
	public IPlacementConfigSerialiser<ChanceConfig> loadFrom(ChanceConfig config) {
		return new ChanceConfigHandler(config);
	}

	@Override
	public IPlacementConfigSerialiser<ChanceConfig> deserialise(Container settings) {
		return new ChanceConfigHandler(settings.getIntegerValue("rarity"));
	}

	@Override
	public void serialise(EditableContainer settings) {
		settings.putIntegerValue("rarity", this.rarity);
	}

	@Override
	public ChanceConfig create() {
		return new ChanceConfig(this.rarity);
	}

	public static final ChanceConfigHandler BASE = new ChanceConfigHandler(0);
}
