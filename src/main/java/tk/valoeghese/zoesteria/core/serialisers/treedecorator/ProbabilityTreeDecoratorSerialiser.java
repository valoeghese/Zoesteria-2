package tk.valoeghese.zoesteria.core.serialisers.treedecorator;

import it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import it.unimi.dsi.fastutil.objects.Object2FloatFunction;
import net.minecraft.world.gen.treedecorator.BeehiveTreeDecorator;
import net.minecraft.world.gen.treedecorator.CocoaTreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import tk.valoeghese.zoesteria.api.feature.ITreeDecoratorSerialiser;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class ProbabilityTreeDecoratorSerialiser<T extends TreeDecorator> implements ITreeDecoratorSerialiser<T> {
	protected ProbabilityTreeDecoratorSerialiser(float probability, Float2ObjectFunction<T> constructor, Object2FloatFunction<T> probabilityGetter) {
		this.constructor = constructor;
		this.pg = probabilityGetter;
		this.probability = probability;
	}

	private final Float2ObjectFunction<T> constructor;
	private final Object2FloatFunction<T> pg;
	private final float probability;

	@Override
	public ITreeDecoratorSerialiser<T> loadFrom(T decorator) {
		return new ProbabilityTreeDecoratorSerialiser<>(this.pg.getFloat(decorator), this.constructor, this.pg);
	}

	@Override
	public ITreeDecoratorSerialiser<T> deserialise(Container settings) {
		return new ProbabilityTreeDecoratorSerialiser<>(settings.getFloatValue("probability"), this.constructor, this.pg);
	}

	@Override
	public void serialise(EditableContainer settings) {
		settings.putFloatValue("probability", this.probability);
	}

	@Override
	public T create() {
		return this.constructor.get(this.probability);
	}

	public static final ProbabilityTreeDecoratorSerialiser<BeehiveTreeDecorator> BEEHIVE = new ProbabilityTreeDecoratorSerialiser<>(0.0f, BeehiveTreeDecorator::new, bh -> ((BeehiveTreeDecorator)bh).probability);
	public static final ProbabilityTreeDecoratorSerialiser<CocoaTreeDecorator> COCOA = new ProbabilityTreeDecoratorSerialiser<>(0.0f, CocoaTreeDecorator::new, co -> ((CocoaTreeDecorator)co).field_227417_b_);
}