package tk.valoeghese.zoesteria.core.serialisers.feature;

import net.minecraft.world.gen.feature.structure.VillageConfig;
import tk.valoeghese.zoesteria.api.feature.IFeatureConfigSerialiser;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public final class VillageConfigSerialiser implements IFeatureConfigSerialiser<VillageConfig> {
	private VillageConfigSerialiser(String startPool, int size) {
		this.startPool = startPool;
		this.size = size;
	}

	private final String startPool;
	private final int size;

	@Override
	public IFeatureConfigSerialiser<VillageConfig> loadFrom(VillageConfig config) {
		return new VillageConfigSerialiser(config.startPool.toString(), config.size);
	}

	@Override
	public IFeatureConfigSerialiser<VillageConfig> deserialise(Container settings) {
		return new VillageConfigSerialiser(
				settings.getStringValue("startPool"),
				settings.getIntegerValue("size"));
	}

	@Override
	public void serialise(EditableContainer settings) {
		settings.putStringValue("startPool", this.startPool);
		settings.putIntegerValue("size", this.size);
	}

	@Override
	public VillageConfig create() {
		return new VillageConfig(this.startPool, this.size);
	}

	public static final VillageConfigSerialiser BASE = new VillageConfigSerialiser(null, 0);
}
