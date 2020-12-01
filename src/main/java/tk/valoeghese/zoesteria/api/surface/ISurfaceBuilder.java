package tk.valoeghese.zoesteria.api.surface;

import java.util.List;

import tk.valoeghese.zoesteria.api.IZFGSerialisable;

public interface ISurfaceBuilder<R extends IZFGSerialisable, T extends ISurfaceBuilderTemplate<R>> {
	String id();
	T template();
	List<R> modifiers();
}
