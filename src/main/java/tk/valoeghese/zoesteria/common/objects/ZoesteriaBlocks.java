package tk.valoeghese.zoesteria.common.objects;

import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tk.valoeghese.zoesteria.common.feature.sapling.AspenSaplingTree;
import tk.valoeghese.zoesteria.common.feature.sapling.BluffPineSaplingTree;

// TODO better plant AABBs
public final class ZoesteriaBlocks {
	private ZoesteriaBlocks() {
	}

	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, "zoesteria");
	public static final SoundType OVERGROWN_STONE_SOUND = new SoundType(1.0F, 1.0F, SoundEvents.BLOCK_STONE_BREAK, SoundEvents.BLOCK_GRASS_STEP, SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);

	// SOILS / GROUND

	public static final RegistryObject<OvergrownStoneBlock> OVERGROWN_STONE = createWithBlockitem(
			"overgrown_stone",
			() -> new OvergrownStoneBlock(Blocks.STONE, Block.Properties.create(Material.ROCK, MaterialColor.STONE).tickRandomly().hardnessAndResistance(1.5F, 6.0F).sound(OVERGROWN_STONE_SOUND).harvestTool(ToolType.PICKAXE).harvestLevel(0)),
			new Item.Properties().group(ZoesteriaItems.CREATIVE_TAB));

	public static final RegistryObject<OvergrownStoneBlock> OVERGROWN_RED_SANDSTONE = createWithBlockitem(
			"overgrown_red_sandstone",
			() -> new OvergrownStoneBlock(Blocks.RED_SANDSTONE, Block.Properties.create(Material.ROCK, MaterialColor.ADOBE).tickRandomly().hardnessAndResistance(1.8F).sound(OVERGROWN_STONE_SOUND).harvestTool(ToolType.PICKAXE).harvestLevel(0)),
			new Item.Properties().group(ZoesteriaItems.CREATIVE_TAB));

	// LEAVES

	public static final RegistryObject<LeavesBlock> ASPEN_LEAVES = createWithBlockitem(
			"aspen_leaves",
			() -> new LeavesBlock(Block.Properties.from(Blocks.SPRUCE_LEAVES)),
			new Item.Properties().group(ZoesteriaItems.CREATIVE_TAB));

	public static final RegistryObject<LeavesBlock> BLUFF_PINE_LEAVES = createWithBlockitem(
			"bluff_pine_leaves",
			() -> new LeavesBlock(Block.Properties.from(Blocks.SPRUCE_LEAVES)),
			new Item.Properties().group(ZoesteriaItems.CREATIVE_TAB));

	// PLANTS
	private static final Predicate<Block> SAND_TYPE = b -> b == Blocks.SAND || b == Blocks.RED_SAND || b == Blocks.COARSE_DIRT;
	private static final Predicate<Block> OUTBACK_SAND_TYPE = b -> b == Blocks.SAND || b == Blocks.RED_SAND || b == Blocks.COARSE_DIRT || b == Blocks.RED_SANDSTONE || b == Blocks.SANDSTONE;

	public static final RegistryObject<ZoesteriaPlantBlock> SPINIFEX_SMALL = createWithBlockitem(
			"spinifex_small",
			() -> new ZoesteriaPlantBlock(Block.Properties.from(Blocks.GRASS), 5.4D, Block.OffsetType.XYZ, SAND_TYPE, PlantType.Desert),
			new Item.Properties().group(ZoesteriaItems.CREATIVE_TAB));

	public static final RegistryObject<ZoesteriaPlantBlock> SPINIFEX_LARGE = createWithBlockitem(
			"spinifex_large",
			() -> new ZoesteriaPlantBlock(Block.Properties.from(Blocks.GRASS), 8.0D, Block.OffsetType.XYZ, SAND_TYPE, PlantType.Desert),
			new Item.Properties().group(ZoesteriaItems.CREATIVE_TAB));

	public static final RegistryObject<ZoesteriaPlantBlock> SHORE_BINDWEED = createWithBlockitem(
			"shore_bindweed",
			() -> new ZoesteriaPlantBlock(Block.Properties.from(Blocks.GRASS), 1.0D, Block.OffsetType.XZ, b -> b == Blocks.GRAVEL || b == Blocks.STONE || b == Blocks.ANDESITE || b == Blocks.DIORITE || b == Blocks.GRANITE),
			new Item.Properties().group(ZoesteriaItems.CREATIVE_TAB));

	public static final RegistryObject<ZoesteriaPlantBlock> OAK_LEAFCARPET = createWithBlockitem(
			"oak_leafcarpet",
			() -> new ZoesteriaPlantBlock(Block.Properties.from(Blocks.GRASS), 1.0D, Block.OffsetType.NONE, null),
			new Item.Properties().group(ZoesteriaItems.CREATIVE_TAB));

	public static final RegistryObject<ZoesteriaPlantBlock> SMALL_BUSH = createWithBlockitem(
			"small_bush",
			() -> new ZoesteriaPlantBlock(Block.Properties.from(Blocks.GRASS), 14.0D, Block.OffsetType.XZ, null),
			new Item.Properties().group(ZoesteriaItems.CREATIVE_TAB));

	public static final RegistryObject<ZoesteriaPlantBlock> SMALL_BERRY_BUSH = createWithBlockitem(
			"small_berry_bush",
			() -> new ZoesteriaPlantBlock(Block.Properties.from(Blocks.GRASS), 14.0D, Block.OffsetType.XZ, null),
			new Item.Properties().group(ZoesteriaItems.CREATIVE_TAB));

	public static final RegistryObject<ZoesteriaPlantBlock> SMALL_CACTLET = createWithBlockitem(
			"small_cactlet",
			() -> new ZoesteriaPlantBlock(Block.Properties.from(Blocks.GRASS), 9.0D, Block.OffsetType.XYZ, SAND_TYPE, PlantType.Desert),
			new Item.Properties().group(ZoesteriaItems.CREATIVE_TAB));

	public static final RegistryObject<ZoesteriaPlantBlock> CACTLET = createWithBlockitem(
			"cactlet",
			() -> new ZoesteriaPlantBlock(Block.Properties.from(Blocks.GRASS), 12.0D, Block.OffsetType.XZ, SAND_TYPE, PlantType.Desert),
			new Item.Properties().group(ZoesteriaItems.CREATIVE_TAB));

	public static final RegistryObject<ZoesteriaDoublePlantBlock> LARGE_CACTLET = createWithBlockitem(
			"large_cactlet",
			() -> new ZoesteriaDoublePlantBlock(Block.Properties.from(Blocks.GRASS), 28.0D, Block.OffsetType.XZ, SAND_TYPE),
			new Item.Properties().group(ZoesteriaItems.CREATIVE_TAB));

	public static final RegistryObject<ZoesteriaPlantBlock> PINGAO = createWithBlockitem(
			"pingao",
			() -> new ZoesteriaPlantBlock(Block.Properties.from(Blocks.GRASS), 8.0D, Block.OffsetType.XYZ, SAND_TYPE, PlantType.Desert),
			new Item.Properties().group(ZoesteriaItems.CREATIVE_TAB));

	public static final RegistryObject<ZoesteriaPlantBlock> TOADSTOOL = createWithBlockitem(
			"toadstool",
			() -> new ZoesteriaPlantBlock(Block.Properties.from(Blocks.BROWN_MUSHROOM), 8.0D, Block.OffsetType.NONE, null),
			new Item.Properties().group(ZoesteriaItems.CREATIVE_TAB));

	public static final RegistryObject<ZoesteriaPlantBlock> TOADSTOOLS = createWithBlockitem(
			"toadstools",
			() -> new ZoesteriaPlantBlock(Block.Properties.from(Blocks.BROWN_MUSHROOM), 12.0D, Block.OffsetType.NONE, null),
			new Item.Properties().group(ZoesteriaItems.CREATIVE_TAB));

	public static final RegistryObject<ZoesteriaPlantBlock> BLUE_FLAX_LILY = createWithBlockitem(
			"blue_flax_lily",
			() -> new ZoesteriaPlantBlock(Block.Properties.from(Blocks.GRASS), 15.0D, Block.OffsetType.XYZ, OUTBACK_SAND_TYPE, PlantType.Desert),
			new Item.Properties().group(ZoesteriaItems.CREATIVE_TAB));

	public static final RegistryObject<ZoesteriaPlantBlock> SANDHILL_CANEGRASS = createWithBlockitem(
			"sandhill_canegrass",
			() -> new ZoesteriaPlantBlock(Block.Properties.from(Blocks.GRASS), 15.0D, Block.OffsetType.XYZ, OUTBACK_SAND_TYPE, PlantType.Desert),
			new Item.Properties().group(ZoesteriaItems.CREATIVE_TAB));

	// SAPLINGS

	public static final RegistryObject<SaplingBlock> ASPEN_SAPLING = createWithBlockitem(
			"aspen_sapling",
			() -> new SaplingBlock(new AspenSaplingTree(), Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.0F).sound(SoundType.PLANT)),
			new Item.Properties().group(ZoesteriaItems.CREATIVE_TAB));

	public static final RegistryObject<SaplingBlock> BLUFF_PINE_SAPLING = createWithBlockitem(
			"bluff_pine_sapling",
			() -> new SaplingBlock(new BluffPineSaplingTree(), Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.0F).sound(SoundType.PLANT)),
			new Item.Properties().group(ZoesteriaItems.CREATIVE_TAB));

	private static <T extends Block> RegistryObject<T> createWithBlockitem(String id, Supplier<T> blockSupplier, Item.Properties itemProperties) {
		RegistryObject<T> result = BLOCKS.register(id, blockSupplier);
		ZoesteriaItems.ITEMS.register(id, result.lazyMap(block -> new BlockItem(block, itemProperties)));
		return result;
	}
}
