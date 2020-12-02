package tk.valoeghese.zoesteria.core.genmodifierpack.biome;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

class ZoesteriaBiome extends Biome {
	ZoesteriaBiome(String packId, String id, Builder properties, BiomeFactory.Details biomeDetails, IForgeRegistry<Biome> biomeRegistry) {
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

		biomeRegistry.register(this);

		biomeDetails.placement.forEach((type, weight) -> {
			BiomeManager.addBiome(type, new BiomeManager.BiomeEntry(this, weight));
		});

		if (biomeDetails.spawnBiome) {
			BiomeManager.addSpawnBiome(this);
		}
	}

	private final boolean customSkyColour;
	private int skyColour;

	private final boolean customRiver;
	private ResourceLocation riverId;
	private Biome river = null;

	// in case of overriding
	// haha 1.16 will destroy this when I port
	private boolean customSurface = false;
	private ConfiguredSurfaceBuilder<SurfaceBuilderConfig> overridden;

	@Override
	@OnlyIn(Dist.CLIENT)
	public int getSkyColor() {
		return this.customSkyColour ? this.skyColour : super.getSkyColor();
	}

	@Override
	public Biome getRiver() {
		return this.customRiver ? this.river() : super.getRiver();
	}

	@Override
	public ConfiguredSurfaceBuilder<?> getSurfaceBuilder() {
		return this.customSurface ? this.overridden : super.getSurfaceBuilder();
	}

	// TODO 1.16-friendly workaround instead. Perhaps only when porting to 1.16
	public void setSurfaceBuilder(SurfaceBuilder<SurfaceBuilderConfig> sb) {
		if (sb == null) {
			throw new NullPointerException("Attempt to set the surface builder of a ZoesteriaBiome to null!");
		}

		ISurfaceBuilderConfig config = this.getSurfaceBuilderConfig();

		if (config instanceof SurfaceBuilderConfig) {
			this.customSurface = true;
			this.overridden = new ConfiguredSurfaceBuilder<>(sb, (SurfaceBuilderConfig) config);
		} else {
			throw new RuntimeException("ISurfaceBuilderConfig used with a custom Zoesteria surface builder is not a SurfaceBuilderConfig!");
		}
	}

	public Biome river() {
		if (this.river != null) {
			return this.river;
		} else {
			return (this.river = ForgeRegistries.BIOMES.getValue(this.riverId));
		}
	}
}
