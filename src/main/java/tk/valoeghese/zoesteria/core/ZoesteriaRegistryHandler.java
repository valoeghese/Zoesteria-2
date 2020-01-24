package tk.valoeghese.zoesteria.core;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import tk.valoeghese.zoesteria.core.genmodifierpack.GenModifierPack;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.FORGE)
public class ZoesteriaRegistryHandler {
	@SubscribeEvent
	public static void onBiomeRegister(RegistryEvent.Register<Biome> event) {
		GenModifierPack.init();
		GenModifierPack.forEach(pack -> pack.loadBiomes());
	}
}
