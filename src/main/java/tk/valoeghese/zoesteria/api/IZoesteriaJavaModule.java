package tk.valoeghese.zoesteria.api;

import java.util.List;

import com.google.common.collect.ImmutableList;

import tk.valoeghese.zoesteria.api.biome.IZoesteriaBiome;
import tk.valoeghese.zoesteria.api.surface.ISurfaceBuilder;
import tk.valoeghese.zoesteria.core.genmodifierpack.GenModifierPack;

public interface IZoesteriaJavaModule {
	String packId();
	Manifest createManifest();
	List<IZoesteriaBiome> createBiomes();

	default List<ISurfaceBuilder> createSurfaces() {
		return ImmutableList.of();
	}

	static void registerModule(IZoesteriaJavaModule module) {
		GenModifierPack.init();
		GenModifierPack.addJavaModuleIfAbsent(module);
	}
}
