package tk.valoeghese.zoesteria.common.biome;

import java.util.List;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager.BiomeType;
import tk.valoeghese.zoesteria.api.biome.BiomeDecorations;
import tk.valoeghese.zoesteria.api.biome.IBiome;
import tk.valoeghese.zoesteria.api.biome.IBiomeProperties;
import tk.valoeghese.zoesteria.api.biome.SpawnEntry;

public class Pampas implements IBiome {
	@Override
	public String id() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBiomeProperties properties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canSpawnInBiome() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addPlacement(Object2IntMap<BiomeType> biomePlacement) {
		biomePlacement.put(BiomeType.COOL, 10);
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