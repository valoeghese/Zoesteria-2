package tk.valoeghese.zoesteria.api;

import java.util.List;

import com.google.common.collect.ImmutableList;

import tk.valoeghese.zoesteria.api.biome.BiomeTweaks;
import tk.valoeghese.zoesteria.api.biome.IZoesteriaBiome;
import tk.valoeghese.zoesteria.api.surface.ISurfaceBuilderTemplate;
import tk.valoeghese.zoesteria.api.surface.ZoesteriaSurfaceBuilder;
import tk.valoeghese.zoesteria.core.ZoesteriaMod;
import tk.valoeghese.zoesteria.core.pack.GenModifierPack;

public interface IZoesteriaJavaModule {
	String packId();
	Manifest createManifest();
	List<IZoesteriaBiome> createBiomes();

	/**
	 * Use this to add new features to existing biomes.
	 * @apiNote this method be subject to future change in order to accommodate more complex tweaks, like adding to specific biomes or adding by different biome properties.
	 */
	default void addBiomeTweaks(BiomeTweaks tweaks) {
	}

	default List<ZoesteriaSurfaceBuilder<?, ?, ?>> createSurfaceBuilders() {
		return ImmutableList.of();
	}

	default List<ISurfaceBuilderTemplate<?, ?>> createSurfaceBuilderTemplates() {
		return ImmutableList.of();
	}

	/**
	 * Called at the time of placement setting serialiser registration.
	 */
	default void registerPlacementSerialisers() {
	}

	/**
	 * Called at the time of feature setting serialiser registration.
	 */
	default void registerFeatureSerialisers() {
	}

	/**
	 * Called at the time of foliage placement serialiser registration.
	 */
	default void registerFoliageSerialisers() {
	}

	/**
	 * Called at the time of biome predicate registration. This occurs at the beginning of Zoesteria's Common Setup.
	 */
	default void registerBiomePredicates() {
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
