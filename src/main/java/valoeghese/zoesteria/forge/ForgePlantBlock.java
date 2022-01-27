package valoeghese.zoesteria.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.common.PlantType;
import valoeghese.zoesteria.abstr.Soil;
import valoeghese.zoesteria.common.objects.ZoesteriaPlantBlock;

public class ForgePlantBlock extends ZoesteriaPlantBlock {
	public ForgePlantBlock(Properties properties, double height, OffsetType offsetType, Soil soil) {
		super(properties, height, offsetType, soil);
		this.plantType = plantTypeFor(soil);
	}

	private final PlantType plantType;

	@Override
	public PlantType getPlantType(BlockGetter world, BlockPos pos) {
		return this.plantType;
	}

	static PlantType plantTypeFor(Soil soil) {
		return switch (soil) {
			case DIRTY -> PlantType.PLAINS;
			case SANDY_DESERT -> PlantType.DESERT;
			case SANDY_BEACH -> PlantType.BEACH;
		};
	}
}
