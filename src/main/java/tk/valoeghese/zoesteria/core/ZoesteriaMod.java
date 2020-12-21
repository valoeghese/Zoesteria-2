package tk.valoeghese.zoesteria.core;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.Tuple;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import tk.valoeghese.zoesteria.api.IZoesteriaJavaModule;
import tk.valoeghese.zoesteria.api.biome.BiomeDecorations;
import tk.valoeghese.zoesteria.api.biome.IBiomePredicate;
import tk.valoeghese.zoesteria.client.ZoesteriaClientEventHandler;
import tk.valoeghese.zoesteria.common.Zoesteria;
import tk.valoeghese.zoesteria.common.ZoesteriaCommonEventHandler;
import tk.valoeghese.zoesteria.common.objects.ZoesteriaBlocks;
import tk.valoeghese.zoesteria.common.objects.ZoesteriaItems;
import tk.valoeghese.zoesteria.core.pack.GenModifierPack;
import tk.valoeghese.zoesteria.core.pack.biome.BiomeFactory;

@Mod("zoesteria")
public class ZoesteriaMod {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Queue<Runnable> COMMON_PROCESSING = new LinkedList<>();

	public ZoesteriaMod() {
		final IEventBus eventHandler = FMLJavaModLoadingContext.get().getModEventBus();

		// core
		eventHandler.addListener(this::setup);
		eventHandler.register(ZoesteriaRegistryHandler.class);

		// common
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ZoesteriaClientEventHandler::safeRunClient);
		eventHandler.register(ZoesteriaCommonEventHandler.class);
		ZoesteriaItems.ITEMS.register(eventHandler);
		ZoesteriaBlocks.BLOCKS.register(eventHandler);

		IZoesteriaJavaModule.registerModule(new Zoesteria());
	}

	private void setup(final FMLCommonSetupEvent event) {
		LOGGER.info("Setting up Zoesteria!");

		// Tweaks
		ZoesteriaRegistryHandler.registerBiomePredicates();
		GenModifierPack.forEach(pack -> pack.loadTweaks());

		while (!COMMON_PROCESSING.isEmpty()) {
			COMMON_PROCESSING.remove().run();
		}

		// Apply Tweaks
		for (Biome biome : ForgeRegistries.BIOMES) {
			for (Tuple<IBiomePredicate, BiomeDecorations> tweak : TWEAKS_TO_ADD) {
				if (tweak.getA().test(biome)) {
					BiomeFactory.addDecorations(biome, tweak.getB(), false);
				}
			}
		}

		// Save Load Order Data
		BiomeFactory.writeLoadOrder(new File(GenModifierPack.ROOT_DIR, "internal_data.dat"));

		// START ZOESTERIA MODULE CODE
		BiomeDictionary.addTypes(Biomes.SHATTERED_SAVANNA, Zoesteria.AMPLIFIED);
		BiomeDictionary.addTypes(Biomes.SHATTERED_SAVANNA_PLATEAU, Zoesteria.AMPLIFIED);
		// END ZOESTERIA MODULE CODE
	}

	/**
	 * FOR INTERNAL USE ONLY.
	 */
	public static final void addTweak(Tuple<IBiomePredicate, BiomeDecorations> tweak) {
		TWEAKS_TO_ADD.add(tweak);
	}

	private static final List<Tuple<IBiomePredicate, BiomeDecorations>> TWEAKS_TO_ADD = new ArrayList<>();
}
