package tk.valoeghese.zoesteria.core.serialisers.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.SphereReplaceConfig;
import tk.valoeghese.zoesteria.api.ZFGUtils;
import tk.valoeghese.zoesteria.api.feature.IFeatureConfigSerialiser;
import tk.valoeghese.zoesteriaconfig.api.ZoesteriaConfig;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class SphereReplaceConfigSerialiser implements IFeatureConfigSerialiser<SphereReplaceConfig> {
	private SphereReplaceConfigSerialiser(BlockState state, int radius, int ySize, List<BlockState> targets) {
		this.state = state;
		this.radius = radius;
		this.ySize = ySize;
		this.targets = targets;
	}

	private final BlockState state;
	private final int radius;
	private final int ySize;
	private final List<BlockState> targets;

	@Override
	public IFeatureConfigSerialiser<SphereReplaceConfig> loadFrom(SphereReplaceConfig config) {
		return new SphereReplaceConfigSerialiser(config.state, config.radius, config.ySize, config.targets);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IFeatureConfigSerialiser<SphereReplaceConfig> deserialise(Container settings) {
		List<Object> targetsData = settings.getList("targets");
		List<BlockState> targets = new ArrayList<>();

		for (Object o : targetsData) {
			targets.add(ZFGUtils.deserialiseStateContainer(ZoesteriaConfig.createWritableConfig((Map<String, Object>) o)));
		}

		return new SphereReplaceConfigSerialiser(
				ZFGUtils.getBlockState(settings, "state"),
				settings.getIntegerValue("radius"),
				settings.getIntegerValue("ySize"),
				targets);
	}

	@Override
	public void serialise(EditableContainer settings) {
		ZFGUtils.putBlockState(settings, "state", this.state);
		settings.putIntegerValue("radius", this.radius);
		settings.putIntegerValue("ySize", this.ySize);

		List<Object> targetsData = new ArrayList<>();

		for (BlockState state : this.targets) {
			targetsData.add(ZFGUtils.serialiseStateContainer(state));
		}

		settings.putList("targets", targetsData);
	}

	@Override
	public SphereReplaceConfig create() {
		return new SphereReplaceConfig(this.state, this.radius, this.ySize, this.targets);
	}

	public static final SphereReplaceConfigSerialiser BASE = new SphereReplaceConfigSerialiser(null, 0, 0, null);
}
