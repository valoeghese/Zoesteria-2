package tk.valoeghese.zoesteria.core.serialisers;

import java.util.LinkedHashMap;

import net.minecraft.world.gen.blockplacer.BlockPlacer;
import net.minecraft.world.gen.blockplacer.DoublePlantBlockPlacer;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import tk.valoeghese.zoesteriaconfig.api.ZoesteriaConfig;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class BlockPlacerHandler {
	public static BlockPlacer deserialize(Container data) {
		switch (data.getStringValue("type")) {
		case "simple_block_placer":
			return new SimpleBlockPlacer();
		case "double_plant_placer":
			return new DoublePlantBlockPlacer();
		default:
			throw new RuntimeException("Unknown or unsupported block placer type!");
		}
	}

	public static Container serialize(BlockPlacer placer) {
		EditableContainer result = ZoesteriaConfig.createWritableConfig(new LinkedHashMap<>());

		if (placer instanceof SimpleBlockPlacer) {
			result.putStringValue("type", "simple_block_placer");
		} else if (placer instanceof DoublePlantBlockPlacer) {
			result.putStringValue("type", "double_plant_placer");
		} else {
			throw new RuntimeException("Unsupported block placer type!");
		}

		return result;
	}
}
