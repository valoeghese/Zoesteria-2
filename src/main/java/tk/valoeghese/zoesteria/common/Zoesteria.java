package tk.valoeghese.zoesteria.common;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.block.Blocks;
import tk.valoeghese.zoesteria.api.IZoesteriaJavaModule;
import tk.valoeghese.zoesteria.api.Manifest;
import tk.valoeghese.zoesteria.api.biome.IZoesteriaBiome;
import tk.valoeghese.zoesteria.api.feature.FeatureSerialisers;
import tk.valoeghese.zoesteria.api.surface.Condition;
import tk.valoeghese.zoesteria.api.surface.ISurfaceBuilderTemplate;
import tk.valoeghese.zoesteria.api.surface.ZoesteriaSurfaceBuilder;
import tk.valoeghese.zoesteria.common.biome.BluffBiome;
import tk.valoeghese.zoesteria.common.surface.AlterBlocksTemplate;
import tk.valoeghese.zoesteria.core.serialisers.NoFeatureConfigHandler;
import tk.valoeghese.zoesteria.core.serialisers.TreeFeatureConfigHandler;

public class Zoesteria implements IZoesteriaJavaModule {
	@Override
	public String packId() {
		return "zoesteria";
	}

	@Override
	public Manifest createManifest() {
		return Manifest.createSchema0(this);
	}

	@Override
	public List<IZoesteriaBiome> createBiomes() {
		List<IZoesteriaBiome> biomes = Lists.newArrayList();
		biomes.add(new BluffBiome());
		return biomes;
	}

	@Override
	public List<ZoesteriaSurfaceBuilder<?, ?>> createSurfaceBuilders() {
		List<ZoesteriaSurfaceBuilder<?, ?>> surfaceBuilders = Lists.newArrayList();
		surfaceBuilders.add(ZoesteriaSurfaceBuilder.create(
				"bluff",
				ALTER_BLOCKS,
				ImmutableList.of(
						new AlterBlocksTemplate.Step(
								new Condition("z_preceeds").withParameter("value", 0),
								ImmutableList.of(
										new AlterBlocksTemplate.Step(
												new Condition("noise_within").withParameter("min", 2.0).withParameter("max", 2.8),
												Optional.of(Blocks.COBBLESTONE),
												Optional.empty(),
												Optional.empty(),
												true)), true),
						new AlterBlocksTemplate.Step(
								new Condition("chance").withParameter("value", 3),
								ImmutableList.of(
										new AlterBlocksTemplate.Step(
												new Condition("noise_within").withParameter("min", 2.0).withParameter("max", 2.7),
												Optional.of(Blocks.MOSSY_COBBLESTONE),
												Optional.empty(),
												Optional.empty(),
												true)
										),
								false),
						new AlterBlocksTemplate.Step(
								new Condition("noise_within").withParameter("min", 2.0).withParameter("max", 2.6),
								Optional.of(Blocks.COBBLESTONE),
								Optional.empty(),
								Optional.empty(),
								true)

						)
				));
		return surfaceBuilders;
	}

	@Override
	public List<ISurfaceBuilderTemplate<?>> createSurfaceBuilderTemplates() {
		return ImmutableList.of(ALTER_BLOCKS);
	}

	@Override
	public void registerFeatureSettings() {
		FeatureSerialisers.registerFeatureSettings(ZoesteriaCommonEventHandler.BLUFF_PINE, TreeFeatureConfigHandler.BASE);
		FeatureSerialisers.registerFeatureSettings(ZoesteriaCommonEventHandler.BLUFF_PINE_SAPLING, TreeFeatureConfigHandler.BASE);
		FeatureSerialisers.registerFeatureSettings(ZoesteriaCommonEventHandler.BLUFF_RUINS, NoFeatureConfigHandler.INSTANCE);
	}

	private static final ISurfaceBuilderTemplate<AlterBlocksTemplate.Step> ALTER_BLOCKS = new AlterBlocksTemplate();
}
