package tk.valoeghese.zoesteria.api;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import tk.valoeghese.zoesteria.api.biome.IBiomePredicate;
import tk.valoeghese.zoesteria.api.feature.IFeatureConfigSerialiser;
import tk.valoeghese.zoesteria.api.feature.IFoliagePlacerSerialiser;
import tk.valoeghese.zoesteria.api.feature.IPlacementConfigSerialiser;
import tk.valoeghese.zoesteria.api.feature.ITreeDecoratorSerialiser;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

/**
 * Register your {@linkplain IFeatureConfigSerialiser}, {@linkplain IPlacementConfigSerialiser},
 * {@linkplain IFoliagePlacerSerialiser}, {@link ITreeDecoratorSerialiser} and {@linkplain IBiomePredicate} instances in feature registry event and placement registry event, respectively.
 */
public class ZoesteriaSerialisers {
	private ZoesteriaSerialisers() {
	}

	/**
	 * Register a feature config serialiser for the serialisation of the data of a feature.
	 * @param feature the feature this is to be registered for.
	 * @param configHandler the serialiser for the feature config.
	 */
	public static <T extends IFeatureConfig> void registerFeatureSettings(Feature<T> feature, IFeatureConfigSerialiser<T> configHandler) {
		FEATURE_CONFIGS.put(feature, configHandler);
	}

	public static <T extends IPlacementConfig> void registerPlacementSerialiser(Placement<T> placement, IPlacementConfigSerialiser<T> configHandler) {
		PLACEMENT_CONFIGS.put(placement, configHandler);
	}

	public static <T extends TreeDecorator> void registerTreeDecorator(ResourceLocation foliageTypeId, Class<T> clazz, ITreeDecoratorSerialiser<T> serialiser) {
		TREE_DECORATORS.put(clazz, serialiser);
		TREE_DECORATORS_BY_ID.put(foliageTypeId, serialiser);
		IDS_BY_TREE_DECORATORS.put(clazz, foliageTypeId);
	}

	public static <T extends FoliagePlacer> void registerFoliagePlacer(ResourceLocation foliageTypeId, Class<T> clazz, IFoliagePlacerSerialiser<T> foliageHandler) {
		FOLIAGE_PLACERS.put(clazz, foliageHandler);
		FOLIAGE_PLACERS_BY_ID.put(foliageTypeId, foliageHandler);
		IDS_BY_FOLIAGE_PLACER.put(clazz, foliageTypeId);
	}

	/**
	 * Register a biome predicate for deserialisation of tweaks.
	 * @param base a dummy instance for use in deserialisation.
	 */
	public static void registerBiomePredicate(IBiomePredicate base) {
		BIOME_PREDICATES.put(base.id(), base);
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
	public static <T extends TreeDecorator> ITreeDecoratorSerialiser<T> getTreeDecorator(Class<T> treeDecoratorClazz) {
		return (ITreeDecoratorSerialiser<T>) TREE_DECORATORS.get(treeDecoratorClazz);
	}

	@SuppressWarnings("unchecked")
	public static <T extends FoliagePlacer> IFoliagePlacerSerialiser<T> getFoliage(Class<T> foliagePlacerClazz) {
		return (IFoliagePlacerSerialiser<T>) FOLIAGE_PLACERS.get(foliagePlacerClazz);
	}

	// tree decorator

	public static TreeDecorator deserialiseTreeDecorator(Container treeDecorator) {
		ITreeDecoratorSerialiser<?> serialiser = TREE_DECORATORS_BY_ID.get(new ResourceLocation(treeDecorator.getStringValue("type")));
		return serialiser.deserialise(treeDecorator).create();
	}

	/**
	 * @return the same {@linkplain EditableContainer} instance as given in the second parameter.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends TreeDecorator> EditableContainer serialiseTreeDecorator(T decorator, EditableContainer treeDecorator) {
		Class<? extends TreeDecorator> clazz = decorator.getClass();
		treeDecorator.putStringValue("type", IDS_BY_TREE_DECORATORS.get(clazz).toString());

		ITreeDecoratorSerialiser<T> serialiser = (ITreeDecoratorSerialiser<T>) ZoesteriaSerialisers.getTreeDecorator(decorator.getClass());
		serialiser.loadFrom(decorator).serialise(treeDecorator);
		return treeDecorator;
	}

	// foliage placer

	public static FoliagePlacer deserialiseFoliage(Container foliagePlacer) {
		IFoliagePlacerSerialiser<?> serialiser = FOLIAGE_PLACERS_BY_ID.get(new ResourceLocation(foliagePlacer.getStringValue("type")));
		return serialiser.deserialise(foliagePlacer).create();
	}

	@SuppressWarnings("unchecked")
	public static <T extends FoliagePlacer> void serialiseFoliage(T placer, EditableContainer foliagePlacer) {
		Class<? extends FoliagePlacer> clazz = placer.getClass();
		foliagePlacer.putStringValue("type", IDS_BY_FOLIAGE_PLACER.get(clazz).toString());

		IFoliagePlacerSerialiser<T> serialiser = (IFoliagePlacerSerialiser<T>) ZoesteriaSerialisers.getFoliage(placer.getClass());
		serialiser.loadFrom(placer).serialise(foliagePlacer);
	}

	public static IBiomePredicate getPredicate(ResourceLocation id) {
		return BIOME_PREDICATES.get(id);
	}

	private static final Map<Feature<?>, IFeatureConfigSerialiser<?>> FEATURE_CONFIGS = new HashMap<>();
	private static final Map<Placement<?>, IPlacementConfigSerialiser<?>> PLACEMENT_CONFIGS = new HashMap<>();

	private static final Map<Class<?>, ITreeDecoratorSerialiser<?>> TREE_DECORATORS = new HashMap<>();
	private static final Map<ResourceLocation, ITreeDecoratorSerialiser<? extends TreeDecorator>> TREE_DECORATORS_BY_ID = new HashMap<>();
	private static final Map<Class<?>, ResourceLocation> IDS_BY_TREE_DECORATORS = new HashMap<>();

	private static final Map<Class<?>, IFoliagePlacerSerialiser<? extends FoliagePlacer>> FOLIAGE_PLACERS = new HashMap<>();
	private static final Map<ResourceLocation, IFoliagePlacerSerialiser<? extends FoliagePlacer>> FOLIAGE_PLACERS_BY_ID = new HashMap<>();
	private static final Map<Class<?>, ResourceLocation> IDS_BY_FOLIAGE_PLACER = new HashMap<>();

	private static final Map<ResourceLocation, IBiomePredicate> BIOME_PREDICATES = new HashMap<>();
}
