package tk.valoeghese.zoesteria.api.surface;

import java.util.List;

import tk.valoeghese.zoesteria.api.IZFGSerialisable;

public final class ZoesteriaSurfaceBuilder<R extends IZFGSerialisable, T extends ISurfaceBuilderTemplate<R>> {
	private ZoesteriaSurfaceBuilder(String id, T template, List<R> steps) {
		this.id = id;
		this.template = template;
		this.steps = steps;
	}

	public final String id;
	public final T template;
	public final List<R> steps;

	public static <R extends IZFGSerialisable, T extends ISurfaceBuilderTemplate<R>> ZoesteriaSurfaceBuilder<R, T> create(String id, T template, List<R> steps) {
		return new ZoesteriaSurfaceBuilder<>(id, template, steps);
	}
}
