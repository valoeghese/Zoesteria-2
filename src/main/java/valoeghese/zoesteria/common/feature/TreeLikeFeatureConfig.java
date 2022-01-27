package valoeghese.zoesteria.common.feature;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.BlockStateProviderType;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class TreeLikeFeatureConfig implements IFeatureConfig {
	public TreeLikeFeatureConfig(BlockStateProvider log, int baseSize, int sizeRandom) {
		this.logProvider = log;
		this.baseSize = baseSize;
		this.sizeRandom = sizeRandom;
	}

	public final BlockStateProvider logProvider;
	public final int baseSize;
	public final int sizeRandom;

	public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
		ImmutableMap.Builder<T, T> builder = ImmutableMap.builder();

		builder.put(ops.createString("log_provider"), this.logProvider.serialize(ops))
		.put(ops.createString("base_size"), ops.createInt(this.baseSize))
		.put(ops.createString("size_random"), ops.createInt(this.sizeRandom));

		return new Dynamic<>(ops, ops.createMap(builder.build()));
	}

	@SuppressWarnings("deprecation")
	public static <T> TreeLikeFeatureConfig deserialize(Dynamic<T> data) {
		// Get key for the block state provider type and look it up
		BlockStateProviderType<?> type = Registry.BLOCK_STATE_PROVIDER_TYPE.getOrDefault(new ResourceLocation(data.get("log_provider").get("type").asString().orElseThrow(RuntimeException::new)));

		return new TreeLikeFeatureConfig(
				// get block state provider from the type, using the remaining data under log_provider
				type.func_227399_a_(data.get("log_provider").orElseEmptyMap()),
				// load ints from the dynamic. self explanatory.
				data.get("base_size").asInt(0),
				data.get("size_random").asInt(0));
	}
}
