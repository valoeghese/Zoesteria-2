package tk.valoeghese.zoesteria.core.serialisers;

import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import tk.valoeghese.zoesteria.api.feature.IZoesteriaFeatureConfig;
import tk.valoeghese.zoesteria.core.ZFGUtils;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class TreeFeatureConfigHandler implements IZoesteriaFeatureConfig<TreeFeatureConfig> {
	private TreeFeatureConfigHandler(TreeFeatureConfig config) {
		this.leaves = config.leavesProvider;
		this.log = config.trunkProvider;
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
	}

	private TreeFeatureConfigHandler(BlockStateProvider leaves, BlockStateProvider log, int lt, int ht, int lf, int hf, int bh, int hA, int hB, int mBU, int tOm, int tOM) {
		this.leaves = leaves;
		this.log = log;
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
	}

	private final BlockStateProvider leaves;
	private final BlockStateProvider log;
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

	@Override
	public IZoesteriaFeatureConfig<TreeFeatureConfig> loadFrom(TreeFeatureConfig config) {
		return new TreeFeatureConfigHandler(config);
	}

	@Override
	public IZoesteriaFeatureConfig<TreeFeatureConfig> deserialise(Container settings) {
		Integer maxBlocksUnderwater = settings.getIntegerValue("maxBlocksUnderwater");

		return new TreeFeatureConfigHandler(
				BlockStateProviderHandler.stateProvider(settings.getContainer("leaves")),
				BlockStateProviderHandler.stateProvider(settings.getContainer("log")),
				ZFGUtils.getIntOrDefault(settings, "minTrunkHeight", -1),
				ZFGUtils.getIntOrDefault(settings, "maxTrunkHeight", -1),
				ZFGUtils.getIntOrDefault(settings, "minFoliageDepth", -1),
				ZFGUtils.getIntOrDefault(settings, "maxFoliageDepth", -1),
				settings.getIntegerValue("baseHeight"),
				settings.getIntegerValue("heightRandA"),
				settings.getIntegerValue("heightRandB"),
				(maxBlocksUnderwater == null ? 0 : maxBlocksUnderwater.intValue()),
				settings.getIntegerValue("minTrunkTopOffset"),
				settings.getIntegerValue("maxTrunkTopOffset")
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
	}

	@Override
	public TreeFeatureConfig create() {
		return new TreeFeatureConfig.Builder(
				this.log,
				this.leaves, new BlobFoliagePlacer(2, 1))
				.baseHeight(this.baseHeight)
				.heightRandA(this.heightRandA)
				.heightRandB(this.heightRandB)
				.trunkHeight(this.minTrunkHeight)
				.trunkHeightRandom(this.maxTrunkHeight - this.minTrunkHeight + 1)
				.foliageHeight(this.minFoliageDepth)
				.foliageHeightRandom(this.maxFoliageDepth - this.minFoliageDepth)
				.trunkTopOffset(this.minTrunkTopOffset)
				.trunkTopOffsetRandom(this.maxTrunkTopOffset - this.minTrunkTopOffset + 1)
				.maxWaterDepth(this.maxBlocksUnderwater)
				.build();
	}

	public static final TreeFeatureConfigHandler BASE = new TreeFeatureConfigHandler(null, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
}
