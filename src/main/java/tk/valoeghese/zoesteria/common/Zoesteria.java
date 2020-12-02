package tk.valoeghese.zoesteria.common;

import java.util.List;

import com.google.common.collect.Lists;

import tk.valoeghese.zoesteria.api.IZoesteriaJavaModule;
import tk.valoeghese.zoesteria.api.Manifest;
import tk.valoeghese.zoesteria.api.biome.IZoesteriaBiome;
import tk.valoeghese.zoesteria.api.feature.FeatureSerialisers;
import tk.valoeghese.zoesteria.common.biome.BluffBiome;
import tk.valoeghese.zoesteria.common.feature.TreeFeatureConfigHandler;
import tk.valoeghese.zoesteria.core.ZoesteriaRegistryHandler;

public class Zoesteria implements IZoesteriaJavaModule {
	@Override
	public String packId() {
		return "zoesteria";
	}

	@Override
	public Manifest createManifest() {
		return Manifest.createSchema0(this);
	}

	@Override
	public List<IZoesteriaBiome> createBiomes() {
		List<IZoesteriaBiome> biomes = Lists.newArrayList();
		biomes.add(new BluffBiome());
		return biomes;
	}

	@Override
	public void registerFeatureSettings() {
		FeatureSerialisers.registerFeatureSettings(ZoesteriaRegistryHandler.BLUFF_PINE, TreeFeatureConfigHandler.BASE);
	}
}
