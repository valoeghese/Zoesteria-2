package valoeghese.zoesteria.common;

import java.util.Random;
import java.util.Set;

import com.mojang.datafixers.Dynamic;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;

public class NoneFoliagePlacer extends FoliagePlacer {
	public NoneFoliagePlacer() {
		this(0, 0);
	}

	private NoneFoliagePlacer(int useless, int useless2) {
		super(useless, useless2, ZoesteriaCommonEventHandler.NONE_FOLIAGE);
	}

	public <T> NoneFoliagePlacer(Dynamic<T> no) {
		this(no.get("radius").asInt(0), no.get("radius_random").asInt(0));
	}

	@Override
	public void func_225571_a_(IWorldGenerationReader p_225571_1_, Random p_225571_2_, TreeFeatureConfig p_225571_3_,
			int p_225571_4_, int p_225571_5_, int p_225571_6_, BlockPos p_225571_7_, Set<BlockPos> p_225571_8_) {
	}

	@Override
	public int func_225573_a_(Random p_225573_1_, int p_225573_2_, int p_225573_3_, TreeFeatureConfig p_225573_4_) {
		return 0;
	}

	@Override
	protected boolean func_225572_a_(Random p_225572_1_, int p_225572_2_, int p_225572_3_, int p_225572_4_,
			int p_225572_5_, int p_225572_6_) {
		return true;
	}

	@Override
	public int func_225570_a_(int p_225570_1_, int p_225570_2_, int p_225570_3_, int p_225570_4_) {
		return 0;
	}
}
