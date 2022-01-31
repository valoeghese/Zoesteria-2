package valoeghese.zoesteria.common.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class TreeFeatureConfig implements FeatureConfiguration {
	public TreeFeatureConfig(BlockStateProvider log, BlockStateProvider leaves,
							 int trunkHeight, int trunkHeightRandom, int trunkTopOffset, int trunkTopOffsetRandom,
							 int baseHeight, int heightRandA, int heightRandB) {
		this.logProvider = log;
		this.leavesProvider = leaves;
		this.trunkHeight = trunkHeight;
		this.trunkHeightRandom = trunkHeightRandom;
		this.trunkTopOffset = trunkTopOffset;
		this.trunkTopOffsetRandom = trunkTopOffsetRandom;
		this.baseHeight = baseHeight;
		this.heightRandA = heightRandA;
		this.heightRandB = heightRandB;
	}

	public final BlockStateProvider logProvider;
	public final BlockStateProvider leavesProvider;
	public final int trunkHeight;
	public final int trunkHeightRandom;
	public final int trunkTopOffset;
	public final int trunkTopOffsetRandom;
	public final int baseHeight;
	public final int heightRandA;
	public final int heightRandB;

	public static final Codec<TreeFeatureConfig> CODEC = RecordCodecBuilder.create(builder ->
			builder.group(
					BlockStateProvider.CODEC.fieldOf("log_provider").forGetter(instance -> instance.logProvider),
					BlockStateProvider.CODEC.fieldOf("leaves_provider").forGetter(instance -> instance.leavesProvider),
					Codec.INT.optionalFieldOf("trunk_height", 0).forGetter(instance -> instance.trunkHeight),
					Codec.INT.optionalFieldOf("trunk_height_random", 0).forGetter(instance -> instance.trunkHeightRandom),
					Codec.INT.optionalFieldOf("trunk_top_offset", 0).forGetter(instance -> instance.trunkHeightRandom),
					Codec.INT.optionalFieldOf("trunk_top_offset_random", 0).forGetter(instance -> instance.trunkHeightRandom),
					Codec.INT.optionalFieldOf("base_height", 0).forGetter(instance -> instance.baseHeight),
					Codec.INT.optionalFieldOf("height_rand_a", 0).forGetter(instance -> instance.heightRandA),
					Codec.INT.optionalFieldOf("height_rand_b", 0).forGetter(instance -> instance.heightRandB))
					.apply(builder, TreeFeatureConfig::new));
}
