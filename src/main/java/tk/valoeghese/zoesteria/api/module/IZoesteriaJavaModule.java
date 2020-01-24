package tk.valoeghese.zoesteria.api.module;

import java.util.List;

import tk.valoeghese.zoesteria.api.biome.IZoesteriaBiome;
import tk.valoeghese.zoesteria.core.genmodifierpack.GenModifierPack;

public interface IZoesteriaJavaModule {
	String packId();
	Manifest createManifest();
	List<IZoesteriaBiome> createBiomes();

	static void registerModule(IZoesteriaJavaModule module) {
		GenModifierPack.init();
		GenModifierPack.addIfAbsent(module);
	}
}
