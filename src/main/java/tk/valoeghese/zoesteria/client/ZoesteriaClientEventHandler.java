package tk.valoeghese.zoesteria.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.BlockItem;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.GrassColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tk.valoeghese.zoesteria.common.objects.ZoesteriaBlocks;
import tk.valoeghese.zoesteria.forge.ForgeProxy;

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
		}, ZoesteriaBlocks.OVERGROWN_STONE.get(), ZoesteriaBlocks.OVERGROWN_RED_SANDSTONE.get());

		blockColourManager.register((state, lightReader, pos, tintIndex) -> {
			if (lightReader == null || pos == null) {
				return FoliageColors.get(0.5D, 1.0D);
			} else {
				return BiomeColors.getFoliageColor(lightReader, pos);
			}
		}, ZoesteriaBlocks.OAK_LEAFCARPET.get());

		blockColourManager.register((state, lightReader, pos, tintIndex) -> {
			return 0xFFEF1C;
		}, ZoesteriaBlocks.ASPEN_LEAVES.get());
	}

	@SubscribeEvent
	public static void onItemColourHandling(ColorHandlerEvent.Item event) {
		final BlockColors blockColourManager = event.getBlockColors();
		final ItemColors itemColourManager = event.getItemColors();

		itemColourManager.register((stack, tintIndex) -> blockColourManager.getColor(
				((BlockItem) stack.getItem()).getBlock().getDefaultState(),
				null,
				null,
				tintIndex), 
				ZoesteriaBlocks.OVERGROWN_STONE.get().asItem(),
				ZoesteriaBlocks.ASPEN_LEAVES.get().asItem(),
				ZoesteriaBlocks.OAK_LEAFCARPET.get().asItem(),
				ZoesteriaBlocks.OVERGROWN_RED_SANDSTONE.get().asItem());
	}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		ForgeProxy.LOGGER.info("Running Zoesteria client Setup.");
		// for overlay to work
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.OVERGROWN_STONE.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.OVERGROWN_RED_SANDSTONE.get(), RenderType.getCutoutMipped());

		// grasses
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.SPINIFEX_SMALL.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.SPINIFEX_LARGE.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.PINGAO.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.BLUE_FLAX_LILY.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.SANDHILL_CANEGRASS.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.PRAIRIE_GRASS.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.PRAIRIE_GRASS_TALL.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.PAMPAS_GRASS.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.TOETOE.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.MEADOW_CLOVERS.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.MEADOW_FLOWERS.get(), RenderType.getCutoutMipped());

		// carpet shaped plant blocks
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.SHORE_BINDWEED.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.OAK_LEAFCARPET.get(), RenderType.getCutoutMipped());

		// other plants
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.SMALL_BUSH.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.SMALL_BERRY_BUSH.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.CACTLET.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.LARGE_CACTLET.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.SMALL_CACTLET.get(), RenderType.getCutoutMipped());

		// fungi (not a plant)
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.TOADSTOOL.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.TOADSTOOLS.get(), RenderType.getCutoutMipped());

		// saplings
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.BLUFF_PINE_SAPLING.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ZoesteriaBlocks.ASPEN_SAPLING.get(), RenderType.getCutoutMipped());
	}

	public static void safeRunClient() {
		final IEventBus eventHandler = FMLJavaModLoadingContext.get().getModEventBus();
		eventHandler.register(ZoesteriaClientEventHandler.class);
	}
}
