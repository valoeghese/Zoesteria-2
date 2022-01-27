package valoeghese.zoesteria.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import valoeghese.zoesteria.abstr.Soil;
import valoeghese.zoesteria.common.objects.ZoesteriaDoublePlantBlock;

public class ForgeDoublePlantBlock extends ZoesteriaDoublePlantBlock implements IPlantable {
	public ForgeDoublePlantBlock(Properties properties, double height, OffsetType offsetType, Soil soil) {
		super(properties, height, offsetType, soil);
		this.plantType = ForgePlantBlock.plantTypeFor(soil);
	}

	private final PlantType plantType;

	@Override
	public PlantType getPlantType(BlockGetter world, BlockPos pos) {
		return this.plantType;
	}
}
