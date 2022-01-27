package valoeghese.zoesteria.common.placement;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

import net.minecraft.world.gen.placement.IPlacementConfig;

public class LinePlacementConfig implements IPlacementConfig {
	public LinePlacementConfig(int count) {
		this(count, 0.008, 0.05);
	}

	public LinePlacementConfig(int count, double frequency, double threshold) {
		this(count, frequency, threshold, 0.0);
	}

	public LinePlacementConfig(int count, double frequency, double threshold, double offset) {
		this.count = count;
		this.frequency = frequency;
		this.threshold = threshold;
		this.offset = offset;
	}

	public final int count;
	public final double frequency;
	public final double threshold;
	public final double offset;

	public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
		return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(
				ops.createString("count"), ops.createInt(this.count),
				ops.createString("frequency"), ops.createDouble(this.frequency),
				ops.createString("threshold"), ops.createDouble(this.threshold),
				ops.createString("offset"), ops.createDouble(this.offset)
				)));
	}

	public static LinePlacementConfig deserialize(Dynamic<?> p_214721_0_) {
		int count = p_214721_0_.get("count").asInt(0);
		double frequency = p_214721_0_.get("frequency").asDouble(0.008);
		double threshold = p_214721_0_.get("threshold").asDouble(0.05);
		double offset = p_214721_0_.get("offset").asDouble(0.0);

		return new LinePlacementConfig(count, frequency, threshold, offset);
	}
}
