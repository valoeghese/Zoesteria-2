package tk.valoeghese.zoesteria.core.serialisers.feature;

import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import tk.valoeghese.zoesteria.api.ZFGUtils;
import tk.valoeghese.zoesteria.api.feature.FeatureSerialisers;
import tk.valoeghese.zoesteria.api.feature.IFeatureConfigSerialiser;
import tk.valoeghese.zoesteria.core.NoneFoliagePlacer;
import tk.valoeghese.zoesteria.core.serialisers.BlockStateProviderHandler;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class TreeFeatureConfigHandler implements IFeatureConfigSerialiser<TreeFeatureConfig> {
	private TreeFeatureConfigHandler(TreeFeatureConfig config) {
		this.leaves = config.leavesProvider;
		this.log = config.trunkProvider;
		this.foliagePlacer = config.foliagePlacer;
		this.minTrunkHeight = config.trunkHeight;
		this.maxTrunkHeight = config.trunkHeightRandom + config.trunkHeight - 1;
		this.minFoliageDepth = config.foliageHeight;
		this.maxFoliageDepth = config.foliageHeightRandom + config.foliageHeight - 1;
		this.baseHeight = config.baseHeight;
		this.heightRandA = config.heightRandA;
		this.heightRandB = config.heightRandB;
		this.maxBlocksUnderwater = config.maxWaterDepth;
		this.minTrunkTopOffset = config.trunkTopOffset;
		this.maxTrunkTopOffset = config.trunkTopOffsetRandom + config.trunkTopOffset - 1;
		this.vines = !config.ignoreVines;
	}

	private TreeFeatureConfigHandler(BlockStateProvider leaves, BlockStateProvider log, FoliagePlacer placer, int lt, int ht, int lf, int hf, int bh, int hA, int hB, int mBU, int tOm, int tOM, boolean v) {
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

	@Override
	public IFeatureConfigSerialiser<TreeFeatureConfig> loadFrom(TreeFeatureConfig config) {
		return new TreeFeatureConfigHandler(config);
	}

	@Override
	public IFeatureConfigSerialiser<TreeFeatureConfig> deserialise(Container settings) {
		Integer maxBlocksUnderwater = settings.getIntegerValue("maxBlocksUnderwater");
		Boolean vines = settings.getBooleanValue("vines");
		Container foliagePlacer = settings.getContainer("foliagePlacer");

		return new TreeFeatureConfigHandler(
				BlockStateProviderHandler.stateProvider(settings.getContainer("leaves")),
				BlockStateProviderHandler.stateProvider(settings.getContainer("log")),
				foliagePlacer == null ? new NoneFoliagePlacer() : FeatureSerialisers.getFoliage(foliagePlacer.getClass()), // TODO foliage placer
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
				(vines == null ? true : !vines.booleanValue())
				);
	}

	@Override
	public void serialise(EditableContainer settings) {
		settings.putMap("leaves", BlockStateProviderHandler.serialiseStateProvider(this.leaves).asMap());
		settings.putMap("log", BlockStateProviderHandler.serialiseStateProvider(this.log).asMap());
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
				.trunkHeightRandom(this.maxTrunkHeight - this.minTrunkHeight + 1)
				.foliageHeight(this.minFoliageDepth)
				.foliageHeightRandom(this.maxFoliageDepth - this.minFoliageDepth)
				.trunkTopOffset(this.minTrunkTopOffset)
				.trunkTopOffsetRandom(this.maxTrunkTopOffset - this.minTrunkTopOffset + 1)
				.maxWaterDepth(this.maxBlocksUnderwater);

		if (!this.vines) {
			config.ignoreVines();
		}
		return config.build();
	}

	public static final TreeFeatureConfigHandler BASE = new TreeFeatureConfigHandler(null, null, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false);
}
