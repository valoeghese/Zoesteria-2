package tk.valoeghese.zoesteria.api.feature;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

/**
 * Register your {@link IZoesteriaFeatureConfig} and {@link IZoesteriaPlacementConfig} instances in feature registry event and placement registry event, respectively.
 */
public class FeatureSerialisers {
	private FeatureSerialisers() {
	}

	public static <T extends IFeatureConfig> void registerFeatureConfig(Feature<T> feature, IZoesteriaFeatureConfig<T> configHandler) {
		FEATURE_CONFIGS.put(feature, configHandler);
	}

	public static <T extends IPlacementConfig> void registerPlacementConfig(Placement<T> placement, IZoesteriaPlacementConfig<T> configHandler) {
		PLACEMENT_CONFIGS.put(placement, configHandler);
	}

	// only dirty reflection hacks or an idiot (or genius) using raw types should be able to cause casting errors

	@SuppressWarnings("unchecked")
	public static <T extends IFeatureConfig> IZoesteriaFeatureConfig<T> get(Feature<T> feature) {
		return (IZoesteriaFeatureConfig<T>) FEATURE_CONFIGS.get(feature);
	}

	@SuppressWarnings("unchecked")
	public static <T extends IPlacementConfig> IZoesteriaPlacementConfig<T> get(Placement<T> placement) {
		return (IZoesteriaPlacementConfig<T>) PLACEMENT_CONFIGS.get(placement);
	}

	private static final Map<Feature<?>, IZoesteriaFeatureConfig<?>> FEATURE_CONFIGS = new HashMap<>();
	private static final Map<Placement<?>, IZoesteriaPlacementConfig<?>> PLACEMENT_CONFIGS = new HashMap<>();
}
