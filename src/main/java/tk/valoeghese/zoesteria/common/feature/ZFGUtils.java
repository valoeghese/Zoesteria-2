package tk.valoeghese.zoesteria.common.feature;

import java.util.LinkedHashMap;
import java.util.Map;

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

	// TODO untested
	@SuppressWarnings("unchecked")
	public static BlockState getBlockState(Container settings, String key) {
		Container state = settings.getContainer(key);
		Block block = getBlock(state, key);

		StateContainer<Block, BlockState> stateContainer = block.getStateContainer();

		BlockState result = block.getDefaultState();

		for (@SuppressWarnings("rawtypes") IProperty property : stateContainer.getProperties()) {
			if (state.containsKey(property.getName())) {
				result = add(result, property, state);
			}
		}

		return result;
	}

	// TODO untested
	@SuppressWarnings("unchecked")
	public static void putBlockState(EditableContainer settings, String key, BlockState value) {
		Map<String, Object> state = new LinkedHashMap<>();

		for (@SuppressWarnings("rawtypes") IProperty property : value.getProperties()) {
			state.put(property.getName(), property.getName(value.get(property)));
		}
	}

	private static <T extends Comparable<T>> BlockState add(BlockState result, IProperty<T> property, Container settings) {
		return result.with(property, (T) property.parseValue(settings.getStringValue(property.getName())).get());
	}
}
