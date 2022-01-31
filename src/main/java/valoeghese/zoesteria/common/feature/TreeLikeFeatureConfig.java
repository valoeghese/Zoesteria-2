package valoeghese.zoesteria.common.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class TreeLikeFeatureConfig implements FeatureConfiguration {
	public TreeLikeFeatureConfig(BlockStateProvider log, int baseSize, int sizeRandom) {
		this.logProvider = log;
		this.baseSize = baseSize;
		this.sizeRandom = sizeRandom;
	}

	public final BlockStateProvider logProvider;
	public final int baseSize;
	public final int sizeRandom;

	public static final Codec<TreeLikeFeatureConfig> CODEC = RecordCodecBuilder.create(builder ->
			builder.group(
					BlockStateProvider.CODEC.fieldOf("log_provider").forGetter(instance -> instance.logProvider),
					Codec.INT.optionalFieldOf("base_size", 0).forGetter(instance -> instance.baseSize),
					Codec.INT.optionalFieldOf("size_random", 0).forGetter(instance -> instance.sizeRandom))
					.apply(builder, TreeLikeFeatureConfig::new));
}
