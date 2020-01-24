package tk.valoeghese.zoesteria.api.module;

public class Manifest {
	private Manifest(String packId) {
		this.packId = packId;
	}

	private final String packId;

	public String getPackId() {
		return this.packId;
	}

	public static Manifest createSchema0(IZoesteriaJavaModule module) {
		return new Manifest(module.packId());
	}
}
