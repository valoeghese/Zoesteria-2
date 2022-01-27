package valoeghese.zoesteria.forge;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import terrablender.api.BiomeProviders;
import terrablender.api.ParameterUtils;
import terrablender.worldgen.TBClimate;
import valoeghese.zoesteria.abstr.Bridge;
import valoeghese.zoesteria.abstr.biome.ZoesteriaBiome;
import valoeghese.zoesteria.client.ZoesteriaClientEventHandler;
import valoeghese.zoesteria.common.Zoesteria;
import valoeghese.zoesteria.common.ZoesteriaBiomeProvider;
import valoeghese.zoesteria.common.ZoesteriaCommonEventHandler;
import valoeghese.zoesteria.common.objects.ZoesteriaBlocks;
import valoeghese.zoesteria.common.objects.ZoesteriaItems;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Mod("zoesteria")
public class ForgeProxy extends Bridge {
	public static final Logger LOGGER = LogManager.getLogger("Zoesteria");
	private static final Map<TBClimate.ParameterPoint, ResourceKey<Biome>> PLACEMENTS = new HashMap<>();
	private static final List<ResourceKey<Biome>> BIOMES_TO_REGISTER = new LinkedList<>(); // we merely add then iterate thus LinkedList

	// FORGE STUFF

	public ForgeProxy() {
		Bridge.setBridge(this);

		// set up
		final IEventBus eventHandler = FMLJavaModLoadingContext.get().getModEventBus();
		Zoesteria.setup(this);

		// core
		eventHandler.addListener(this::init);
		eventHandler.register(ZoesteriaRegistryHandler.class);

		// common
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ZoesteriaClientEventHandler::safeRunClient);
		eventHandler.register(ZoesteriaCommonEventHandler.class);
		ZoesteriaItems.ITEMS.register(eventHandler);
		ZoesteriaBlocks.BLOCKS.register(eventHandler);
	}

	private void init(final FMLCommonSetupEvent event) {
		Zoesteria.init(this);

		event.enqueueWork(() -> {
			BiomeProviders.register(new ZoesteriaBiomeProvider());
		});
	}

	// API STUFF

	@Override
	public ResourceKey<Biome> registerBiome(String id, ZoesteriaBiome biome, ParameterUtils.ParameterPointListBuilder placement) {
		ResourceKey result = ResourceKey.create(Registry.BIOME_REGISTRY, Zoesteria.id(id));
		// TODO build the ZoesteriaBiome
		placement.build().forEach(pp -> PLACEMENTS.put(pp, result));
		BIOMES_TO_REGISTER.add(result);
		return result;
	}

	@Override
	public void log(String string, Object... formattedObjects) {
		LOGGER.info(string, formattedObjects);
	}

	// IMPLEMENTATION STUFF

	@Override
	public Map<TBClimate.ParameterPoint, ResourceKey<net.minecraft.world.level.biome.Biome>> getBiomePlacements() {
		return PLACEMENTS;
	}
}
