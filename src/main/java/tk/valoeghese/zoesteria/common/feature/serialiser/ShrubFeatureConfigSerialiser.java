package tk.valoeghese.zoesteria.common.feature.serialiser;

import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import tk.valoeghese.zoesteria.api.feature.IFeatureConfigSerialiser;
import tk.valoeghese.zoesteria.common.feature.ShrubFeatureConfig;
import tk.valoeghese.zoesteria.core.serialisers.BlockStateProviderHandler;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class ShrubFeatureConfigSerialiser implements IFeatureConfigSerialiser<ShrubFeatureConfig> {
	private ShrubFeatureConfigSerialiser(BlockStateProvider log, BlockStateProvider leaves) {
		this.log = log;
		this.leaves = leaves;
	}

	private final BlockStateProvider log;
	private final BlockStateProvider leaves;

	@Override
	public IFeatureConfigSerialiser<ShrubFeatureConfig> loadFrom(ShrubFeatureConfig config) {
		return new ShrubFeatureConfigSerialiser(config.logProvider, config.leavesProvider);
	}

	@Override
	public IFeatureConfigSerialiser<ShrubFeatureConfig> deserialise(Container settings) {
		return new ShrubFeatureConfigSerialiser(
				BlockStateProviderHandler.stateProvider(settings.getContainer("log")),
				BlockStateProviderHandler.stateProvider(settings.getContainer("leaves")));
	}

	@Override
	public void serialise(EditableContainer settings) {
		settings.putMap("log", BlockStateProviderHandler.serialiseStateProvider(this.log).asMap());
		settings.putMap("leaves", BlockStateProviderHandler.serialiseStateProvider(this.leaves).asMap());
	}

	@Override
	public ShrubFeatureConfig create() {
		return new ShrubFeatureConfig(this.log, this.leaves);
	}

	public static final ShrubFeatureConfigSerialiser BASE = new ShrubFeatureConfigSerialiser(null, null);
}
