package tk.valoeghese.zoesteria.common.objects;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ZoesteriaBlocks {
	private ZoesteriaBlocks() {
	}

	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, "zoesteria");

	public static final SoundType OVERGROWN_STONE_SOUND = new SoundType(1.0F, 1.0F, SoundEvents.BLOCK_STONE_BREAK, SoundEvents.BLOCK_GRASS_STEP, SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);

	public static final RegistryObject<OvergrownStoneBlock> OVERGROWN_STONE = createWithBlockitem(
			"overgrown_stone",
			() -> new OvergrownStoneBlock(Block.Properties.create(Material.ROCK, MaterialColor.STONE).tickRandomly().hardnessAndResistance(1.5F, 6.0F).sound(OVERGROWN_STONE_SOUND)),
			new Item.Properties().group(ZoesteriaItems.CREATIVE_TAB));

	private static <T extends Block> RegistryObject<T> createWithBlockitem(String id, Supplier<T> blockSupplier, Item.Properties itemProperties) {
		RegistryObject<T> result = BLOCKS.register(id, blockSupplier);
		ZoesteriaItems.ITEMS.register(id, result.lazyMap(block -> new BlockItem(block, itemProperties)));
		return result;
	}
}
