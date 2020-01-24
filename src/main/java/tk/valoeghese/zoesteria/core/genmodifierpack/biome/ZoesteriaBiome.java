package tk.valoeghese.zoesteria.core.genmodifierpack.biome;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.registries.ForgeRegistries;

class ZoesteriaBiome extends Biome {
	ZoesteriaBiome(String packId, String id, Builder properties, BiomeFactory.Details biomeDetails) {
		super(properties);

		if (id.contains(":")) {
			this.setRegistryName(id);
		} else {
			this.setRegistryName(packId, id);
		}

		if (biomeDetails.skyColour != null) {
			this.customSkyColour = true;
			this.skyColour = biomeDetails.skyColour.intValue();
		} else {
			this.customSkyColour = false;
		}

		ForgeRegistries.BIOMES.register(this);
		
		biomeDetails.placement.forEach((type, weight) -> {
			BiomeManager.addBiome(type, new BiomeManager.BiomeEntry(this, weight));
		});
	}

	private final boolean customSkyColour;
	private int skyColour;

	@Override
	@OnlyIn(Dist.CLIENT)
	public int func_225529_c_() {
		return this.customSkyColour ? this.skyColour : super.func_225529_c_();
	}
}
