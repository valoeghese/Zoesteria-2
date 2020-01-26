package tk.valoeghese.zoesteria.core;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tk.valoeghese.zoesteria.core.genmodifierpack.GenModifierPack;

public class ZoesteriaRegistryHandler {
	@SubscribeEvent
	public static void onBiomeRegister(RegistryEvent.Register<Biome> event) {
		ZoesteriaMod.LOGGER.info("Loading biomes of GenModifierPacks");
		GenModifierPack.init();
		GenModifierPack.forEach(pack -> pack.loadBiomes(event.getRegistry()));
		GenModifierPack.flagLoadedPackBiomes();
	}
}
