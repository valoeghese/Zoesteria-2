package tk.valoeghese.zoesteria.core.serialisers.feature;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.BlockBlobConfig;
import tk.valoeghese.zoesteria.api.ZFGUtils;
import tk.valoeghese.zoesteria.api.feature.IFeatureConfigSerialiser;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class BlockBlobConfigSerialiser implements IFeatureConfigSerialiser<BlockBlobConfig> {
	public BlockBlobConfigSerialiser(BlockState state, int size) {
		this.state = state;
		this.startRadius = size;
	}

	private final BlockState state;
	private final int startRadius;

	@Override
	public IFeatureConfigSerialiser<BlockBlobConfig> loadFrom(BlockBlobConfig config) {
		return new BlockBlobConfigSerialiser(config.state, config.startRadius);
	}

	@Override
	public IFeatureConfigSerialiser<BlockBlobConfig> deserialise(Container settings) {
		return new BlockBlobConfigSerialiser(
				ZFGUtils.getBlockState(settings, "state"),
				settings.getIntegerValue("startRadius"));
	}

	@Override
	public void serialise(EditableContainer settings) {
		ZFGUtils.putBlockState(settings, "state", this.state);
		settings.putIntegerValue("startRadius", this.startRadius);
	}

	@Override
	public BlockBlobConfig create() {
		return new BlockBlobConfig(this.state, this.startRadius);
	}

	public static final BlockBlobConfigSerialiser BASE = new BlockBlobConfigSerialiser(null, 0);
}
