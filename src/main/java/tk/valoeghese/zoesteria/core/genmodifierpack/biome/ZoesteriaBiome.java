package tk.valoeghese.zoesteria.core.genmodifierpack.biome;

import net.minecraft.util.ResourceLocation;
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

		if (biomeDetails.river != null) {
			this.customRiver = true;
			this.riverId = new ResourceLocation(biomeDetails.river);
		} else {
			this.customRiver = false;
		}

		ForgeRegistries.BIOMES.register(this);

		biomeDetails.placement.forEach((type, weight) -> {
			BiomeManager.addBiome(type, new BiomeManager.BiomeEntry(this, weight));
		});
	}

	private final boolean customSkyColour;
	private int skyColour;

	private final boolean customRiver;
	private ResourceLocation riverId;
	private Biome river = null;

	@Override
	@OnlyIn(Dist.CLIENT)
	public int func_225529_c_() {
		return this.customSkyColour ? this.skyColour : super.func_225529_c_();
	}

	@Override
	public Biome getRiver() {
		return this.customRiver ? this.river() : super.getRiver();
	}

	public Biome river() {
		if (this.river != null) {
			return this.river;
		} else {
			return (this.river = ForgeRegistries.BIOMES.getValue(this.riverId));
		}
	}
}
