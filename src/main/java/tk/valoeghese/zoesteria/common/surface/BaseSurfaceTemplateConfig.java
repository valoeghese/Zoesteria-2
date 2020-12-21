package tk.valoeghese.zoesteria.common.surface;

import java.util.LinkedHashMap;

import tk.valoeghese.zoesteria.api.surface.ISurfaceTemplateConfig;
import tk.valoeghese.zoesteriaconfig.api.ZoesteriaConfig;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class BaseSurfaceTemplateConfig implements ISurfaceTemplateConfig {
	public BaseSurfaceTemplateConfig(String base) {
		this.base = base;
	}

	public final String base;

	@Override
	public Container toZoesteriaConfig() {
		EditableContainer result = ZoesteriaConfig.createWritableConfig(new LinkedHashMap<>());
		result.putStringValue("baseSurfaceBuilder", this.base);
		return result;
	}

	public static final BaseSurfaceTemplateConfig deserialise(Container data) {
		return new BaseSurfaceTemplateConfig(data.getStringValue("baseSurfaceBuilder"));
	}
}
