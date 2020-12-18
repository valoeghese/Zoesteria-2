package tk.valoeghese.zoesteria.api.biome;

import net.minecraft.entity.EntityType;

public final class SpawnEntry {
	public SpawnEntry(EntityType<?> type) {
		this.type = type;
	}

	private final EntityType<?> type;
	private int minCount;
	private int maxCount;
	private int weight;

	public SpawnEntry spawnWeight(int weight) {
		this.weight = weight;
		return this;
	}

	public SpawnEntry spawnGroupCount(int minCount, int maxCount) {
		this.minCount = minCount;
		this.maxCount = maxCount;
		return this;
	}

	public EntityType<?> getEntityType() {
		return this.type;
	}

	public int getMinGroupCount() {
		return this.minCount;
	}

	public int getMaxGroupCount() {
		return this.maxCount;
	}

	public int getSpawnWeight() {
		return this.weight;
	}
}
