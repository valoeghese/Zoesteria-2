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
import tk.valoeghese.zoesteria.api.surface.ISurfaceBuilderTemplate;
import tk.valoeghese.zoesteria.api.surface.ZoesteriaSurfaceBuilder;
import tk.valoeghese.zoesteria.common.biome.BluffBiome;
import tk.valoeghese.zoesteria.common.feature.TreeFeatureConfigHandler;
import tk.valoeghese.zoesteria.common.surface.AlterMaterialsTemplate;
import tk.valoeghese.zoesteria.core.ZoesteriaRegistryHandler;

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
				ALTER_MATERIALS,
				ImmutableList.of(
						new AlterMaterialsTemplate.Step(
								new AlterMaterialsTemplate.Condition("noise_within").withParameter("min", 2.0).withParameter("max", 2.8),
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
		return ImmutableList.of();
	}

	@Override
	public void registerFeatureSettings() {
		FeatureSerialisers.registerFeatureSettings(ZoesteriaRegistryHandler.BLUFF_PINE, TreeFeatureConfigHandler.BASE);
	}

	private static final ISurfaceBuilderTemplate<AlterMaterialsTemplate.Step> ALTER_MATERIALS = new AlterMaterialsTemplate();
}
