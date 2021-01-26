package tk.valoeghese.zoesteria.common.biome;

import java.util.List;

import com.google.common.collect.ImmutableList;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager.BiomeType;
import tk.valoeghese.zoesteria.api.biome.BiomeDecorations;
import tk.valoeghese.zoesteria.api.biome.IBiome;
import tk.valoeghese.zoesteria.api.biome.IBiomeProperties;
import tk.valoeghese.zoesteria.api.biome.SpawnEntry;

public class Meadow implements IBiome {
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
		return ImmutableList.of(
				Type.OVERWORLD,
				Type.PLAINS,
				Type.LUSH,
				Type.SPARSE);
	}

	@Override
	public List<SpawnEntry> mobSpawns() {
		// TODO Auto-generated method stub
		return null;
	}
}
