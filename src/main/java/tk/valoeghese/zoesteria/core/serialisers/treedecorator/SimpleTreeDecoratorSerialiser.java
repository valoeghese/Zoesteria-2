package tk.valoeghese.zoesteria.core.serialisers.treedecorator;

import java.util.function.Supplier;

import net.minecraft.world.gen.treedecorator.LeaveVineTreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TrunkVineTreeDecorator;
import tk.valoeghese.zoesteria.api.feature.ITreeDecoratorSerialiser;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class SimpleTreeDecoratorSerialiser<T extends TreeDecorator> implements ITreeDecoratorSerialiser<T> {
	protected SimpleTreeDecoratorSerialiser(Supplier<T> constructor) {
		this.constructor = constructor;
	}

	private final Supplier<T> constructor;

	@Override
	public ITreeDecoratorSerialiser<T> loadFrom(T decorator) {
		return this;
	}

	@Override
	public ITreeDecoratorSerialiser<T> deserialise(Container settings) {
		return this;
	}

	@Override
	public void serialise(EditableContainer settings) {
	}

	@Override
	public T create() {
		return this.constructor.get();
	}

	public static final SimpleTreeDecoratorSerialiser<TrunkVineTreeDecorator> TRUNK_VINE = new SimpleTreeDecoratorSerialiser<>(TrunkVineTreeDecorator::new);
	public static final SimpleTreeDecoratorSerialiser<LeaveVineTreeDecorator> LEAVES_VINE = new SimpleTreeDecoratorSerialiser<>(LeaveVineTreeDecorator::new);
}