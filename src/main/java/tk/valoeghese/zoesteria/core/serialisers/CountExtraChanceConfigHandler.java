package tk.valoeghese.zoesteria.core.serialisers;

import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import tk.valoeghese.zoesteria.api.feature.IZoesteriaPlacementConfig;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class CountExtraChanceConfigHandler implements IZoesteriaPlacementConfig<AtSurfaceWithExtraConfig> {
	private CountExtraChanceConfigHandler(AtSurfaceWithExtraConfig config) {
		this(config.count, config.extraChance, config.extraCount);
	}

	private CountExtraChanceConfigHandler(int count, float extraChance, int extraCount) {
		this.count = count;
		this.extraChance = extraChance;
		this.extraCount = extraCount;
	}

	private final int count;
	private final float extraChance;
	private final int extraCount;

	@Override
	public IZoesteriaPlacementConfig<AtSurfaceWithExtraConfig> loadFrom(AtSurfaceWithExtraConfig config) {
		return new CountExtraChanceConfigHandler(config);
	}

	@Override
	public IZoesteriaPlacementConfig<AtSurfaceWithExtraConfig> deserialise(Container settings) {
		return new CountExtraChanceConfigHandler(
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

	public static final CountExtraChanceConfigHandler BASE = new CountExtraChanceConfigHandler(0, 0, 0);
}
