package tk.valoeghese.zoesteria.core;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class ZFGUtils {
	private ZFGUtils() {
	}

	public static Block getBlock(Container settings, String key) {
		return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(settings.getStringValue(key)));
	}

	public static void putBlock(EditableContainer settings, String key, Block value) {
		settings.putStringValue(key, ForgeRegistries.BLOCKS.getKey(value).toString());
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

	@SuppressWarnings("unchecked")
	public static BlockState getBlockState(Container settings, String key) {
		if (key == null) {
			throw new NullPointerException("Key for block state is null!");
		}

		Container state = settings.getContainer(key);

		if (state == null) {
			throw new NullPointerException("State container of block state is null!");
		}

		Block block = getBlock(state, "block");

		StateContainer<Block, BlockState> stateContainer = block.getStateContainer();

		BlockState result = block.getDefaultState();

		for (@SuppressWarnings("rawtypes") IProperty property : stateContainer.getProperties()) {
			if (state.containsKey(property.getName())) {
				result = add(result, property, state);
			}
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public static void putBlockState(EditableContainer settings, String key, BlockState value) {
		Map<String, Object> state = new LinkedHashMap<>();

		for (@SuppressWarnings("rawtypes") IProperty property : value.getProperties()) {
			state.put(property.getName(), property.getName(value.get(property)));
		}

		state.put("block", ForgeRegistries.BLOCKS.getKey(value.getBlock()).toString());

		settings.putMap(key, state);
	}

	private static <T extends Comparable<T>> BlockState add(BlockState result, IProperty<T> property, Container settings) {
		return result.with(property, (T) property.parseValue(settings.getStringValue(property.getName())).get());
	}

	// TODO port to regular getBoolean behaviour from ZoesteriaConfig

	public static Boolean getBooleanOrNull(Container container, String key) {
		return getBooleanOrDefault(container, key, null);
	}

	public static Boolean getBooleanOrDefault(Container container, String key, Boolean defaultValue) {
		String stringValue = container.getStringValue(key);
		return stringValue == null ? defaultValue : Boolean.valueOf(stringValue);
	}

	private static final Set<ResourceLocation> blockKeys = ForgeRegistries.BLOCKS.getKeys();
}
