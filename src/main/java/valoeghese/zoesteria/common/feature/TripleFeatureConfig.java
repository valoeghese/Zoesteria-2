package valoeghese.zoesteria.common.feature;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.function.Supplier;

public class TripleFeatureConfig implements FeatureConfiguration {
	public TripleFeatureConfig(PlacedFeature feature0, PlacedFeature feature1, PlacedFeature feature2) {
		this(feature0, feature1, feature2, 0.0D);
	}

	public TripleFeatureConfig(PlacedFeature feature0, PlacedFeature feature1, PlacedFeature feature2, double offset) {
		this.feature0 = feature0;
		this.feature1 = feature1;
		this.feature2 = feature2;
		this.offset = offset;
	}

	private TripleFeatureConfig(Supplier<PlacedFeature> feature0, Supplier<PlacedFeature> feature1, Supplier<PlacedFeature> feature2, double offset) {
		this.feature0 = feature0.get();
		this.feature1 = feature1.get();
		this.feature2 = feature2.get();
		this.offset = offset;
	}

	public final PlacedFeature feature0;
	public final PlacedFeature feature1;
	public final PlacedFeature feature2;
	public final double offset;

	public static final Codec<TripleFeatureConfig> CODEC = RecordCodecBuilder.create(builder ->
			builder.group(
					PlacedFeature.CODEC.fieldOf("feature0").forGetter(instance -> () -> instance.feature0),
					PlacedFeature.CODEC.fieldOf("feature1").forGetter(instance -> () -> instance.feature1),
					PlacedFeature.CODEC.fieldOf("feature2").forGetter(instance -> () -> instance.feature2),
					Codec.DOUBLE.fieldOf("offset").forGetter(instance -> instance.offset))
					.apply(builder, TripleFeatureConfig::new));
}
