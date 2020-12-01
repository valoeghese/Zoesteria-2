package tk.valoeghese.zoesteria.core;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tk.valoeghese.zoesteria.api.IZoesteriaJavaModule;
import tk.valoeghese.zoesteria.common.Zoesteria;

@Mod("zoesteria")
public class ZoesteriaMod {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Queue<Runnable> FEATURE_TASK = new LinkedList<>();

	public ZoesteriaMod() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().register(ZoesteriaRegistryHandler.class);
		MinecraftForge.EVENT_BUS.register(this);

		IZoesteriaJavaModule.registerModule(new Zoesteria());
	}

	private void setup(final FMLCommonSetupEvent event) {
		LOGGER.info("Setting up Zoesteria!");

		while (!FEATURE_TASK.isEmpty()) {
			FEATURE_TASK.remove().run();
		}
	}
}
