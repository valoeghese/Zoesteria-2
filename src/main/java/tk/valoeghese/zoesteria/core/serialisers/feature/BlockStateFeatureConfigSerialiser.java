package tk.valoeghese.zoesteria.core.serialisers.feature;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import tk.valoeghese.zoesteria.api.ZFGUtils;
import tk.valoeghese.zoesteria.api.feature.IFeatureConfigSerialiser;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class BlockStateFeatureConfigSerialiser implements IFeatureConfigSerialiser<BlockStateFeatureConfig> {
	private BlockStateFeatureConfigSerialiser(BlockState state) {
		this.state = state;
	}

	private final BlockState state;

	@Override
	public IFeatureConfigSerialiser<BlockStateFeatureConfig> loadFrom(BlockStateFeatureConfig config) {
		return new BlockStateFeatureConfigSerialiser(config.state);
	}

	@Override
	public IFeatureConfigSerialiser<BlockStateFeatureConfig> deserialise(Container settings) {
		return new BlockStateFeatureConfigSerialiser(ZFGUtils.getBlockState(settings, "state"));
	}

	@Override
	public void serialise(EditableContainer settings) {
		ZFGUtils.putBlockState(settings, "state", this.state);
	}

	@Override
	public BlockStateFeatureConfig create() {
		return new BlockStateFeatureConfig(this.state);
	}

	public static final BlockStateFeatureConfigSerialiser BASE = new BlockStateFeatureConfigSerialiser(null);
}
