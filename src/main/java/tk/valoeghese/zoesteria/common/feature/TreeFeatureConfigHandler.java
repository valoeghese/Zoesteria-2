package tk.valoeghese.zoesteria.common.feature;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraftforge.registries.ForgeRegistries;
import tk.valoeghese.zoesteria.api.feature.IZoesteriaFeatureConfig;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class TreeFeatureConfigHandler implements IZoesteriaFeatureConfig<TreeFeatureConfig> {
	private TreeFeatureConfigHandler(TreeFeatureConfig config) {
		this.leaves = config.leavesProvider.getBlockState(RAND, BlockPos.ZERO);
		this.log = config.trunkProvider.getBlockState(RAND, BlockPos.ZERO);
		this.minTrunkHeight = config.trunkHeight;
		this.maxTrunkHeight = config.trunkHeightRandom + config.trunkHeight;
		this.minFoliageDepth = config.foliageHeight;
		this.maxFoliageDepth = config.foliageHeightRandom + config.foliageHeight;
	}

	private TreeFeatureConfigHandler(BlockState leaves, BlockState log, int lt, int ht, int lf, int hf) {
		this.leaves = leaves;
		this.log = log;
		this.minTrunkHeight = lt;
		this.maxTrunkHeight = ht;
		this.minFoliageDepth = lf;
		this.maxFoliageDepth = hf;
	}

	private static final Random RAND = new Random();

	private final BlockState leaves;
	private final BlockState log;
	private final int minTrunkHeight;
	private final int maxTrunkHeight;
	private final int minFoliageDepth;
	private final int maxFoliageDepth;

	@Override
	public IZoesteriaFeatureConfig<TreeFeatureConfig> loadFrom(TreeFeatureConfig config) {
		return new TreeFeatureConfigHandler(config);
	}

	@Override
	public IZoesteriaFeatureConfig<TreeFeatureConfig> deserialise(Container settings) {
		return new TreeFeatureConfigHandler(
				ZFGUtils.getBlockState(settings, "leaves")
				);
	}

	@Override
	public void serialise(EditableContainer settings) {
		// TODO Auto-generated method stub

	}

	@Override
	public TreeFeatureConfig create() {
		return new TreeFeatureConfig.Builder(trunkProviderIn, leavesProviderIn, foliagePlacerIn)
	}

	public static final TreeFeatureConfigHandler BASE = new TreeFeatureConfigHandler();
}
