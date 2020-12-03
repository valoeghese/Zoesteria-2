package tk.valoeghese.zoesteria.common.surface;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import tk.valoeghese.zoesteria.api.IZFGSerialisable;
import tk.valoeghese.zoesteria.api.ZFGUtils;
import tk.valoeghese.zoesteria.api.surface.Condition;
import tk.valoeghese.zoesteria.api.surface.ISurfaceBuilderTemplate;
import tk.valoeghese.zoesteria.api.surface.Condition.SBPredicate;
import tk.valoeghese.zoesteriaconfig.api.ZoesteriaConfig;
import tk.valoeghese.zoesteriaconfig.api.container.Container;
import tk.valoeghese.zoesteriaconfig.api.container.EditableContainer;

/**
 * You may notice there is a lot of weird code in this class.
 * This is for optimisation purposes.
 */
public class AlterBlocksTemplate implements ISurfaceBuilderTemplate<AlterBlocksTemplate.Step> {
	@Override
	public String id() {
		return "alter_blocks";
	}

	@Override
	public SurfaceBuilder<SurfaceBuilderConfig> create(Container surfaceBuilderData) {
		List<SBStep> steps = constructStepBranch(surfaceBuilderData.getList("steps"));

		return new AlterBlocksSB((original, rand, x, z, noise) -> {
			AtomicReference<Block> top = new AtomicReference<>(original.getTop().getBlock());
			AtomicReference<Block> filler = new AtomicReference<>(original.getUnder().getBlock());
			AtomicReference<Block> underwater = new AtomicReference<>(original.getUnderWaterMaterial().getBlock());

			for (SBStep step : steps) {
				if (step.alterBlocks(top, filler, underwater, rand, x, z, noise)) {
					break;
				}
			}

			return new SurfaceBuilderConfig(top.get().getDefaultState(), filler.get().getDefaultState(), underwater.get().getDefaultState());
		});
	}

	private SBStep constructStep(Container stepData) {
		SBPredicate condition = Condition.deserialise(stepData.getContainer("condition"));
		boolean terminate = stepData.getBooleanValue("terminate");
		List<Object> steps = stepData.getList("steps"); // microoptimisation - skip containsKey

		if (steps == null) {
			int type = stepData.containsKey("topBlock") ? 1 : 0;
			type = (type << 1) | (stepData.containsKey("fillerBlock") ? 1 : 0);
			type = (type << 1) | (stepData.containsKey("underwaterBlock") ? 1 : 0);
			return constructStepNodule(stepData, condition, type, terminate);
		} else {
			// branch
			List<SBStep> constructedSteps = constructStepBranch(steps);

			return (top, filler, underwater, rand, x, z, noise) -> {
				if (condition.test(rand, x, z, noise)) {
					for (SBStep step : constructedSteps) {
						if (step.alterBlocks(top, filler, underwater, rand, x, z, noise)) {
							return terminate;
						}
					}
				}

				return false;
			};
		}
	}

	@SuppressWarnings("unchecked")
	private List<SBStep> constructStepBranch(List<Object> steps) {
		List<SBStep> constructedSteps = new ArrayList<>();

		for (Object o : steps) {
			constructedSteps.add(
					constructStep(
							ZoesteriaConfig.createWritableConfig((Map<String, Object>) o)
							)
					);
		}

		return constructedSteps;
	}

	private SBStep constructStepNodule(Container stepData, SBPredicate condition, int type, boolean terminate) {
		// Microoptimisation go brr

		switch (type) {
		case 0:
			return (top, filler, underwater, rand, x, z, noise) -> {
				return terminate && condition.test(rand, x, z, noise);
			};
		case 1: //uw
		{
			Block underwaterBlock = ZFGUtils.getBlock(stepData, "underwaterBlock");

			return (top, filler, underwater, rand, x, z, noise) -> {
				if (condition.test(rand, x, z, noise)) {
					underwater.set(underwaterBlock);
					return terminate;
				}

				return false;
			};
		}
		case 2: // f
		{
			Block fillerBlock = ZFGUtils.getBlock(stepData, "fillerBlock");

			return (top, filler, underwater, rand, x, z, noise) -> {
				if (condition.test(rand, x, z, noise)) {
					filler.set(fillerBlock);
					return terminate;
				}

				return false;
			};
		}
		case 3: // f uw
		{
			Block underwaterBlock = ZFGUtils.getBlock(stepData, "underwaterBlock");
			Block fillerBlock = ZFGUtils.getBlock(stepData, "fillerBlock");

			return (top, filler, underwater, rand, x, z, noise) -> {
				if (condition.test(rand, x, z, noise)) {
					underwater.set(underwaterBlock);
					filler.set(fillerBlock);
					return terminate;
				}

				return false;
			};
		}
		case 4: // t
		{
			Block topBlock = ZFGUtils.getBlock(stepData, "topBlock");
			return (top, filler, underwater, rand, x, z, noise) -> {
				if (condition.test(rand, x, z, noise)) {
					top.set(topBlock);
					return terminate;
				}

				return false;
			};
		}
		case 5: // t uw
		{
			Block underwaterBlock = ZFGUtils.getBlock(stepData, "underwaterBlock");
			Block topBlock = ZFGUtils.getBlock(stepData, "topBlock");

			return (top, filler, underwater, rand, x, z, noise) -> {
				if (condition.test(rand, x, z, noise)) {
					underwater.set(underwaterBlock);
					top.set(topBlock);
					return terminate;
				}

				return false;
			};
		}
		case 6: // t f
		{
			Block fillerBlock = ZFGUtils.getBlock(stepData, "fillerBlock");
			Block topBlock = ZFGUtils.getBlock(stepData, "topBlock");

			return (top, filler, underwater, rand, x, z, noise) -> {
				if (condition.test(rand, x, z, noise)) {
					filler.set(fillerBlock);
					top.set(topBlock);
					return terminate;
				}

				return false;
			};
		}
		case 7: // t f uw
		{
			Block underwaterBlock = ZFGUtils.getBlock(stepData, "underwaterBlock");
			Block fillerBlock = ZFGUtils.getBlock(stepData, "fillerBlock");
			Block topBlock = ZFGUtils.getBlock(stepData, "topBlock");

			return (top, filler, underwater, rand, x, z, noise) -> {
				if (condition.test(rand, x, z, noise)) {
					underwater.set(underwaterBlock);
					filler.set(fillerBlock);
					top.set(topBlock);
					return terminate;
				}

				return false;
			};
		}
		default:
			throw new RuntimeException("what?\thow?\t(step nodule type generated an impossible output: " + type + ")");
		}
	}

	public static class AlterBlocksSB extends SurfaceBuilder<SurfaceBuilderConfig> {
		public AlterBlocksSB(SBFunction function) {
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
					this.function.alterMaterials(config, random, x, z, noise));
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
			this.steps = steps;
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

	@FunctionalInterface
	interface SBFunction {
		SurfaceBuilderConfig alterMaterials(SurfaceBuilderConfig original, Random rand, int x, int z, double noise);
	}

	@FunctionalInterface
	interface SBStep {
		boolean alterBlocks(AtomicReference<Block> top, AtomicReference<Block> filler, AtomicReference<Block> underwater, Random rand, int x, int z, double noise);
	}
}
