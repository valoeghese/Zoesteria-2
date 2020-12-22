package tk.valoeghese.zoesteria.core.serialisers.feature;

import net.minecraft.world.gen.blockplacer.BlockPlacer;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import tk.valoeghese.zoesteria.api.feature.IFeatureConfigSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.BlockPlacerHandler;
import tk.valoeghese.zoesteria.core.serialisers.BlockStateProviderHandler;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class BlockClusterConfigSerialiser implements IFeatureConfigSerialiser<BlockClusterFeatureConfig> {
	private BlockClusterConfigSerialiser(BlockClusterFeatureConfig config) {
		this(config.stateProvider, config.blockPlacer, config.tryCount, config.xSpread, config.ySpread, config.zSpread, config.isReplaceable, config.requiresWater);
	}

	private BlockClusterConfigSerialiser(BlockStateProvider stateProvider, BlockPlacer blockPlacer, int tryCount, int xSpread, int ySpread, int zSpread, boolean isReplaceable, boolean requiresWater) {
		this.stateProvider = stateProvider;
		this.blockPlacer = blockPlacer;
		this.tries = tryCount;
		this.xSpread = xSpread;
		this.ySpread = ySpread;
		this.zSpread = zSpread;
		this.isReplaceable = isReplaceable;
		this.requiresWater = requiresWater;
	}

	private final BlockStateProvider stateProvider;
	private final BlockPlacer blockPlacer;
	private final int tries;
	private final int xSpread;
	private final int ySpread;
	private final int zSpread;
	private final boolean isReplaceable;
	private final boolean requiresWater;

	@Override
	public IFeatureConfigSerialiser<BlockClusterFeatureConfig> loadFrom(BlockClusterFeatureConfig config) {
		return new BlockClusterConfigSerialiser(config);
	}

	@Override
	public IFeatureConfigSerialiser<BlockClusterFeatureConfig> deserialise(Container settings) {
		return new BlockClusterConfigSerialiser(
				BlockStateProviderHandler.stateProvider(settings.getContainer("stateProvider")),
				BlockPlacerHandler.deserialize(settings.getContainer("blockPlacer")),
				settings.getIntegerValue("tries"),
				settings.getIntegerValue("xSpread"),
				settings.getIntegerValue("ySpread"),
				settings.getIntegerValue("zSpread"),
				settings.getBooleanValue("isReplaceable"),
				settings.getBooleanValue("requiresWater")
				);
	}

	@Override
	public void serialise(EditableContainer settings) {
		settings.putMap("stateProvider", BlockStateProviderHandler.serialiseStateProvider(this.stateProvider).asMap());

		settings.putMap("blockPlacer", BlockPlacerHandler.serialize(this.blockPlacer).asMap());
		settings.putIntegerValue("tries", this.tries);
		settings.putIntegerValue("xSpread", this.xSpread);
		settings.putIntegerValue("ySpread", this.ySpread);
		settings.putIntegerValue("zSpread", this.zSpread);
		settings.putBooleanValue("isReplaceable", this.isReplaceable);
		settings.putBooleanValue("requiresWater", this.requiresWater);
	}

	@Override
	public BlockClusterFeatureConfig create() {
		BlockClusterFeatureConfig.Builder result = new BlockClusterFeatureConfig.Builder(this.stateProvider, this.blockPlacer)
				.tries(this.tries)
				.xSpread(this.xSpread)
				.ySpread(this.ySpread)
				.zSpread(this.zSpread);

		if (this.isReplaceable) {
			result.replaceable();
		}

		if (this.requiresWater) {
			result.requiresWater();
		}

		return result.build();
	}

	public static final BlockClusterConfigSerialiser BASE = new BlockClusterConfigSerialiser(null, null, 0, 0, 0, 0, false, false);
}
