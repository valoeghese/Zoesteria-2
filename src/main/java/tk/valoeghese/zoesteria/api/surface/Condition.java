package tk.valoeghese.zoesteria.api.surface;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import tk.valoeghese.zoesteria.api.IZFGSerialisable;
import tk.valoeghese.zoesteria.common.surface.AlterBlocksTemplate;
import tk.valoeghese.zoesteriaconfig.api.ZoesteriaConfig;
import tk.valoeghese.zoesteriaconfig.api.container.Container;

/**
 * A surface builder condition. Used by {@link AlterBlocksTemplate}.
 */
public class Condition implements IZFGSerialisable {
	public Condition(String conditionName) {
		this.conditionParams.put("type", conditionName);
	}

	private final Map<String, Object> conditionParams = new LinkedHashMap<>();

	public Condition withParameter(String parameter, Object value) {
		if (parameter.equals("type")) {
			throw new RuntimeException("type is not a valid parameter: it is a reserved key!");
		}

		this.conditionParams.put(parameter, value);
		return this;
	}

	@Override
	public Container toZoesteriaConfig() {
		return ZoesteriaConfig.createWritableConfig(this.conditionParams);
	}	

	/**
	 * Note that the seemingly weird and unnecessary code here is for optimisation purposes in order to make the game run more smoothly.
	 * @param conditionData the serialised condition data.
	 * @return a predicate which tests based on the settings provided as condition data.
	 */
	public static SBPredicate deserialise(Container conditionData) {
		String type = conditionData.getStringValue("type");

		switch (type) {
		case "noise_within":
		{
			double min = conditionData.getDoubleValue("min");
			double max = conditionData.getDoubleValue("max");
			return (rand, x, z, noise) -> noise > min && noise < max;
		}
		case "noise_outside":
		{
			double min = conditionData.getDoubleValue("min");
			double max = conditionData.getDoubleValue("max");
			return (rand, x, z, noise) -> noise < min || noise > max;
		}
		case "noise_exceeds":
		{
			double val = conditionData.getDoubleValue("value");
			return (rand, x, z, noise) -> noise > val;
		}
		case "noise_preceeds":
		{
			double val = conditionData.getDoubleValue("value");
			return (rand, x, z, noise) -> noise < val;
		}
		case "z_exceeds":
		{
			int val = conditionData.getIntegerValue("value");
			return (rand, x, z, noise) -> z > val;
		}
		case "z_preceeds":
		{
			int val = conditionData.getIntegerValue("value");
			return (rand, x, z, noise) -> z < val;
		}
		case "x_exceeds":
		{
			int val = conditionData.getIntegerValue("value");
			return (rand, x, z, noise) -> x > val;
		}
		case "x_preceeds":
		{
			int val = conditionData.getIntegerValue("value");
			return (rand, x, z, noise) -> x < val;
		}
		case "chance":
		{
			int val = conditionData.getIntegerValue("value");
			return (rand, x, z, noise) -> rand.nextInt(val) == 0;
		}
		case "chance_double":
		{
			double val = conditionData.getDoubleValue("value");
			return (rand, x, z, noise) -> rand.nextDouble() < val;
		}
		default:
			throw new RuntimeException("Unknown condition type: " + type);
		}
	}

	@FunctionalInterface
	public interface SBPredicate {
		boolean test(Random rand, int x, int z, double noise);
	}
}
