package valoeghese.zoesteria.common.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.Random;

public abstract class OldStyleFeature<FC extends FeatureConfiguration> extends Feature<FC> {
	public OldStyleFeature(Codec<FC> pCodec) {
		super(pCodec);
	}

	@Override
	public final boolean place(FeaturePlaceContext<FC> ctx) {
		return this.place(ctx.level(), ctx.chunkGenerator(), ctx.random(), ctx.origin(), ctx.config());
	}

	abstract protected boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, Random random, BlockPos origin, FC config);
}
