package tk.valoeghese.zoesteria.core.serialisers.placement;

import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import tk.valoeghese.zoesteria.api.feature.IPlacementConfigSerialiser;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class CountExtraChanceConfigSerialiser implements IPlacementConfigSerialiser<AtSurfaceWithExtraConfig> {
	private CountExtraChanceConfigSerialiser(AtSurfaceWithExtraConfig config) {
		this(config.count, config.extraChance, config.extraCount);
	}

	private CountExtraChanceConfigSerialiser(int count, float extraChance, int extraCount) {
		this.count = count;
		this.extraChance = extraChance;
		this.extraCount = extraCount;
	}

	private final int count;
	private final float extraChance;
	private final int extraCount;

	@Override
	public IPlacementConfigSerialiser<AtSurfaceWithExtraConfig> loadFrom(AtSurfaceWithExtraConfig config) {
		return new CountExtraChanceConfigSerialiser(config);
	}

	@Override
	public IPlacementConfigSerialiser<AtSurfaceWithExtraConfig> deserialise(Container settings) {
		return new CountExtraChanceConfigSerialiser(
				settings.getIntegerValue("count"),
				settings.getFloatValue("extraChance"),
				settings.getIntegerValue("extraCount"));
	}

	@Override
	public void serialise(EditableContainer settings) {
		settings.putIntegerValue("count", this.count);
		settings.putFloatValue("extraChance", this.extraChance);
		settings.putIntegerValue("extraCount", this.extraCount);
	}

	@Override
	public AtSurfaceWithExtraConfig create() {
		return new AtSurfaceWithExtraConfig(this.count, this.extraChance, this.extraCount);
	}

	public static final CountExtraChanceConfigSerialiser BASE = new CountExtraChanceConfigSerialiser(0, 0, 0);
}
