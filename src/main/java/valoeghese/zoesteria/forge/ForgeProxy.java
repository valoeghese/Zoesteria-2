package valoeghese.zoesteria.forge;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
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
import valoeghese.zoesteria.abstr.Bridge;
import valoeghese.zoesteria.abstr.biome.ZoesteriaBiome;
import valoeghese.zoesteria.common.Zoesteria;
import valoeghese.zoesteria.common.ZoesteriaBiomeProvider;
import valoeghese.zoesteria.common.ZoesteriaFeatures;
import valoeghese.zoesteria.common.objects.ZoesteriaBlocks;
import valoeghese.zoesteria.common.objects.ZoesteriaItems;
import valoeghese.zoesteria.forge.client.ZoesteriaClientEventHandler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Mod("zoesteria")
public class ForgeProxy extends Bridge {
	public static final Logger LOGGER = LogManager.getLogger("Zoesteria");
	private static final Map<Climate.ParameterPoint, ResourceKey<Biome>> PLACEMENTS = new HashMap<>();
	private static final List<Biome> BIOMES_TO_REGISTER = new LinkedList<>();

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
		eventHandler.register(ZoesteriaFeatures.class);
		ZoesteriaItems.ITEMS.register(eventHandler);
		ZoesteriaBlocks.BLOCKS.register(eventHandler);
	}

	private void init(final FMLCommonSetupEvent event) {
		Zoesteria.init(this);

		event.enqueueWork(() -> {
			BiomeProviders.register(new ZoesteriaBiomeProvider());
		});
	}

	// REGISTRY STUFF

	static Iterable<Biome> getBiomesToRegister() {
		return BIOMES_TO_REGISTER;
	}

	// API STUFF

	@Override
	public ResourceKey<Biome> registerBiome(String id, ZoesteriaBiome biome, ParameterUtils.ParameterPointListBuilder placement) {
		ResourceKey<Biome> result = ResourceKey.create(Registry.BIOME_REGISTRY, Zoesteria.id(id));
		placement.buildVanilla().forEach(pp -> PLACEMENTS.put(pp, result));
		BIOMES_TO_REGISTER.add(build(biome).setRegistryName(id)); // build biome and store the pair to register later
		return result;
	}

	@Override
	public void log(String string, Object... formattedObjects) {
		LOGGER.info(string, formattedObjects);
	}

	// IMPLEMENTATION STUFF

	@Override
	public Map<Climate.ParameterPoint, ResourceKey<net.minecraft.world.level.biome.Biome>> getBiomePlacements() {
		return PLACEMENTS;
	}
}
