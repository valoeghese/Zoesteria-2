package tk.valoeghese.zoesteria.common;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidWithNoiseConfig;
import net.minecraftforge.common.BiomeDictionary;
import tk.valoeghese.zoesteria.api.IZoesteriaJavaModule;
import tk.valoeghese.zoesteria.api.Manifest;
import tk.valoeghese.zoesteria.api.ZoesteriaSerialisers;
import tk.valoeghese.zoesteria.api.biome.BiomeDecorations;
import tk.valoeghese.zoesteria.api.biome.BiomeTweaks;
import tk.valoeghese.zoesteria.api.biome.IZoesteriaBiome;
import tk.valoeghese.zoesteria.api.surface.Condition;
import tk.valoeghese.zoesteria.api.surface.ISurfaceBuilderTemplate;
import tk.valoeghese.zoesteria.api.surface.ZoesteriaSurfaceBuilder;
import tk.valoeghese.zoesteria.common.biome.BluffBiome;
import tk.valoeghese.zoesteria.common.biome.Woodlands;
import tk.valoeghese.zoesteria.common.feature.serialiser.TreeLikeFeatureConfigSerialiser;
import tk.valoeghese.zoesteria.common.feature.serialiser.TripleFeatureConfigSerialiser;
import tk.valoeghese.zoesteria.common.objects.ZoesteriaBlocks;
import tk.valoeghese.zoesteria.common.predicate.BiomeListPredicate;
import tk.valoeghese.zoesteria.common.predicate.OverworldBiomeDictionaryPredicate;
import tk.valoeghese.zoesteria.common.surface.AlterBlocksTemplate;
import tk.valoeghese.zoesteria.core.serialisers.feature.NoFeatureConfigSerialiser;
import tk.valoeghese.zoesteria.core.serialisers.feature.TreeFeatureConfigSerialiser;

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
		biomes.add(new Woodlands("low_woodlands", 8, 0.3f, 0.07f));
		biomes.add(new Woodlands("high_woodlands", 6, 1.3f, 0.03f));
		biomes.add(new Woodlands("woodlands_hills", 6, 0.45f, 0.38f, true));
		return biomes;
	}

	@Override
	public void registerFoliageSerialisers() {
		ZoesteriaSerialisers.registerFoliagePlacer(new ResourceLocation("zoesteria", "none"), NoneFoliagePlacer.class, NoneFoliagePlacerSerialiser.INSTANCE);
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
		surfaceBuilders.add(ZoesteriaSurfaceBuilder.create(
				"woodlands",
				ALTER_BLOCKS,
				ImmutableList.of(
						new AlterBlocksTemplate.Step(
								new Condition("noise_outside").withParameter("min", -2.45).withParameter("max", 2.65),
								Optional.of(Blocks.COARSE_DIRT),
								Optional.of(Blocks.COARSE_DIRT),
								Optional.empty(),
								true))));
		return surfaceBuilders;
	}

	@Override
	public void registerBiomePredicates() {
		ZoesteriaSerialisers.registerBiomePredicate(new BiomeListPredicate(null));
		ZoesteriaSerialisers.registerBiomePredicate(new OverworldBiomeDictionaryPredicate(null));
	}

	@Override
	public void addBiomeTweaks(BiomeTweaks tweaks) {
		BiomeDecorations beachDecorations = BiomeDecorations.create()
				.addDecoration(Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(
						new BlockClusterFeatureConfig.Builder(
								new SimpleBlockStateProvider(ZoesteriaBlocks.SPINIFEX_SMALL.get().getDefaultState()),
								new SimpleBlockPlacer()).tries(32).xSpread(12).zSpread(12).build()
						)
						.withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2))))
				.addDecoration(Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(
						new BlockClusterFeatureConfig.Builder(
								new SimpleBlockStateProvider(ZoesteriaBlocks.SPINIFEX_LARGE.get().getDefaultState()),
								new SimpleBlockPlacer()).tries(32).build()
						)
						.withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(1))))
				.addDecoration(Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(
						new BlockClusterFeatureConfig.Builder(
								new SimpleBlockStateProvider(ZoesteriaBlocks.SHORE_BINDWEED.get().getDefaultState()),
								new SimpleBlockPlacer()).tries(32).build()
						)
						.withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2))));

		tweaks.addTweak("beach_tweaks", new OverworldBiomeDictionaryPredicate(BiomeDictionary.Type.BEACH), beachDecorations);

		// ==== FOREST ====
		BiomeDecorations forestDecorations = BiomeDecorations.create()
				.addDecoration(Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(
						new BlockClusterFeatureConfig.Builder(
								new SimpleBlockStateProvider(ZoesteriaBlocks.OAK_LEAFCARPET.get().getDefaultState()),
								new SimpleBlockPlacer()).xSpread(0).ySpread(0).zSpread(0).tries(1).build()
						)
						/**
						 * The noise spread is the same as the original Zoesteria, but other constants are different.
						 */
						.withPlacement(Placement.TOP_SOLID_HEIGHTMAP_NOISE_BIASED.configure(new TopSolidWithNoiseConfig(3, 5 * 16 /*0.2 x chunkpos*/, 1.3, Heightmap.Type.OCEAN_FLOOR_WG))))
				.addDecoration(Decoration.VEGETAL_DECORATION, ZoesteriaCommonEventHandler.SIMPLE_BUSH
						.withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(1)))
						)
				.addDecoration(Decoration.VEGETAL_DECORATION, ZoesteriaCommonEventHandler.BERRY_BUSH
						.withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(1))))
				.addDecoration(Decoration.VEGETAL_DECORATION, ZoesteriaCommonEventHandler.CONFIGURED_TOADSTOOL
						.withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2))));

		tweaks.addTweak("forest_tweaks", new OverworldBiomeDictionaryPredicate(BiomeDictionary.Type.FOREST), forestDecorations);

		// ==== LUSH ====
		BiomeDecorations lushDecorations = BiomeDecorations.create()
				.addDecoration(Decoration.VEGETAL_DECORATION, ZoesteriaCommonEventHandler.SIMPLE_BUSH
						.withPlacement(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(2))));
		tweaks.addTweak("lush_biome_tweaks", new OverworldBiomeDictionaryPredicate(BiomeDictionary.Type.LUSH), lushDecorations);

		// === DESERT BIOMES ===
		BiomeDecorations desertDecorations = BiomeDecorations.create()
				.addDecoration(Decoration.VEGETAL_DECORATION, ZoesteriaCommonEventHandler.CONFIGURED_CACTLET
						.withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2))));
		tweaks.addTweak("cactlets", new BiomeListPredicate(Lists.newArrayList(
				Biomes.DESERT,
				Biomes.DESERT_HILLS,
				Biomes.DESERT_LAKES,
				Biomes.BADLANDS,
				Biomes.ERODED_BADLANDS)), desertDecorations);
	}

	@Override
	public List<ISurfaceBuilderTemplate<?>> createSurfaceBuilderTemplates() {
		return ImmutableList.of(ALTER_BLOCKS);
	}

	@Override
	public void registerFeatureSerialisers() {
		ZoesteriaSerialisers.registerFeatureSettings(ZoesteriaCommonEventHandler.BLUFF_PINE, TreeFeatureConfigSerialiser.BASE);
		ZoesteriaSerialisers.registerFeatureSettings(ZoesteriaCommonEventHandler.BLUFF_PINE_SAPLING, TreeFeatureConfigSerialiser.BASE);
		ZoesteriaSerialisers.registerFeatureSettings(ZoesteriaCommonEventHandler.BLUFF_RUINS, NoFeatureConfigSerialiser.INSTANCE);
		ZoesteriaSerialisers.registerFeatureSettings(ZoesteriaCommonEventHandler.FALLEN_LOG, TreeLikeFeatureConfigSerialiser.BASE);
		ZoesteriaSerialisers.registerFeatureSettings(ZoesteriaCommonEventHandler.LOLLIPOP_TREE, TreeFeatureConfigSerialiser.BASE);
		ZoesteriaSerialisers.registerFeatureSettings(ZoesteriaCommonEventHandler.TRIPLE_NOISE_SELECTOR, TripleFeatureConfigSerialiser.BASE);
	}

	private static final ISurfaceBuilderTemplate<AlterBlocksTemplate.Step> ALTER_BLOCKS = new AlterBlocksTemplate();
	public static final BiomeDictionary.Type AMPLIFIED = BiomeDictionary.Type.getType("AMPLIFIED");
}
