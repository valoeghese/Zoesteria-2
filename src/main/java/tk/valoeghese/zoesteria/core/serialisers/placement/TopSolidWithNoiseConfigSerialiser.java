package tk.valoeghese.zoesteria.core.serialisers.placement;

import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.placement.TopSolidWithNoiseConfig;
import tk.valoeghese.zoesteria.api.feature.IPlacementConfigSerialiser;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class TopSolidWithNoiseConfigSerialiser implements IPlacementConfigSerialiser<TopSolidWithNoiseConfig> {
	private TopSolidWithNoiseConfigSerialiser(int amplitude, double stretch, double offset, Heightmap.Type heightmap) {
		this.amplitude = amplitude;
		this.stretch = stretch;
		this.offset = offset;
		this.heightmap = heightmap;
	}

	private final int amplitude;
	private final double stretch;
	private final double offset;
	private final Heightmap.Type heightmap;

	@Override
	public IPlacementConfigSerialiser<TopSolidWithNoiseConfig> loadFrom(TopSolidWithNoiseConfig config) {
		return new TopSolidWithNoiseConfigSerialiser(config.noiseToCountRatio, config.noiseFactor, config.noiseOffset, config.heightmap);
	}

	@Override
	public IPlacementConfigSerialiser<TopSolidWithNoiseConfig> deserialise(Container settings) {
		return new TopSolidWithNoiseConfigSerialiser(
				settings.getIntegerValue("amplitude"),
				settings.getDoubleValue("stretch"),
				settings.getDoubleValue("offset"),
				Heightmap.Type.getTypeFromId(settings.getStringValue("heightmap")));
	}

	@Override
	public void serialise(EditableContainer settings) {
		settings.putIntegerValue("amplitude", this.amplitude);
		settings.putDoubleValue("stretch", this.stretch);
		settings.putDoubleValue("offset", this.offset);
		settings.putStringValue("heightmap", this.heightmap.getId());
	}

	@Override
	public TopSolidWithNoiseConfig create() {
		return new TopSolidWithNoiseConfig(this.amplitude, this.stretch, this.offset, this.heightmap);
	}

	public static final TopSolidWithNoiseConfigSerialiser BASE = new TopSolidWithNoiseConfigSerialiser(0, 0, 0, null);
}
