package tk.valoeghese.zoesteria.common.objects;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ZoesteriaItems {
	private ZoesteriaItems() {
	}

	public static final ItemGroup CREATIVE_TAB = new ItemGroup("zoesteria") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ZoesteriaBlocks.SPINIFEX_LARGE.get().asItem());
		}
	};
	
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, "zoesteria");
}
