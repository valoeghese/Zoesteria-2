package tk.valoeghese.zoesteria.common.biome;

import java.util.List;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.biome.Biome.Category;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager.BiomeType;
import tk.valoeghese.zoesteria.api.biome.BiomeDecorations;
import tk.valoeghese.zoesteria.api.biome.IBiomeProperties;
import tk.valoeghese.zoesteria.api.biome.IBiome;
import tk.valoeghese.zoesteria.api.biome.SpawnEntry;

public class Prairie implements IBiome {
	public Prairie(String id, float baseHeight) {
		this.id = id;
		this.baseHeight = baseHeight;
	}

	private final String id;
	private final float baseHeight;

	@Override
	public String id() {
		return this.id;
	}

	@Override
	public IBiomeProperties properties() {
		return IBiomeProperties.builder(Category.PLAINS)
				.depth(this.baseHeight)
				.scale(-0.01f)
				.temperature(0.4f)
				.downfall(0.6f)
				.build();
	}

	@Override
	public void addPlacement(Object2IntMap<BiomeType> biomePlacement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canSpawnInBiome() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BiomeDecorations getDecorations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Type> biomeTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SpawnEntry> mobSpawns() {
		// TODO Auto-generated method stub
		return null;
	}
}
