package tk.valoeghese.zoesteria.api.surface;

import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import tk.valoeghese.zoesteria.api.IZFGSerialisable;
import tk.valoeghese.zoesteriaconfig.api.container.Container;

public interface ISurfaceBuilderTemplate<T extends IZFGSerialisable, R extends ISurfaceTemplateConfig> {
	String id();
	SurfaceBuilder<SurfaceBuilderConfig> create(Container surfaceBuilderData);
}
