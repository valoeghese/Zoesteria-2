package tk.valoeghese.zoesteria.core.serialisers.placement;

import net.minecraft.world.gen.placement.HeightWithChanceConfig;
import tk.valoeghese.zoesteria.api.feature.IPlacementConfigSerialiser;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class HeightChanceConfigSerialiser implements IPlacementConfigSerialiser<HeightWithChanceConfig> {
	private HeightChanceConfigSerialiser(HeightWithChanceConfig config) {
		this.count = config.count;
		this.chance = config.chance;
	}

	private HeightChanceConfigSerialiser(int count, float chance) {
		this.count = count;
		this.chance = chance;
	}

	private final int count;
	private final float chance;

	@Override
	public IPlacementConfigSerialiser<HeightWithChanceConfig> loadFrom(HeightWithChanceConfig config) {
		return new HeightChanceConfigSerialiser(config);
	}

	@Override
	public IPlacementConfigSerialiser<HeightWithChanceConfig> deserialise(Container settings) {
		int count = settings.getIntegerValue("count");
		float chance = settings.getFloatValue("chance");
		return new HeightChanceConfigSerialiser(count, chance);
	}

	@Override
	public void serialise(EditableContainer settings) {
		settings.putIntegerValue("count", this.count);
		settings.putFloatValue("chance", this.chance);
	}

	@Override
	public HeightWithChanceConfig create() {
		return new HeightWithChanceConfig(this.count, this.chance);
	}
	
	public static final HeightChanceConfigSerialiser BASE = new HeightChanceConfigSerialiser(0, 0.0F);
}
