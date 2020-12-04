package tk.valoeghese.zoesteria.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.BlockItem;
import net.minecraft.world.GrassColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tk.valoeghese.zoesteria.common.objects.ZoesteriaBlocks;
import tk.valoeghese.zoesteria.core.ZoesteriaMod;

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

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		ZoesteriaMod.LOGGER.info("Running Zoesteria client Setup.");
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.OVERGROWN_STONE.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.SPINIFEX_SMALL.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.SPINIFEX_LARGE.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.BLUFF_PINE_SAPLING.get(), RenderType.getCutoutMipped());
	}

	public static void safeRunClient() {
		final IEventBus eventHandler = FMLJavaModLoadingContext.get().getModEventBus();
		eventHandler.register(ZoesteriaClientEventHandler.class);
	}
}