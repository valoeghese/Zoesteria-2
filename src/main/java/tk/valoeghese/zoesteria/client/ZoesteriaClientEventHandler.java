package tk.valoeghese.zoesteria.client;

import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.BlockItem;
import net.minecraft.world.GrassColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tk.valoeghese.zoesteria.common.objects.ZoesteriaBlocks;

public class ZoesteriaClientEventHandler {
	@SubscribeEvent
	public static void onBlockColourHandling(ColorHandlerEvent.Block event) {
		BlockColors blockColourManager = event.getBlockColors();

		blockColourManager.register((state, lightReader, pos, tintIndex) -> {
			if (lightReader == null || pos == null) {
				return GrassColors.get(0.5D, 1.0D); // byg uses these constants and I trust corgitaco
			} else {
				return BiomeColors.getGrassColor(lightReader, pos);
			}
		}, ZoesteriaBlocks.OVERGROWN_STONE.get());
	}

	@SubscribeEvent
	public static void onItemColourHandling(ColorHandlerEvent.Item event) {
		final BlockColors blockColourManager = event.getBlockColors();
		final ItemColors itemColourManager = event.getItemColors();

		// block items with grass colouring ONLY
		itemColourManager.register((stack, tintIndex) -> blockColourManager.getColor(
				((BlockItem) stack.getItem()).getBlock().getDefaultState(),
				null,
				null,
				tintIndex), ZoesteriaBlocks.OVERGROWN_STONE.get().asItem());
	}
}
