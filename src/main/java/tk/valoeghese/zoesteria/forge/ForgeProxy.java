package tk.valoeghese.zoesteria.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tk.valoeghese.zoesteria.abstr.Proxy;
import tk.valoeghese.zoesteria.client.ZoesteriaClientEventHandler;
import tk.valoeghese.zoesteria.common.Zoesteria;
import tk.valoeghese.zoesteria.common.ZoesteriaCommonEventHandler;
import tk.valoeghese.zoesteria.common.objects.ZoesteriaBlocks;
import tk.valoeghese.zoesteria.common.objects.ZoesteriaItems;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Mod("zoesteria")
public class ForgeProxy implements Proxy {
	public static final Logger LOGGER = LogManager.getLogger("Zoesteria");

	public ForgeProxy() {
		final IEventBus eventHandler = FMLJavaModLoadingContext.get().getModEventBus();

		Zoesteria.setup(this);
		// core
		eventHandler.addListener(this::setup);
		eventHandler.register(ZoesteriaRegistryHandler.class);

		// common
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ZoesteriaClientEventHandler::safeRunClient);
		eventHandler.register(ZoesteriaCommonEventHandler.class);
		ZoesteriaItems.ITEMS.register(eventHandler);
		ZoesteriaBlocks.BLOCKS.register(eventHandler);
	}

	private void setup(final FMLCommonSetupEvent event) {
		LOGGER.info("Setting up Zoesteria!");

		ZoesteriaRegistryHandler.registerBiomePredicates();
	}
}
