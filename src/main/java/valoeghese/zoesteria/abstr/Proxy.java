package valoeghese.zoesteria.abstr;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import terrablender.api.ParameterUtils;
import valoeghese.zoesteria.abstr.biome.ZoesteriaBiome;

/**
 * An interface for a bridge between a worldgen mod and a given minecraft version and modding api.
 * @implNote implementations should extend {@link Bridge} unless they want to do a lot more work themselves.
 */
public interface Proxy {
	/**
	 * Register a biome with a given id with the given generation and biome.
	 * @param id the id. Will be zoesteria:${id}.
	 * @param biome the biome to register at the given id.
	 * @param placement the placements for the given biome in the world. i.e. where it will generate.
	 * @return a lazy registered reference of the biome's resource key.
	 */
	ResourceKey<Biome> registerBiome(String id, ZoesteriaBiome biome, ParameterUtils.ParameterPointListBuilder placement);

	/**
	 * Log to the logger.
	 * @param string the message to log.
	 * @param formattedObjects the formatted objects in the message.
	 */
	void log(String string, Object ... formattedObjects);
}
