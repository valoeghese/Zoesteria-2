package tk.valoeghese.zoesteria.common.objects;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ZoesteriaItems {
	public static ItemGroup creativeTab = new ItemGroup("zoesteria") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(Items.SPRUCE_SAPLING);
		}
	};
}
