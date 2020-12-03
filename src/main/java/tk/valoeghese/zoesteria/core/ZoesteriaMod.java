package tk.valoeghese.zoesteria.core;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tk.valoeghese.zoesteria.api.IZoesteriaJavaModule;
import tk.valoeghese.zoesteria.common.Zoesteria;
import tk.valoeghese.zoesteria.common.objects.ZoesteriaBlocks;
import tk.valoeghese.zoesteria.common.objects.ZoesteriaItems;

@Mod("zoesteria")
public class ZoesteriaMod {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Queue<Runnable> COMMON_PROCESSING = new LinkedList<>();

	public ZoesteriaMod() {
		IEventBus eventHandler = FMLJavaModLoadingContext.get().getModEventBus();

		eventHandler.addListener(this::setup);
		eventHandler.register(ZoesteriaRegistryHandler.class);
		ZoesteriaItems.ITEMS.register(eventHandler);
		ZoesteriaBlocks.BLOCKS.register(eventHandler);

		MinecraftForge.EVENT_BUS.register(this);

		IZoesteriaJavaModule.registerModule(new Zoesteria());
	}

	private void setup(final FMLCommonSetupEvent event) {
		LOGGER.info("Setting up Zoesteria!");

		while (!COMMON_PROCESSING.isEmpty()) {
			COMMON_PROCESSING.remove().run();
		}
	}
}
