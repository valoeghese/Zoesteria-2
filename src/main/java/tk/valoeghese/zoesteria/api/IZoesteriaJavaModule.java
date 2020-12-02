package tk.valoeghese.zoesteria.api;

import java.util.List;

import com.google.common.collect.ImmutableList;

import tk.valoeghese.zoesteria.api.biome.IZoesteriaBiome;
import tk.valoeghese.zoesteria.api.surface.ISurfaceBuilder;
import tk.valoeghese.zoesteria.core.ZoesteriaMod;
import tk.valoeghese.zoesteria.core.genmodifierpack.GenModifierPack;

public interface IZoesteriaJavaModule {
	String packId();
	Manifest createManifest();
	List<IZoesteriaBiome> createBiomes();

	default List<ISurfaceBuilder<?, ?>> createSurfaces() {
		return ImmutableList.of();
	}

	default void registerPlacementSettings() {
	}

	default void registerFeatureSettings() {
	}

	/**
	 * Call this method in the constructor of your main mod class to register your Zoesteria java module to Zoesteria-2.
	 * @param module the module to register.
	 */
	static void registerModule(IZoesteriaJavaModule module) {
		synchronized (GenModifierPack.ROOT_DIR) {
			ZoesteriaMod.LOGGER.info("Adding module: " + module.packId());
			GenModifierPack.init();
			GenModifierPack.addJavaModuleIfAbsent(module);
		}
	}
}
