package tk.valoeghese.zoesteria.core.serialisers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import net.minecraft.block.BlockState;
import net.minecraft.util.WeightedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.ForestFlowerBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.PlainFlowerBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import tk.valoeghese.zoesteria.api.ZFGUtils;
import tk.valoeghese.zoesteriaconfig.api.ZoesteriaConfig;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public final class BlockStateProviderHandler {
	private BlockStateProviderHandler() {
	}

	public static BlockStateProvider stateProvider(Container data) {
		String provider = data.getStringValue("stateProvider");

		switch (provider) {
		case "simple":
			return simpleStateProvider(data);
		case "plains_flower":
			return new PlainFlowerBlockStateProvider();
		case "forest_flower":
			return new ForestFlowerBlockStateProvider();
		case "weighted":
			return weightedStateProvider(data);
		default:
			throw new RuntimeException("Invalid state provider or Zoesteria cannot currently handle the state provider:" + provider);
		}
	}

	public static Container serialiseStateProvider(BlockStateProvider stateProvider) {
		// different formatting because my eyes go weird reading this in my usual formatting
		if (stateProvider instanceof SimpleBlockStateProvider)
		{
			return serialiseSimpleStateProvider(stateProvider.getBlockState(DOES_NOTHING, BlockPos.ZERO));
		}
		else if (stateProvider instanceof PlainFlowerBlockStateProvider)
		{
			EditableContainer result = ZoesteriaConfig.createWritableConfig(new LinkedHashMap<>());
			result.putStringValue("stateProvider", "plains_flower");
			return result;
		}
		else if (stateProvider instanceof ForestFlowerBlockStateProvider)
		{
			EditableContainer result = ZoesteriaConfig.createWritableConfig(new LinkedHashMap<>());
			result.putStringValue("stateProvider", "forest_flower");
			return result;
		}
		else if (stateProvider instanceof WeightedBlockStateProvider)
		{
			List<WeightedList<BlockState>.Entry<? extends BlockState>> states = ((WeightedBlockStateProvider) stateProvider).weightedStates.func_226319_c_()
					.collect(Collectors.toList());
			List<Object> serialised = new ArrayList<>();

			// add weighted states to a list
			for (WeightedList<BlockState>.Entry<? extends BlockState> entry : states) {
				Map<String, Object> weightedState = new LinkedHashMap<>();
				weightedState.put("state", ZFGUtils.serialiseStateContainer(entry.func_220647_b()));
				weightedState.put("weight", String.valueOf(entry.func_226322_b_())); // TODO String.valueOf should be removeable, but I want to wait for a bug fix to do with re-reading values.
				serialised.add(weightedState);
			}

			// put list in a container.
			EditableContainer result = ZoesteriaConfig.createWritableConfig(new LinkedHashMap<>());
			result.putStringValue("stateProvider", "weighted");
			result.putList("weightedStates", serialised);
			return result;
		}
		else
		{
			throw new RuntimeException("Zoesteria cannot currently handle the state provider:" + stateProvider);
		}
	}

	public static BlockStateProvider simpleStateProvider(Container data) {
		BlockState state = ZFGUtils.getBlockState(data, "state");
		return new SimpleBlockStateProvider(state);
	}

	@SuppressWarnings("unchecked")
	private static BlockStateProvider weightedStateProvider(Container data) {
		List<Object> serialised = data.getList("weightedStates");
		WeightedBlockStateProvider result = new WeightedBlockStateProvider();

		for (Object o : serialised) {
			Map<String, Object> weightedState = (Map<String, Object>) o;

			result.addWeightedBlockstate(
					ZFGUtils.deserialiseStateContainer(
							ZoesteriaConfig.createWritableConfig((Map<String, Object>) weightedState.get("state"))
							),
					Integer.valueOf((String) weightedState.get("weight")));
		}

		return result;
	}

	public static Container serialiseSimpleStateProvider(BlockState state) {
		EditableContainer result = ZoesteriaConfig.createWritableConfig(new LinkedHashMap<>());
		result.putStringValue("stateProvider", "simple");
		ZFGUtils.putBlockState(result, "state", state);
		return result;
	}

	private static final Random DOES_NOTHING = new Random();
}
