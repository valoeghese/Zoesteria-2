package tk.valoeghese.zoesteria.common.feature;

import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import tk.valoeghese.zoesteria.api.feature.IFeatureConfigSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.BlockStateProviderHandler;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class TreeLikeFeatureConfigSerialiser implements IFeatureConfigSerialiser<TreeLikeFeatureConfig> {
	private TreeLikeFeatureConfigSerialiser(BlockStateProvider log, int minSize, int maxSize) {
		this.log = log;
		this.minSize = minSize;
		this.maxSize = maxSize;
	}

	private final BlockStateProvider log;
	private final int minSize;
	private final int maxSize;

	@Override
	public IFeatureConfigSerialiser<TreeLikeFeatureConfig> loadFrom(TreeLikeFeatureConfig config) {
		final int minSize = config.baseSize;
		return new TreeLikeFeatureConfigSerialiser(config.logProvider, minSize, minSize + config.sizeRandom);
	}

	@Override
	public IFeatureConfigSerialiser<TreeLikeFeatureConfig> deserialise(Container settings) {
		return new TreeLikeFeatureConfigSerialiser(
				BlockStateProviderHandler.stateProvider(settings.getContainer("log")),
				settings.getIntegerValue("minSize"),
				settings.getIntegerValue("maxSize"));
	}

	@Override
	public void serialise(EditableContainer settings) {
		settings.putMap("log", BlockStateProviderHandler.serialiseStateProvider(this.log).asMap());
		settings.putIntegerValue("minSize", this.minSize);
		settings.putIntegerValue("maxSize", this.maxSize);
	}

	@Override
	public TreeLikeFeatureConfig create() {
		return new TreeLikeFeatureConfig(this.log, this.minSize, this.maxSize - this.minSize);
	}

	public static final TreeLikeFeatureConfigSerialiser BASE = new TreeLikeFeatureConfigSerialiser(null, 0, 0);
}
