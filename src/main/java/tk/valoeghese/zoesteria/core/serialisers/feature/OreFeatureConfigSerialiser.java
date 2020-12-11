package tk.valoeghese.zoesteria.core.serialisers.feature;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType;
import tk.valoeghese.zoesteria.api.ZFGUtils;
import tk.valoeghese.zoesteria.api.feature.IFeatureConfigSerialiser;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class OreFeatureConfigSerialiser implements IFeatureConfigSerialiser<OreFeatureConfig> {
	private OreFeatureConfigSerialiser(OreFeatureConfig config) {
		this.size = config.size;
		this.state = config.state;
		this.target = config.target;
	}

	private OreFeatureConfigSerialiser(int size, BlockState state, FillerBlockType target) {
		this.size = size;
		this.state = state;
		this.target = target;
	}

	private int size;
	private BlockState state;
	private FillerBlockType target;

	@Override
	public IFeatureConfigSerialiser<OreFeatureConfig> loadFrom(OreFeatureConfig config) {
		return new OreFeatureConfigSerialiser(config);
	}

	@Override
	public IFeatureConfigSerialiser<OreFeatureConfig> deserialise(Container settings) {
		return new OreFeatureConfigSerialiser(
				settings.getIntegerValue("size"),
				ZFGUtils.getBlockState(settings, "state"),
				FillerBlockType.byName(settings.getStringValue("target")));
	}

	@Override
	public void serialise(EditableContainer settings) {
		settings.putIntegerValue("size", this.size);
		ZFGUtils.putBlockState(settings, "state", this.state);
		settings.putStringValue("target", this.target.getName());
	}

	@Override
	public OreFeatureConfig create() {
		return new OreFeatureConfig(this.target, this.state, this.size);
	}

	public static final OreFeatureConfigSerialiser BASE = new OreFeatureConfigSerialiser(0, null, null);
}
