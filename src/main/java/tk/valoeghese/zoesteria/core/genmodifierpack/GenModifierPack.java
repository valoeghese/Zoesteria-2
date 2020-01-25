package tk.valoeghese.zoesteria.core.genmodifierpack;

import java.io.File;
import java.util.Map;
import java.util.function.Consumer;

import com.google.common.collect.Maps;

import tk.valoeghese.common.util.FileUtils;
import tk.valoeghese.zoesteria.api.module.IZoesteriaJavaModule;
import tk.valoeghese.zoesteria.core.genmodifierpack.biome.BiomeFactory;
import tk.valoeghese.zoesteriaconfig.api.ZoesteriaConfig;
import tk.valoeghese.zoesteriaconfig.api.container.Container;

public final class GenModifierPack {
	private GenModifierPack(String id, String packDir, boolean enabled) {
		this.id = id;
		this.packDir = packDir;
		this.disabled = !enabled;
	}

	private final String id;
	private final String packDir;
	private final boolean disabled;

	public void loadBiomes() {
		if (this.disabled) {
			return;
		}

		File biomesDir = new File(this.packDir + "./biomes/");

		if (!biomesDir.isDirectory()) {
			return;
		}

		FileUtils.trailFilesOfExtension(biomesDir, "cfg", (file, trail) -> {
			BiomeFactory.buildBiome(file, this.id);
		});
	}

	public String getId() {
		return this.id;
	}

	public boolean isDisabled() {
		return this.disabled;
	}

	public static void add(String packDir) {
		File manifest = new File(packDir + "/manifest.cfg");

		if (!manifest.isFile()) {
			return;
		}

		Container packManifest = ZoesteriaConfig.loadConfig(manifest);
		int version = packManifest.getIntegerValue("schemaVersion").intValue();

		switch (version) {
		case 0:
			String id = packManifest.getStringValue("id");

			if (isLoaded(id)) {
				System.out.println("GenModifierPack with id \"" + id + "\" already loaded.");
				return;
			}

			Boolean enabled = Utils.getBoolean(packManifest, "enabled", true);

			System.out.println("Zoesteria has detected module: " + id);
			PACKS.put(id, new GenModifierPack(id, packDir, enabled.booleanValue()));
			break;
		default:
			throw new RuntimeException("Unknown pack schemaVersion detected!");
		}
	}

	public static void addIfAbsent(IZoesteriaJavaModule module) {
		String packId = module.packId();

		if (!isLoaded(packId)) {
			// create module dir
			String packDir = "./zoesteria/" + packId;
			File packDirFile = new File(packDir);
			packDirFile.mkdirs();

			// create manifest
			ConfigWriter manifest = new ConfigWriter(module.createManifest().asMap());
			manifest.writeToFile(new File(packDir + "/manifest.cfg"));

			// create biome files
		}
	}

	public static void forEach(Consumer<GenModifierPack> callback) {
		PACKS.forEach((id, pack) -> callback.accept(pack));
	}

	public static void init() {
		if (!initialised) {
			if (ROOT_DIR.isDirectory()) {
				FileUtils.forEachDirectory(ROOT_DIR, dir -> {
					GenModifierPack.add(dir.getPath());
				});
			}

			initialised = true;
		}
	}

	public static boolean isLoaded(String packId) {
		return PACKS.containsKey(packId);
	}

	private static boolean initialised = false;

	private static final Map<String, GenModifierPack> PACKS = Maps.newHashMap();
	public static final File ROOT_DIR = new File("./zoesteria");
}
