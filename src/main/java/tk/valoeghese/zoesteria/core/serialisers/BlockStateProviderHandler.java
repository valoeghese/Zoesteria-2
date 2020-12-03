package tk.valoeghese.zoesteria.core.serialisers;

import java.util.LinkedHashMap;
import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.ForestFlowerBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.PlainFlowerBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import tk.valoeghese.zoesteria.core.ZFGUtils;
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
		default:
			throw new RuntimeException("Invalid state provider or Zoesteria cannot currently handle the state provider:" + provider);
		}
	}

	public static Container serialiseStateProvider(BlockStateProvider stateProvider) {
		// different formatting because my eyes go weird reading this in my usual formatting
		if (stateProvider instanceof SimpleBlockStateProvider)
		{
			return serialiseSimpleStateProvider(stateProvider.getBlockState(USELESS, BlockPos.ZERO));
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
		else
		{
			throw new RuntimeException("Zoesteria cannot currently handle the state provider:" + stateProvider);
		}
	}

	public static SimpleBlockStateProvider simpleStateProvider(Container data) {
		BlockState state = ZFGUtils.getBlockState(data, "state");
		return new SimpleBlockStateProvider(state);
	}

	public static Container serialiseSimpleStateProvider(BlockState state) {
		EditableContainer result = ZoesteriaConfig.createWritableConfig(new LinkedHashMap<>());
		result.putStringValue("stateProvider", "simple");
		ZFGUtils.putBlockState(result, "state", state);
		return result;
	}

	private static final Random USELESS = new Random();
}
