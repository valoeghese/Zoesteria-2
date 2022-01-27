package valoeghese.zoesteria.common;

import net.minecraft.resources.ResourceLocation;
import terrablender.api.ParameterUtils.*;
import valoeghese.zoesteria.abstr.Proxy;
import valoeghese.zoesteria.common.biome.Bluff;

public class Zoesteria {
	public static ResourceLocation id(String id) {
		return new ResourceLocation("zoesteria", id);
	}

	public static void setup(Proxy proxy) {
		proxy.log("Setting up Zoesteria!");
		addBiomes(proxy);
	}

	public static void init(Proxy proxy) {
	}

	private static void addBiomes(Proxy proxy) {
		// Bluff
		proxy.registerBiome("bluff", new Bluff(), new ParameterPointListBuilder()
				.depth(Depth.SURFACE)
				.continentalness(Continentalness.INLAND)
				.erosion(Erosion.span(Erosion.EROSION_2, Erosion.EROSION_4))
				.weirdness(Weirdness.HIGH_SLICE_VARIANT_ASCENDING, Weirdness.HIGH_SLICE_VARIANT_DESCENDING));
//
//		// Woodlands
//		biomes.add(new Woodlands("low_woodlands", 8, 0.3f, 0.07f));
//		biomes.add(new Woodlands("high_woodlands", 6, 1.3f, 0.03f));
//
//		// Australian Outback
//		biomes.add(new AustralianOutback(false)); // normal
//		biomes.add(new AustralianOutback(true)); // uluru
//
//		// Fields (Prairie, Pampas Grassland, Meadow)
//		biomes.add(new Prairie("prairie", 0.1f));
//		biomes.add(new Prairie("prairie_rise", 0.55f));
//
//		biomes.add(new Pampas("pampas", Pampas.Type.NORMAL));
//		biomes.add(new Pampas("pampas_flats", Pampas.Type.FLATS));
//		biomes.add(new Pampas("pampas_hills", Pampas.Type.HILLS));
//
//		biomes.add(new Meadow("meadow", 0.25f));
//		biomes.add(new Meadow("meadow_rise", 0.68f));
	}

//	@Override
//	public List<ZoesteriaSurfaceBuilder<?, ?, ?>> createSurfaceBuilders() {
//		List<ZoesteriaSurfaceBuilder<?, ?, ?>> surfaceBuilders = Lists.newArrayList();
//		surfaceBuilders.add(ZoesteriaSurfaceBuilder.create(
//				"bluff",
//				ALTER_BLOCKS,
//				new BaseSurfaceTemplateConfig("minecraft:default"),
//				ImmutableList.of(
//						new AlterBlocksTemplate.Step(
//								new Condition("z_preceeds").withParameter("value", 0),
//								ImmutableList.of(
//										new AlterBlocksTemplate.Step(
//												new Condition("noise_within").withParameter("min", 2.0).withParameter("max", 2.8),
//												Optional.of(Blocks.COBBLESTONE),
//												Optional.empty(),
//												Optional.empty(),
//												true)), true),
//						new AlterBlocksTemplate.Step(
//								new Condition("chance").withParameter("value", 3),
//								ImmutableList.of(
//										new AlterBlocksTemplate.Step(
//												new Condition("noise_within").withParameter("min", 2.0).withParameter("max", 2.7),
//												Optional.of(Blocks.MOSSY_COBBLESTONE),
//												Optional.empty(),
//												Optional.empty(),
//												true)
//										),
//								false),
//						new AlterBlocksTemplate.Step(
//								new Condition("noise_within").withParameter("min", 2.0).withParameter("max", 2.6),
//								Optional.of(Blocks.COBBLESTONE),
//								Optional.empty(),
//								Optional.empty(),
//								true)
//
//						)
//				));
//		surfaceBuilders.add(ZoesteriaSurfaceBuilder.create(
//				"woodlands",
//				ALTER_BLOCKS,
//				new BaseSurfaceTemplateConfig("minecraft:default"),
//				ImmutableList.of(
//						new AlterBlocksTemplate.Step(
//								new Condition("noise_outside").withParameter("min", -2.45).withParameter("max", 2.65),
//								Optional.of(Blocks.COARSE_DIRT),
//								Optional.of(Blocks.COARSE_DIRT),
//								Optional.empty(),
//								true))));
//		surfaceBuilders.add(ZoesteriaSurfaceBuilder.create(
//				"outback",
//				ALTER_BLOCKS,
//				new BaseSurfaceTemplateConfig("minecraft:default"),
//				ImmutableList.of(
//						new AlterBlocksTemplate.Step(
//								new Condition("noise_preceeds").withParameter("value", -2.5),
//								Optional.of(ZoesteriaBlocks.GIBBER.get()),
//								Optional.empty(),
//								Optional.empty(),
//								true),
//						new AlterBlocksTemplate.Step(
//								new Condition("noise_exceeds").withParameter("value", 2.6),
//								Optional.of(Blocks.GRASS_BLOCK),
//								Optional.of(Blocks.DIRT),
//								Optional.empty(),
//								true),
//						new AlterBlocksTemplate.Step(
//								new Condition("noise_exceeds").withParameter("value", 1.4),
//								ImmutableList.of(
//										new AlterBlocksTemplate.Step(
//												new Condition("chance").withParameter("value", 3),
//												Optional.of(ZoesteriaBlocks.OVERGROWN_RED_SANDSTONE.get()),
//												Optional.empty(),
//												Optional.empty(),
//												true)),
//								false))));
//		surfaceBuilders.add(ZoesteriaSurfaceBuilder.create(
//				"pampas_hills",
//				ALTER_BLOCKS,
//				new BaseSurfaceTemplateConfig("minecraft:default"),
//				ImmutableList.of(
//						new AlterBlocksTemplate.Step(
//								new Condition("noise_outside").withParameter("min", -2.85).withParameter("max", 2.85),
//								Optional.of(Blocks.STONE),
//								Optional.of(Blocks.STONE),
//								Optional.of(Blocks.STONE),
//								true),
//						new AlterBlocksTemplate.Step(
//								new Condition("noise_outside").withParameter("min", -2.5).withParameter("max", 2.5),
//								Optional.of(Blocks.GRAVEL),
//								Optional.of(Blocks.GRAVEL),
//								Optional.of(Blocks.GRAVEL),
//								true))));
//		return surfaceBuilders;
//	}

//	@Override
//	public void addBiomeTweaks(BiomeTweaks tweaks) {
//		BiomeDecorations beachDecorations = BiomeDecorations.create()
//				.addDecoration(Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(
//						new BlockClusterFeatureConfig.Builder(
//								new WeightedBlockStateProvider()
//								.addWeightedBlockstate(ZoesteriaBlocks.SPINIFEX_SMALL.get().getDefaultState(), 2)
//								.addWeightedBlockstate(ZoesteriaBlocks.SPINIFEX_LARGE.get().getDefaultState(), 1),
//								new SimpleBlockPlacer()).tries(32).xSpread(12).zSpread(12).build()
//						)
//						.withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(3))))
//				.addDecoration(Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(
//						new BlockClusterFeatureConfig.Builder(
//								new SimpleBlockStateProvider(ZoesteriaBlocks.PINGAO.get().getDefaultState()),
//								new SimpleBlockPlacer()).tries(32).build()
//						)
//						.withPlacement(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(3))))
//				.addDecoration(Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(
//						new BlockClusterFeatureConfig.Builder(
//								new SimpleBlockStateProvider(ZoesteriaBlocks.SHORE_BINDWEED.get().getDefaultState()),
//								new SimpleBlockPlacer()).tries(32).build()
//						)
//						.withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2))));
//
//		tweaks.addTweak("beach_tweaks", new OverworldBiomeDictionaryPredicate(BiomeDictionary.Type.BEACH), beachDecorations);
//
//		// ==== FOREST ====
//		BiomeDecorations forestDecorations = BiomeDecorations.create()
//				.addDecoration(Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(
//						new BlockClusterFeatureConfig.Builder(
//								new SimpleBlockStateProvider(ZoesteriaBlocks.OAK_LEAFCARPET.get().getDefaultState()),
//								new SimpleBlockPlacer()).xSpread(0).ySpread(0).zSpread(0).tries(1).build()
//						)
//						/**
//						 * The noise spread is the same as the original Zoesteria, but other constants are different.
//						 */
//						.withPlacement(Placement.TOP_SOLID_HEIGHTMAP_NOISE_BIASED.configure(new TopSolidWithNoiseConfig(3, 5 * 16 /*0.2 x chunkpos*/, 1.3, Heightmap.Type.OCEAN_FLOOR_WG))))
//				.addDecoration(Decoration.VEGETAL_DECORATION, ZoesteriaCommonEventHandler.SIMPLE_BUSH
//						.withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(1)))
//						)
//				.addDecoration(Decoration.VEGETAL_DECORATION, ZoesteriaCommonEventHandler.BERRY_BUSH
//						.withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(1))))
//				.addDecoration(Decoration.VEGETAL_DECORATION, ZoesteriaCommonEventHandler.CONFIGURED_TOADSTOOL
//						.withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2))));
//
//		tweaks.addTweak("forest_tweaks", new OverworldBiomeDictionaryPredicate(BiomeDictionary.Type.FOREST), forestDecorations);
//
//		// ==== LUSH ====
//		BiomeDecorations lushDecorations = BiomeDecorations.create()
//				.addDecoration(Decoration.VEGETAL_DECORATION, ZoesteriaCommonEventHandler.SIMPLE_BUSH
//						.withPlacement(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(2))));
//		tweaks.addTweak("lush_biome_tweaks", new OverworldBiomeDictionaryPredicate(BiomeDictionary.Type.LUSH), lushDecorations);
//
//		// === DESERT BIOMES ===
//		BiomeDecorations desertDecorations = BiomeDecorations.create()
//				.addDecoration(Decoration.VEGETAL_DECORATION, ZoesteriaCommonEventHandler.CONFIGURED_CACTLET
//						.withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(1))))
//				.addDecoration(Decoration.VEGETAL_DECORATION, ZoesteriaCommonEventHandler.CONFIGURED_LARGE_CACTLET
//						.withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(1))));
//		tweaks.addTweak("cactlets", new BiomeListPredicate(Lists.newArrayList(
//				Biomes.DESERT,
//				Biomes.DESERT_HILLS,
//				Biomes.DESERT_LAKES,
//				Biomes.BADLANDS,
//				Biomes.ERODED_BADLANDS)), desertDecorations);
//
//		// === WETLAND AND RIVER BIOMES ===
//		BiomeDecorations waterDecorations = BiomeDecorations.create()
//				.addDecoration(Decoration.VEGETAL_DECORATION, ZoesteriaCommonEventHandler.CONFIGURED_TOETOE
//						.withPlacement(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(6))));
//		tweaks.addTweak("water_biome_tweaks", new BiomeListPredicate(Lists.newArrayList(
//				Biomes.RIVER,
//				Biomes.SWAMP,
//				Biomes.OCEAN,
//				Biomes.LUKEWARM_OCEAN,
//				Biomes.BEACH)), waterDecorations);
//	}
}
