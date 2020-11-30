package tk.valoeghese.zoesteria.common.feature;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import tk.valoeghese.zoesteria.api.feature.IZoesteriaFeatureConfig;
import tk.valoeghese.zoesteria.core.Default;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class TreeFeatureConfigHandler implements IZoesteriaFeatureConfig<TreeFeatureConfig> {
	private TreeFeatureConfigHandler(TreeFeatureConfig config) {
		// TODO make not bad, have ways of having variable leaves and log
		this.leaves = config.leavesProvider.getBlockState(RAND, BlockPos.ZERO);
		this.log = config.trunkProvider.getBlockState(RAND, BlockPos.ZERO);
		this.minTrunkHeight = config.trunkHeight;
		this.maxTrunkHeight = config.trunkHeightRandom + config.trunkHeight;
		this.minFoliageDepth = config.foliageHeight;
		this.maxFoliageDepth = config.foliageHeightRandom + config.foliageHeight;
		this.baseHeight = config.baseHeight;
		this.heightRandA = config.heightRandA;
		this.heightRandB = config.heightRandB;
		this.maxBlocksUnderwater = config.maxWaterDepth;
	}

	private TreeFeatureConfigHandler(BlockState leaves, BlockState log, int lt, int ht, int lf, int hf, int bh, int hA, int hB, int mBU) {
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
	}

	private static final Random RAND = new Random();

	private final BlockState leaves;
	private final BlockState log;
	private final int baseHeight;
	private final int heightRandA;
	private final int heightRandB;
	private final int minTrunkHeight;
	private final int maxTrunkHeight;
	private final int minFoliageDepth;
	private final int maxFoliageDepth;
	private final int maxBlocksUnderwater;

	@Override
	public IZoesteriaFeatureConfig<TreeFeatureConfig> loadFrom(TreeFeatureConfig config) {
		return new TreeFeatureConfigHandler(config);
	}

	@Override
	public IZoesteriaFeatureConfig<TreeFeatureConfig> deserialise(Container settings) {
		Integer maxBlocksUnderwater = settings.getIntegerValue("maxBlocksUnderwater");

		return new TreeFeatureConfigHandler(
				ZFGUtils.getBlockState(settings, "leaves"),
				ZFGUtils.getBlockState(settings, "log"),
				settings.getIntegerValue("minTrunkHeight"),
				settings.getIntegerValue("maxTrunkHeight"),
				settings.getIntegerValue("minFoliageDepth"),
				settings.getIntegerValue("maxFoliageDepth"),
				settings.getIntegerValue("baseHeight"),
				settings.getIntegerValue("heightRandA"),
				settings.getIntegerValue("heightRandB"),
				maxBlocksUnderwater == null ? 0 : maxBlocksUnderwater.intValue()
				);
	}

	@Override
	public void serialise(EditableContainer settings) {
		ZFGUtils.putBlockState(settings, "leaves", this.leaves);
		ZFGUtils.putBlockState(settings, "log", this.log);
		settings.putIntegerValue("baseHeight", this.baseHeight);
		settings.putIntegerValue("heightRandA", this.heightRandA);
		settings.putIntegerValue("heightRandB", this.heightRandB);
		settings.putIntegerValue("minTrunkHeight", this.minTrunkHeight);
		settings.putIntegerValue("maxTrunkHeight", this.maxTrunkHeight);
		settings.putIntegerValue("minFoliageDepth", this.minFoliageDepth);
		settings.putIntegerValue("maxFoliageDepth", this.maxFoliageDepth);
	}

	@Override
	public TreeFeatureConfig create() {
		return new TreeFeatureConfig.Builder(
				new SimpleBlockStateProvider(this.log),
				new SimpleBlockStateProvider(this.leaves), new BlobFoliagePlacer(2, 1))
				.baseHeight(this.baseHeight)
				.heightRandA(this.heightRandA)
				.heightRandB(this.heightRandB)
				.trunkHeight(this.minTrunkHeight)
				.trunkHeightRandom(this.maxTrunkHeight - this.minTrunkHeight)
				.foliageHeight(this.minFoliageDepth)
				.foliageHeightRandom(this.maxFoliageDepth - this.minFoliageDepth)
				.maxWaterDepth(this.maxBlocksUnderwater)
				.build();
	}

	public static final TreeFeatureConfigHandler BASE = new TreeFeatureConfigHandler(Default.STATE, Default.STATE, 0, 0, 0, 0, 0, 0, 0, 0);
}
