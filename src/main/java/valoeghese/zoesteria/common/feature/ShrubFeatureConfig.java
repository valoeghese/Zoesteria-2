package valoeghese.zoesteria.common.feature;

import com.google.common.collect.ImmutableMap;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class ShrubFeatureConfig implements FeatureConfiguration {
	public ShrubFeatureConfig(BlockStateProvider log, BlockStateProvider leaves) {
		this.logProvider = log;
		this.leavesProvider = leaves;
	}

	public final BlockStateProvider logProvider;
	public final BlockStateProvider leavesProvider;

	public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
		ImmutableMap.Builder<T, T> builder = ImmutableMap.builder();

		builder.put(ops.createString("log_provider"), this.logProvider.serialize(ops))
		.put(ops.createString("leaves_provider"), this.leavesProvider.serialize(ops));

		return new Dynamic<>(ops, ops.createMap(builder.build()));
	}

	@SuppressWarnings("deprecation")
	public static <T> ShrubFeatureConfig deserialize(Dynamic<T> data) {
		// Get key for the block state provider type and look it up
		BlockStateProviderType<?> typeLog = Registry.BLOCK_STATE_PROVIDER_TYPE.getOrDefault(new ResourceLocation(data.get("log_provider").get("type").asString().orElseThrow(RuntimeException::new)));
		BlockStateProviderType<?> typeLeaf = Registry.BLOCK_STATE_PROVIDER_TYPE.getOrDefault(new ResourceLocation(data.get("leaves_provider").get("type").asString().orElseThrow(RuntimeException::new)));

		return new ShrubFeatureConfig(
				// get block state provider from the type, using the remaining data under the providers
				typeLog.func_227399_a_(data.get("log_provider").orElseEmptyMap()),
				typeLeaf.func_227399_a_(data.get("leaves_provider").orElseEmptyMap()));
	}
}
