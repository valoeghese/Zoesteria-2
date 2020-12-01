package tk.valoeghese.zoesteria.core;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.Feature;

/**
 * Backup registries for looking up feature registry names before biome load.
 * @author Valoeghese
 */
public final class InternalBackupRegistries {
	private InternalBackupRegistries() {
	}

	public static final Map<ResourceLocation, Feature<?>> FEATURE_LOOKUP = new HashMap<>();
}
