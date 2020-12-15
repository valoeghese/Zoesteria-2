package tk.valoeghese.zoesteria.common.objects;

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
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tk.valoeghese.zoesteria.common.feature.sapling.AspenSaplingTree;
import tk.valoeghese.zoesteria.common.feature.sapling.BluffPineSaplingTree;

public final class ZoesteriaBlocks {
	private ZoesteriaBlocks() {
	}

	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, "zoesteria");
	public static final SoundType OVERGROWN_STONE_SOUND = new SoundType(1.0F, 1.0F, SoundEvents.BLOCK_STONE_BREAK, SoundEvents.BLOCK_GRASS_STEP, SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);

	// SOILS / GROUND

	public static final RegistryObject<OvergrownStoneBlock> OVERGROWN_STONE = createWithBlockitem(
			"overgrown_stone",
			() -> new OvergrownStoneBlock(Block.Properties.create(Material.ROCK, MaterialColor.STONE).tickRandomly().hardnessAndResistance(1.5F, 6.0F).sound(OVERGROWN_STONE_SOUND).harvestTool(ToolType.PICKAXE).harvestLevel(0)),
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
	public static final RegistryObject<ZoesteriaPlantBlock> SPINIFEX_SMALL = createWithBlockitem(
			"spinifex_small",
			() -> new ZoesteriaPlantBlock(Block.Properties.from(Blocks.GRASS), 5.4D, Block.OffsetType.XYZ, b -> b == Blocks.SAND || b == Blocks.RED_SAND || b == Blocks.COARSE_DIRT),
			new Item.Properties().group(ZoesteriaItems.CREATIVE_TAB));

	public static final RegistryObject<ZoesteriaPlantBlock> SPINIFEX_LARGE = createWithBlockitem(
			"spinifex_large",
			() -> new ZoesteriaPlantBlock(Block.Properties.from(Blocks.GRASS), 8.0D, Block.OffsetType.XYZ, b -> b == Blocks.SAND || b == Blocks.RED_SAND || b == Blocks.COARSE_DIRT),
			new Item.Properties().group(ZoesteriaItems.CREATIVE_TAB));

	public static final RegistryObject<ZoesteriaPlantBlock> SHORE_BINDWEED = createWithBlockitem(
			"shore_bindweed",
			() -> new ZoesteriaPlantBlock(Block.Properties.from(Blocks.GRASS), 1.0D, Block.OffsetType.XZ, b -> b == Blocks.GRAVEL || b == Blocks.STONE || b == Blocks.ANDESITE || b == Blocks.DIORITE || b == Blocks.GRANITE),
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
