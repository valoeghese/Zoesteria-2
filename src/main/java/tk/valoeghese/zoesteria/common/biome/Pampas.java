package tk.valoeghese.zoesteria.common.biome;

import java.util.ArrayList;
import java.util.List;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager.BiomeType;
import tk.valoeghese.zoesteria.api.biome.BiomeDecorations;
import tk.valoeghese.zoesteria.api.biome.IBiome;
import tk.valoeghese.zoesteria.api.biome.IBiomeProperties;
import tk.valoeghese.zoesteria.api.biome.SpawnEntry;

public class Pampas implements IBiome {
	public Pampas(String id, Type type) {
		this.id = id;
		this.type = type;
	}

	private final String id;
	private final Type type;

	@Override
	public String id() {
		return this.id;
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
	public List<BiomeDictionary.Type> biomeTypes() {
		List<BiomeDictionary.Type> result = new ArrayList<>();
		result.add(BiomeDictionary.Type.OVERWORLD);
		result.add(BiomeDictionary.Type.PLAINS);

		if (this.type == Type.FLATS) {
			result.add(BiomeDictionary.Type.SPARSE);
		} else if (this.type == Type.HILLS) {
			result.add(BiomeDictionary.Type.HILLS);
		}

		return result;
	}

	@Override
	public List<SpawnEntry> mobSpawns() {
		// TODO Auto-generated method stub
		return null;
	}

	public enum Type {
		FLATS,
		NORMAL,
		HILLS
	}
}
