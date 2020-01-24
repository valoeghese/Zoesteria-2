package tk.valoeghese.zoesteria.core.genmodifierpack;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import tk.valoeghese.zoesteriaconfig.api.container.Container;

public final class Utils {
	private Utils() {
	}

	public static Block getBlock(String id, Block defaultBlock) {
		if (id == null) {
			return defaultBlock;
		} else {
			ResourceLocation rl = new ResourceLocation(id);

			if (!blockKeys.contains(rl)) {
				return defaultBlock;
			}
			return ForgeRegistries.BLOCKS.getValue(rl);
		}
	}

	public static Boolean getBoolean(Container container, String key) {
		return getBoolean(container, key, null);
	}

	public static Boolean getBoolean(Container container, String key, Boolean defaultValue) {
		String stringValue = container.getStringValue(key);
		return stringValue == null ? defaultValue : Boolean.valueOf(stringValue);
	}

	private static final Set<ResourceLocation> blockKeys = ForgeRegistries.BLOCKS.getKeys();
}
