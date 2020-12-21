package tk.valoeghese.zoesteria.api.surface;

import java.util.List;

import tk.valoeghese.zoesteria.api.IZFGSerialisable;

public final class ZoesteriaSurfaceBuilder<R extends IZFGSerialisable, S extends ISurfaceTemplateConfig, T extends ISurfaceBuilderTemplate<R, S>> {
	private ZoesteriaSurfaceBuilder(String id, T template, S config, List<R> steps) {
		this.id = id;
		this.template = template;
		this.steps = steps;
		this.config = config;
	}

	public final String id;
	public final T template;
	public final List<R> steps;
	public final S config;

	public static <R extends IZFGSerialisable, S extends ISurfaceTemplateConfig, T extends ISurfaceBuilderTemplate<R, S>> ZoesteriaSurfaceBuilder<R, S, T> create(String id, T template, S config, List<R> steps) {
		return new ZoesteriaSurfaceBuilder<>(id, template, config, steps);
	}
}
