package tk.valoeghese.zoesteria.api.module;

import java.util.LinkedHashMap;
import java.util.Map;

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

	public Map<String, Object> asMap() {
		Map<String, Object> result = new LinkedHashMap<>();
		result.put("schemaVersion", "0");
		result.put("id", this.packId);
		result.put("enabled", "true");
		return result;
	}
}
