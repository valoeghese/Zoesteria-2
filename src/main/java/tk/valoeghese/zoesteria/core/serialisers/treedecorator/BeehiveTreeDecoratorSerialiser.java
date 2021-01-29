package tk.valoeghese.zoesteria.core.serialisers.treedecorator;

import net.minecraft.world.gen.treedecorator.BeehiveTreeDecorator;
import tk.valoeghese.zoesteria.api.feature.ITreeDecoratorSerialiser;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class BeehiveTreeDecoratorSerialiser implements ITreeDecoratorSerialiser<BeehiveTreeDecorator> {
	private BeehiveTreeDecoratorSerialiser(float probability) {
		this.probability = probability;
	}

	private final float probability;

	@Override
	public ITreeDecoratorSerialiser<BeehiveTreeDecorator> loadFrom(BeehiveTreeDecorator decorator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITreeDecoratorSerialiser<BeehiveTreeDecorator> deserialise(Container settings) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void serialise(EditableContainer settings) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BeehiveTreeDecorator create() {
		// TODO Auto-generated method stub
		return null;
	}

}
