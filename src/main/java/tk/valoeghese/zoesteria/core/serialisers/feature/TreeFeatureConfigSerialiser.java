package tk.valoeghese.zoesteria.core.serialisers.feature;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import tk.valoeghese.zoesteria.api.ZFGUtils;
import tk.valoeghese.zoesteria.api.ZoesteriaSerialisers;
import tk.valoeghese.zoesteria.api.feature.IFeatureConfigSerialiser;
import tk.valoeghese.zoesteria.common.NoneFoliagePlacer;
import tk.valoeghese.zoesteria.core.serialisers.BlockStateProviderHandler;
import tk.valoeghese.zoesteriaconfig.api.ZoesteriaConfig;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class TreeFeatureConfigSerialiser implements IFeatureConfigSerialiser<TreeFeatureConfig> {
	private TreeFeatureConfigSerialiser(TreeFeatureConfig config) {
		this.leaves = config.leavesProvider;
		this.log = config.trunkProvider;
		this.foliagePlacer = config.foliagePlacer;
		this.minTrunkHeight = config.trunkHeight;
		this.maxTrunkHeight = config.trunkHeightRandom + config.trunkHeight;
		this.minFoliageDepth = config.foliageHeight;
		this.maxFoliageDepth = config.foliageHeightRandom + config.foliageHeight;
		this.baseHeight = config.baseHeight;
		this.heightRandA = config.heightRandA;
		this.heightRandB = config.heightRandB;
		this.maxBlocksUnderwater = config.maxWaterDepth;
		this.minTrunkTopOffset = config.trunkTopOffset;
		this.maxTrunkTopOffset = config.trunkTopOffsetRandom + config.trunkTopOffset;
		this.vines = !config.ignoreVines;
		this.decorators = config.decorators;
	}

	private TreeFeatureConfigSerialiser(BlockStateProvider leaves, BlockStateProvider log, FoliagePlacer placer, int lt, int ht, int lf, int hf, int bh, int hA, int hB, int mBU, int tOm, int tOM, boolean v, List<TreeDecorator> td) {
		this.leaves = leaves;
		this.log = log;
		this.foliagePlacer = placer;
		this.minTrunkHeight = lt;
		this.maxTrunkHeight = ht;
		this.minFoliageDepth = lf;
		this.maxFoliageDepth = hf;
		this.baseHeight = bh;
		this.heightRandA = hA;
		this.heightRandB = hB;
		this.maxBlocksUnderwater = mBU;
		this.minTrunkTopOffset = tOm;
		this.maxTrunkTopOffset = tOM;
		this.vines = v;
		this.decorators = td;
	}

	private final BlockStateProvider leaves;
	private final BlockStateProvider log;
	private final FoliagePlacer foliagePlacer;
	private final int baseHeight;
	private final int heightRandA;
	private final int heightRandB;
	private final int minTrunkHeight;
	private final int maxTrunkHeight;
	private final int minFoliageDepth;
	private final int maxFoliageDepth;
	private final int minTrunkTopOffset;
	private final int maxTrunkTopOffset;
	private final int maxBlocksUnderwater;
	private final boolean vines;
	private final List<TreeDecorator> decorators;

	@Override
	public IFeatureConfigSerialiser<TreeFeatureConfig> loadFrom(TreeFeatureConfig config) {
		return new TreeFeatureConfigSerialiser(config);
	}

	@Override
	public IFeatureConfigSerialiser<TreeFeatureConfig> deserialise(Container settings) {
		Integer maxBlocksUnderwater = settings.getIntegerValue("maxBlocksUnderwater");
		Boolean vines = settings.getBooleanValue("vines");
		Container foliagePlacer = settings.getContainer("foliagePlacer");

		List<TreeDecorator> decorators = settings.containsKey("decorators") ? settings.getList("decorators").stream()
				.map(obj -> {
					@SuppressWarnings("unchecked") Map<String, Object> decorator = (Map<String, Object>) obj;
					return ZoesteriaSerialisers.deserialiseTreeDecorator(ZoesteriaConfig.createWritableConfig(decorator));
				}).collect(Collectors.toList()) : ImmutableList.of();

		return new TreeFeatureConfigSerialiser(
				BlockStateProviderHandler.stateProvider(settings.getContainer("leaves")),
				BlockStateProviderHandler.stateProvider(settings.getContainer("log")),
				foliagePlacer == null ? new NoneFoliagePlacer() : ZoesteriaSerialisers.deserialiseFoliage(foliagePlacer), // TODO foliage placer
						ZFGUtils.getIntOrDefault(settings, "minTrunkHeight", -1),
						ZFGUtils.getIntOrDefault(settings, "maxTrunkHeight", -1),
						ZFGUtils.getIntOrDefault(settings, "minFoliageDepth", -1),
						ZFGUtils.getIntOrDefault(settings, "maxFoliageDepth", -1),
						settings.getIntegerValue("baseHeight"),
						settings.getIntegerValue("heightRandA"),
						settings.getIntegerValue("heightRandB"),
						(maxBlocksUnderwater == null ? 0 : maxBlocksUnderwater.intValue()),
						settings.getIntegerValue("minTrunkTopOffset"),
						settings.getIntegerValue("maxTrunkTopOffset"),
						(vines == null ? true : !vines.booleanValue()),
						decorators
				);
	}

	@Override
	public void serialise(EditableContainer settings) {
		settings.putMap("leaves", BlockStateProviderHandler.serialiseStateProvider(this.leaves).asMap());
		settings.putMap("log", BlockStateProviderHandler.serialiseStateProvider(this.log).asMap());

		EditableContainer foliagePlacer = ZoesteriaConfig.createWritableConfig(new LinkedHashMap<>());
		ZoesteriaSerialisers.serialiseFoliage(this.foliagePlacer, foliagePlacer);
		settings.putMap("foliagePlacer", foliagePlacer.asMap());

		settings.putIntegerValue("baseHeight", this.baseHeight);
		settings.putIntegerValue("heightRandA", this.heightRandA);
		settings.putIntegerValue("heightRandB", this.heightRandB);

		if (this.minTrunkHeight >= 0 || this.maxTrunkHeight >= 0) {
			settings.putIntegerValue("minTrunkHeight", this.minTrunkHeight);
			settings.putIntegerValue("maxTrunkHeight", this.maxTrunkHeight);
		}

		settings.putIntegerValue("minTrunkTopOffset", this.minTrunkTopOffset);
		settings.putIntegerValue("maxTrunkTopOffset", this.maxTrunkTopOffset);

		if (this.minFoliageDepth >= 0 || this.maxFoliageDepth >= 0) {
			settings.putIntegerValue("minFoliageDepth", this.minFoliageDepth);
			settings.putIntegerValue("maxFoliageDepth", this.maxFoliageDepth);
		}

		if (this.maxBlocksUnderwater != 0) {
			settings.putIntegerValue("maxBlocksUnderwater", this.maxBlocksUnderwater);
		}

		if (this.vines) {
			settings.putBooleanValue("vines", true);
		}
		
		if (!this.decorators.isEmpty()) {
			settings.putList("decorators", this.decorators.stream()
					.map(decorator -> ZoesteriaSerialisers.serialiseTreeDecorator(decorator,
							ZoesteriaConfig.createWritableConfig(new LinkedHashMap<>())).asMap()
							).collect(Collectors.toList()));
		}
	}

	@Override
	public TreeFeatureConfig create() {
		TreeFeatureConfig.Builder config = new TreeFeatureConfig.Builder(
				this.log,
				this.leaves,
				this.foliagePlacer)
				.baseHeight(this.baseHeight)
				.heightRandA(this.heightRandA)
				.heightRandB(this.heightRandB)
				.trunkHeight(this.minTrunkHeight)
				.trunkHeightRandom(this.maxTrunkHeight - this.minTrunkHeight)
				.foliageHeight(this.minFoliageDepth)
				.foliageHeightRandom(this.maxFoliageDepth - this.minFoliageDepth)
				.trunkTopOffset(this.minTrunkTopOffset)
				.trunkTopOffsetRandom(this.maxTrunkTopOffset - this.minTrunkTopOffset)
				.maxWaterDepth(this.maxBlocksUnderwater);

		if (!this.vines) {
			config.ignoreVines();
		}

		if (!this.decorators.isEmpty()) {
			config.decorators(this.decorators);
		}

		return config.build();
	}

	public static final TreeFeatureConfigSerialiser BASE = new TreeFeatureConfigSerialiser(null, null, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, null);
}
