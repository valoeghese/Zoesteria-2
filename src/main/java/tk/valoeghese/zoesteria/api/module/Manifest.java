package tk.valoeghese.zoesteria.api.module;

import java.util.Map;

import com.google.common.collect.Maps;

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
		Map<String, Object> result = Maps.newLinkedHashMap();
		result.put("schemaVersion", "0");
		result.put("id", this.packId);
		result.put("enabled", "true");
		return result;
	}
}
