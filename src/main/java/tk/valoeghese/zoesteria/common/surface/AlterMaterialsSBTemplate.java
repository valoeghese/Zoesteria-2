package tk.valoeghese.zoesteria.common.surface;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import tk.valoeghese.zoesteria.api.IZFGSerialisable;
import tk.valoeghese.zoesteria.api.surface.ISurfaceBuilderTemplate;
import tk.valoeghese.zoesteria.core.ZFGUtils;
import tk.valoeghese.zoesteriaconfig.api.ZoesteriaConfig;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

public class AlterMaterialsSBTemplate implements ISurfaceBuilderTemplate<AlterMaterialsSBTemplate.Step> {
	@Override
	public String id() {
		return "alter_materials";
	}

	@Override
	public SurfaceBuilder<SurfaceBuilderConfig> create(Container surfaceBuilderData) {
		return null;
	}

	private SBPredicate constructCondition(Container conditionData) {
		String type = conditionData.getStringValue("type");

		switch (type) {
		case "noise_within":
		{
			double min = conditionData.getDoubleValue("min");
			double max = conditionData.getDoubleValue("max");
			return (x, z, noise) -> min < noise && noise < max;
		}
		case "noise_outside":
		{
			double min = conditionData.getDoubleValue("min");
			double max = conditionData.getDoubleValue("max");
			return (x, z, noise) -> noise < min || noise > max;
		}
		case "noise_exceeds":
		{
			double val = conditionData.getDoubleValue("value");
			return (x, z, noise) -> noise > val;
		}
		case "noise_preceeds":
		{
			double val = conditionData.getDoubleValue("value");
			return (x, z, noise) -> noise < val;
		}
		case "z_exceeds":
		{
			int val = conditionData.getIntegerValue("value");
			return (x, z, noise) -> z > val;
		}
		case "z_preceeds":
		{
			int val = conditionData.getIntegerValue("value");
			return (x, z, noise) -> z < val;
		}
		case "x_exceeds":
		{
			int val = conditionData.getIntegerValue("value");
			return (x, z, noise) -> x > val;
		}
		case "x_preceeds":
		{
			int val = conditionData.getIntegerValue("value");
			return (x, z, noise) -> x < val;
		}
		default:
			throw new RuntimeException("Unknown condition type: " + type);
		}
	}

	public static class AlterMaterialsSB extends SurfaceBuilder<SurfaceBuilderConfig> {
		public AlterMaterialsSB(SBFunction function) {
			super(SurfaceBuilderConfig::deserialize);
			this.function = function;
		}

		private SBFunction function;

		@Override
		public void buildSurface(Random random, IChunk chunkIn, Biome biomeIn, int x, int z, int startHeight,
				double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed,
				SurfaceBuilderConfig config) {
			SurfaceBuilder.DEFAULT.buildSurface(
					random,
					chunkIn,
					biomeIn,
					x,
					z, 
					startHeight,
					noise,
					defaultBlock,
					defaultFluid,
					seaLevel,
					seed,
					this.function.alterMaterials(config, x, z, noise));
		}
	}

	public static class Step implements IZFGSerialisable {
		public Step(Condition condition, Optional<Block> top, Optional<Block> filler, Optional<Block> underwater, boolean terminate) {
			this.condition = condition;
			this.top = top;
			this.filler = filler;
			this.underwater = underwater;
			this.terminate = terminate;
			this.steps = null;
		}

		public Step(Condition condition, List<Step> steps, boolean terminate) {
			this.condition = condition;
			this.top = null;
			this.filler = null;
			this.underwater = null;
			this.terminate = terminate;
			this.steps = null;
		}

		private final Condition condition;
		private final Optional<Block> top;
		private final Optional<Block> filler;
		private final Optional<Block> underwater;
		private final List<Step> steps;
		private final boolean terminate;

		@Override
		public Container toZoesteriaConfig() {
			final EditableContainer result = ZoesteriaConfig.createWritableConfig(new LinkedHashMap<>());

			result.putMap("condition", this.condition.toZoesteriaConfig().asMap());

			if (this.steps == null) {
				// node
				this.top.ifPresent(val -> ZFGUtils.putBlock(result, "topBlock", val));
				this.filler.ifPresent(val -> ZFGUtils.putBlock(result, "fillerBlock", val));
				this.underwater.ifPresent(val -> ZFGUtils.putBlock(result, "underwaterBlock", val));
			} else {
				// branch
				List<Object> steps = new ArrayList<>();

				for (Step step : this.steps) {
					steps.add(step.toZoesteriaConfig().asMap());
				}

				result.putList("steps", steps);
			}

			result.putBooleanValue("terminate", this.terminate);

			return result; 
		}	
	}

	// a surface builder condition.
	public static class Condition implements IZFGSerialisable {
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
	}

	@FunctionalInterface
	interface SBFunction {
		SurfaceBuilderConfig alterMaterials(SurfaceBuilderConfig original, int x, int z, double noise);
	}

	@FunctionalInterface
	interface SBPredicate {
		boolean test(int x, int z, double noise);
	}
}
