package tk.valoeghese.zoesteria.api.feature;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

/**
 * Register your {@linkplain IFeatureConfigSerialiser}, {@linkplain IPlacementConfigSerialiser}, and {@linkplain IFoliagePlacerSerialiser} instances in feature registry event and placement registry event, respectively.
 */
public class FeatureSerialisers {
	private FeatureSerialisers() {
	}

	public static <T extends IFeatureConfig> void registerFeatureSettings(Feature<T> feature, IFeatureConfigSerialiser<T> configHandler) {
		FEATURE_CONFIGS.put(feature, configHandler);
	}

	public static <T extends IPlacementConfig> void registerPlacementSettings(Placement<T> placement, IPlacementConfigSerialiser<T> configHandler) {
		PLACEMENT_CONFIGS.put(placement, configHandler);
	}

	public static <T extends FoliagePlacer> void registerFoliagePlacer(Class<T> placement, IFoliagePlacerSerialiser<T> configHandler) {
		FOLIAGE_PLACERS.put(placement, configHandler);
	}

	// only dirty reflection hacks or an idiot (or genius) using raw types should be able to cause casting errors

	@SuppressWarnings("unchecked")
	public static <T extends IFeatureConfig> IFeatureConfigSerialiser<T> getFeatureSettings(Feature<T> featureConfig) {
		return (IFeatureConfigSerialiser<T>) FEATURE_CONFIGS.get(featureConfig);
	}

	@SuppressWarnings("unchecked")
	public static <T extends IPlacementConfig> IPlacementConfigSerialiser<T> getPlacement(Placement<T> placementConfig) {
		return (IPlacementConfigSerialiser<T>) PLACEMENT_CONFIGS.get(placementConfig);
	}

	@SuppressWarnings("unchecked")
	public static <T extends FoliagePlacer> IFoliagePlacerSerialiser<T> getFoliage(Class<T> foliagePlacerType) {
		return (IFoliagePlacerSerialiser<T>) FOLIAGE_PLACERS.get(foliagePlacerType);
	}

	private static final Map<Feature<?>, IFeatureConfigSerialiser<?>> FEATURE_CONFIGS = new HashMap<>();
	private static final Map<Placement<?>, IPlacementConfigSerialiser<?>> PLACEMENT_CONFIGS = new HashMap<>();
	private static final Map<Class<?>, IFoliagePlacerSerialiser<?>> FOLIAGE_PLACERS = new HashMap<>();
}
