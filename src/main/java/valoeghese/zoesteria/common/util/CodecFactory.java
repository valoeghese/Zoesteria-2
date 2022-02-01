package valoeghese.zoesteria.common.util;

import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import org.checkerframework.checker.units.qual.A;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.Function;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.ARETURN;

/**
 * A factory to automatically generate codecs. If you have cursedcodeophobia I advise you to look away immediately in order to not develop PTSD.
 */
public final class CodecFactory {
	private CodecFactory() {} // NO-OP

	static final ConcerningClassLoader LOADER = new ConcerningClassLoader();
	static final String CODEC_LOCATION = "com/mojang/serialization/Codec";

	static String descr(String loc) {
		return "L" + loc + ";";
	}

	private static AdaptableDescriptor getTypeDescriptor(Class<?> clazz) {
		// int types (excl z)
		if (int.class.isAssignableFrom(clazz)) return AdaptableDescriptor.INT;
		else if (byte.class.isAssignableFrom(clazz)) return AdaptableDescriptor.BYTE;
		else if (short.class.isAssignableFrom(clazz)) return AdaptableDescriptor.SHORT;
		else if (char.class.isAssignableFrom(clazz)) return AdaptableDescriptor.CHAR;

		// z, j
		else if (boolean.class.isAssignableFrom(clazz)) return AdaptableDescriptor.BOOLEAN;
		else if (long.class.isAssignableFrom(clazz)) return AdaptableDescriptor.LONG;
		// f, d
		else if (float.class.isAssignableFrom(clazz)) return AdaptableDescriptor.FLOAT;
		else if (double.class.isAssignableFrom(clazz)) return AdaptableDescriptor.DOUBLE;
		// class
		else return new AdaptableDescriptor(clazz);
	}

	private static String camelToSnake(String camel) {
		StringBuilder result = new StringBuilder();

		for (char c : camel.toCharArray()) {
			if (Character.isUpperCase(c)) {
				result.append('_').append(Character.toLowerCase(c));
			} else {
				result.append(c);
			}
		}

		return result.toString();
	}

	public static <FC extends FeatureConfiguration> Codec<FC> create(Class<FC> clazz) {
		final String genClassName = "valoeghese/zoesteria/common/feature/Codec" + clazz.getSimpleName();
		final String configClassName = clazz.getName().replace('.', '/');

		try {
			CodecField[] fields = Arrays.stream(clazz.getFields())
					.filter(field -> !Modifier.isStatic(field.getModifiers()))
					.map(field -> new CodecField(field.getName(), camelToSnake(field.getName()), getTypeDescriptor(field.getType())))
					.toArray(CodecField[]::new);

			ClassNode classBuilder = new ClassNode(ASM9);

			classBuilder.visit(
					V17,
					ACC_PUBLIC,
					genClassName,
					null,
					"java/lang/Object",
					new String[]{"valoeghese/zoesteria/common/util/CodecAssistant"}
			);

			// create constructor
			createConstructor(classBuilder);

			// create getters
			for (CodecField f : fields) {
				AdaptableDescriptor typeDescr = f.descriptor();

				MethodVisitor getter = classBuilder.visitMethod(
						ACC_PUBLIC | ACC_FINAL,
						"get_" + f.snakeName(),
						"()" + typeDescr.toString(),
						null,
						null
				);
				getter.visitCode();
				// get the field
				getter.visitFieldInsn(
						GETFIELD,
						configClassName,
						f.name(),
						typeDescr.toString()
				);
				// return it
				typeDescr.visitReturn(getter);
			}

			// create the apply method

			MethodVisitor apply = classBuilder.visitMethod(
					ACC_PUBLIC,
					"apply",
					"(Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance)Lcom/mojang/datafixers/kinds/App;",
					null,
					null
			);

			apply.visitCode();

			// add fields
			for (CodecField f : fields) {
				Class<?> type = f.descriptor().clazz;

				if (int.class.equals(type)) { // case int
					// get int codec on the stack
					apply.visitFieldInsn(
							GETSTATIC,
							CODEC_LOCATION,
							"INT",
							"Lcom/mojang/serialization/codecs/PrimitiveCodec;"
					);
					// parameters for optionalFieldOf
					apply.visitLdcInsn(f.snakeName()); // codec field name
					apply.visitInsn(ICONST_0); // default
					// create optional field (default 0)
					apply.visitMethodInsn(
							INVOKEINTERFACE,
							CODEC_LOCATION,
							"optionalFieldOf",
							"(Ljava/lang/String)",
							true
					);
					// parameters for forGetter

				}
			}

			// todo group fields

			// todo apply and build the codec app

			apply.visitInsn(ARETURN);

			classBuilder.visitEnd();

			ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
			classBuilder.accept(writer);

			CodecBuilder codecBuilder = (CodecBuilder) LOADER.defineClass(genClassName.replace('/', '.'), writer.toByteArray()).getConstructors()[0].newInstance();
			return RecordCodecBuilder.create(new CodecBuilderFunction<>(codecBuilder));
		} catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException("Exception while creating automatic codec!", e);
		}
	}

	private static void createConstructor(ClassNode classBuilder) {
		MethodVisitor constr = classBuilder.visitMethod(
				ACC_PUBLIC,
				"<init>",
				"()V",
				null,
				null
		);

		constr.visitCode();
		constr.visitVarInsn(ALOAD, 0); // this
		constr.visitMethodInsn( // superconstructor
				INVOKESPECIAL,
				"java/lang/Object",
				"<init>",
				"()V",
				false);
		constr.visitInsn(RETURN);
	}

	private static String getterFn(CodecField field, String fromClazz, String clazzSimpleName) throws InvocationTargetException, InstantiationException, IllegalAccessException {
		ClassNode classBuilder = new ClassNode(ASM9);
		String genClassName = "valoeghese/zoesteria/common/util/Getter" + clazzSimpleName + field.name();

		classBuilder.visit(
				V17,
				ACC_PUBLIC,
				genClassName,
				null,
				"java/lang/Object",
				new String[]{"java/util/function/Function"}
		);

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		classBuilder.accept(writer);

		createConstructor(classBuilder);

		// TODO make getter

		LOADER.defineClass(genClassName.replace('/', '.'), writer.toByteArray()).getConstructors()[0].newInstance();
		return genClassName;
	}
}

class ConcerningClassLoader extends ClassLoader {
	public Class<?> defineClass(String name, byte[] b) {
		return defineClass(name, b, 0, b.length);
	}
}

// the de-genericer
class CodecBuilderFunction<FC extends FeatureConfiguration> implements Function<RecordCodecBuilder.Instance<FC>, App<RecordCodecBuilder.Mu<FC>, FC>> {
	CodecBuilderFunction(CodecBuilder b) {
		this.builder = b;
	}

	private final CodecBuilder builder;

	@Override
	public App apply(RecordCodecBuilder.Instance instance) {

		return this.builder.apply(instance);
	}
}

@FunctionalInterface
interface CodecBuilder {
	App apply(RecordCodecBuilder.Instance instance);
}

class AdaptableDescriptor {
	AdaptableDescriptor(Class<?> clazz) {
		this(clazz, CodecFactory.descr(clazz.getName().replace('.', '/')), false, false);
	}

	private AdaptableDescriptor(Class<?> clazz, String jvmRep, boolean primitive, boolean bit64) {
		this.primitive = primitive;
		this.clazz = clazz;
		this.bit64 = bit64;
		this.jvmRep = jvmRep;
	}

	final boolean primitive;
	final boolean bit64;
	final Class<?> clazz;
	private final String jvmRep; // use toString() to get this

	static final AdaptableDescriptor INT = new IntDescriptor(int.class, "I");
	static final AdaptableDescriptor BYTE = new IntDescriptor(byte.class, "B");
	static final AdaptableDescriptor SHORT = new IntDescriptor(short.class, "S");
	static final AdaptableDescriptor LONG = new AdaptableDescriptor(long.class, "J", true, true) {
		@Override
		public void visitReturn(MethodVisitor visitor) {
			visitor.visitInsn(LRETURN);
		}
	};
	static final AdaptableDescriptor DOUBLE = new AdaptableDescriptor(double.class, "D", true, true) {
		@Override
		public void visitReturn(MethodVisitor visitor) {
			visitor.visitInsn(DRETURN);
		}
	};
	static final AdaptableDescriptor FLOAT = new AdaptableDescriptor(float.class, "F", true, false) {
		@Override
		public void visitReturn(MethodVisitor visitor) {
			visitor.visitInsn(FRETURN);
		}
	};
	static final AdaptableDescriptor CHAR = new IntDescriptor(char.class, "C");
	static final AdaptableDescriptor BOOLEAN = new IntDescriptor(boolean.class, "Z");

	@Override
	public String toString() {
		return this.jvmRep;
	}

	public void visitReturn(MethodVisitor visitor) {
		visitor.visitInsn(RETURN);
	}

	private static final class IntDescriptor extends AdaptableDescriptor {
		IntDescriptor(Class<?> clazz, String jvmRep) {
			super(clazz, jvmRep, true, false);
		}

		@Override
		public void visitReturn(MethodVisitor visitor) {
			visitor.visitInsn(IRETURN);
		}
	}
}

record CodecField(String name, String snakeName, AdaptableDescriptor descriptor) {
}
