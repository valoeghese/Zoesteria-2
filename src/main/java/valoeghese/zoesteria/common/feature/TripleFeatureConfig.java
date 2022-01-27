package valoeghese.zoesteria.common.feature;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class TripleFeatureConfig implements IFeatureConfig {
	public TripleFeatureConfig(ConfiguredFeature<?, ?> feature0, ConfiguredFeature<?, ?> feature1, ConfiguredFeature<?, ?> feature2) {
		this(feature0, feature1, feature2, 0.0D);
	}

	public TripleFeatureConfig(ConfiguredFeature<?, ?> feature0, ConfiguredFeature<?, ?> feature1, ConfiguredFeature<?, ?> feature2, double offset) {
		this.feature0 = feature0;
		this.feature1 = feature1;
		this.feature2 = feature2;
		this.offset = offset;
	}

	public final ConfiguredFeature<?, ?> feature0;
	public final ConfiguredFeature<?, ?> feature1;
	public final ConfiguredFeature<?, ?> feature2;
	public final double offset;

	@Override
	public <T> Dynamic<T> serialize(DynamicOps<T> dynamic) {
		return new Dynamic<>(dynamic,
				dynamic.createMap(ImmutableMap.of(
						dynamic.createString("feature0"),
						this.feature0.serialize(dynamic).getValue(),
						dynamic.createString("feature1"),
						this.feature1.serialize(dynamic).getValue(),
						dynamic.createString("feature2"),
						this.feature2.serialize(dynamic).getValue(),
						dynamic.createString("offset"),
						dynamic.createDouble(this.offset))));
	}

	public static <T> TripleFeatureConfig deserialize(Dynamic<T> dynamic) {
		ConfiguredFeature<?, ?> feature0 = ConfiguredFeature.deserialize(dynamic.get("feature0").orElseEmptyMap());
		ConfiguredFeature<?, ?> feature1 = ConfiguredFeature.deserialize(dynamic.get("feature1").orElseEmptyMap());
		ConfiguredFeature<?, ?> feature2 = ConfiguredFeature.deserialize(dynamic.get("feature2").orElseEmptyMap());
		double offset = dynamic.get("offset").asDouble(0.0D);
		return new TripleFeatureConfig(feature0, feature1, feature2, offset);
	}
}
