package tk.valoeghese.zoesteria.common.biome;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome.Category;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager.BiomeType;
import tk.valoeghese.zoesteria.api.biome.BiomeDecorations;
import tk.valoeghese.zoesteria.api.biome.BiomeDefaultFeatures;
import tk.valoeghese.zoesteria.api.biome.IBiomeProperties;
import tk.valoeghese.zoesteria.api.biome.IZoesteriaBiome;
import tk.valoeghese.zoesteria.api.biome.SpawnEntry;

public class AustralianOutback implements IZoesteriaBiome {
	public AustralianOutback(boolean plateau) {
		this.plateau = plateau;
	}

	private final boolean plateau;

	@Override
	public String id() {
		return this.plateau ? "uluru": "australian_outback";
	}

	@Override
	public IBiomeProperties properties() {
		IBiomeProperties.Builder builder = IBiomeProperties.builder(Category.DESERT)
				.depth(0.3f)
				.scale(0.11f)
				.temperature(1.6f)
				.downfall(0.1f);
		
		if (this.plateau) {
		} else {
			builder.surfaceBuilder("zoesteria:outback").topBlock("minecraft:red_sand").fillerBlock("minecraft:red_sandstone").underwaterBlock("minecraft:red_sand");
		}

		return builder.build();
	}

	@Override
	public void addPlacement(Object2IntMap<BiomeType> biomePlacement) {
		biomePlacement.put(BiomeType.DESERT, 10);
	}

	@Override
	public boolean canSpawnInBiome() {
		return true;
	}

	@Override
	public BiomeDecorations getDecorations() {
		BiomeDecorations result = BiomeDecorations.create();
		BiomeDefaultFeatures.addOres(result);
		BiomeDefaultFeatures.addSedimentDisks(result);
		BiomeDefaultFeatures.addStoneVariants(result);
		BiomeDefaultFeatures.addMushrooms(result, 1, 1);
		BiomeDefaultFeatures.addGrass(result, 2);
		return result;
	}

	@Override
	public List<Type> biomeTypes() {
		List<Type> result = Lists.newArrayList(
				Type.OVERWORLD,
				Type.DRY,
				Type.HOT,
				Type.SPARSE);

		if (this.plateau) {
			result.add(Type.PLATEAU);
		} else {
			result.add(Type.SANDY);
		}

		return result;
	}

	@Override
	public List<SpawnEntry> mobSpawns() {
		return ImmutableList.of(
				new SpawnEntry(EntityType.HORSE).spawnWeight(1).spawnGroupCount(1, 3),
				new SpawnEntry(EntityType.RABBIT).spawnWeight(20).spawnGroupCount(2, 4),

				new SpawnEntry(EntityType.BAT).spawnWeight(10).spawnGroupCount(8, 8),

				new SpawnEntry(EntityType.SPIDER).spawnWeight(100).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.HUSK).spawnWeight(50).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.ZOMBIE).spawnWeight(45).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.ZOMBIE_VILLAGER).spawnWeight(5).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.SKELETON).spawnWeight(100).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.CREEPER).spawnWeight(100).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.SLIME).spawnWeight(100).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.ENDERMAN).spawnWeight(10).spawnGroupCount(2, 4),
				new SpawnEntry(EntityType.WITCH).spawnWeight(5).spawnGroupCount(2, 4));
	}
}
