package tk.valoeghese.zoesteria.forge;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.foliageplacer.AcaciaFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.PineFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.SpruceFoliagePlacer;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.treedecorator.BeehiveTreeDecorator;
import net.minecraft.world.gen.treedecorator.CocoaTreeDecorator;
import net.minecraft.world.gen.treedecorator.LeaveVineTreeDecorator;
import net.minecraft.world.gen.treedecorator.TrunkVineTreeDecorator;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import tk.valoeghese.zoesteria.api.IZoesteriaJavaModule;
import tk.valoeghese.zoesteria.api.surface.ISurfaceBuilderTemplate;
import tk.valoeghese.zoesteria.forge.pack.GenModifierPack;
import tk.valoeghese.zoesteria.forge.serialisers.feature.BlockBlobConfigSerialiser;
import tk.valoeghese.zoesteria.forge.serialisers.feature.BlockClusterConfigSerialiser;
import tk.valoeghese.zoesteria.forge.serialisers.feature.BlockStateFeatureConfigSerialiser;
import tk.valoeghese.zoesteria.forge.serialisers.feature.NoFeatureConfigSerialiser;
import tk.valoeghese.zoesteria.forge.serialisers.feature.OreFeatureConfigSerialiser;
import tk.valoeghese.zoesteria.forge.serialisers.feature.RandomSelectorConfigSerialiser;
import tk.valoeghese.zoesteria.forge.serialisers.feature.SphereReplaceConfigSerialiser;
import tk.valoeghese.zoesteria.forge.serialisers.feature.TreeFeatureConfigSerialiser;
import tk.valoeghese.zoesteria.forge.serialisers.feature.VillageConfigSerialiser;
import tk.valoeghese.zoesteria.forge.serialisers.foliage.AcaciaFoliagePlacerSerialiser;
import tk.valoeghese.zoesteria.forge.serialisers.foliage.BlobFoliagePlacerSerialiser;
import tk.valoeghese.zoesteria.forge.serialisers.foliage.PineFoliagePlacerSerialiser;
import tk.valoeghese.zoesteria.forge.serialisers.foliage.SpruceFoliagePlacerSerialiser;
import tk.valoeghese.zoesteria.forge.serialisers.placement.ChanceConfigSerialiser;
import tk.valoeghese.zoesteria.forge.serialisers.placement.CountExtraChanceConfigSerialiser;
import tk.valoeghese.zoesteria.forge.serialisers.placement.CountRangeConfigSerialiser;
import tk.valoeghese.zoesteria.forge.serialisers.placement.DepthAverageConfigHandler;
import tk.valoeghese.zoesteria.forge.serialisers.placement.FrequencyConfigSerialiser;
import tk.valoeghese.zoesteria.forge.serialisers.placement.HeightChanceConfigSerialiser;
import tk.valoeghese.zoesteria.forge.serialisers.placement.TopSolidWithNoiseConfigSerialiser;
import tk.valoeghese.zoesteria.forge.serialisers.treedecorator.ProbabilityTreeDecoratorSerialiser;
import tk.valoeghese.zoesteria.forge.serialisers.treedecorator.SimpleTreeDecoratorSerialiser;

/**
 * Event registry handler for core stuff.
 */
public class ZoesteriaRegistryHandler {
	@SubscribeEvent
	public static void onBiomeRegister(RegistryEvent.Register<Biome> event) {
		ForgeProxy.LOGGER.info("Loading biomes of GenModifierPacks");
		GenModifierPack.init();
		GenModifierPack.loadInitialBiomes(event.getRegistry());
		GenModifierPack.forEach(pack -> pack.loadRemainingBiomes(event.getRegistry()));
	}

	@SubscribeEvent
	public static void onFeatureRegister(RegistryEvent.Register<Feature<?>> event) {
		registerFeatureSettings();
	}

	@SubscribeEvent
	public static void onPlacementRegister(RegistryEvent.Register<Placement<?>> event) {
		registerPlacementSettings();
	}
}
