package tk.valoeghese.zoesteria.api;

import java.util.List;

import com.google.common.collect.ImmutableList;

import tk.valoeghese.zoesteria.api.biome.IZoesteriaBiome;
import tk.valoeghese.zoesteria.api.surface.ISurfaceBuilderTemplate;
import tk.valoeghese.zoesteria.api.surface.ZoesteriaSurfaceBuilder;
import tk.valoeghese.zoesteria.core.ZoesteriaMod;
import tk.valoeghese.zoesteria.core.genmodifierpack.GenModifierPack;

public interface IZoesteriaJavaModule {
	String packId();
	Manifest createManifest();
	List<IZoesteriaBiome> createBiomes();

	default List<ZoesteriaSurfaceBuilder<?, ?>> createSurfaceBuilders() {
		return ImmutableList.of();
	}

	default List<ISurfaceBuilderTemplate<?>> createSurfaceBuilderTemplates() {
		return ImmutableList.of();
	}

	/**
	 * Called at the time of placement settings registration.
	 */
	default void registerPlacementSettings() {
	}

	/**
	 * Called at the time of feature settings registration.
	 */
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
