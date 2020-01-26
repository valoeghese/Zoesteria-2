package tk.valoeghese.zoesteria.core.genmodifierpack;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tk.valoeghese.zoesteriaconfig.impl.util.FileUtil;

/**
 * Public version of the writer code in {@link tk.valoeghese.zoesteriaconfig.impl.parser.ImplZoesteriaConfigAccess}
 */
public final class ConfigWriter {
	public ConfigWriter(Map<String, Object> configMap) {
		this.parserMap = configMap;
	}

	private final Map<String, Object> parserMap;

	@SuppressWarnings("unchecked")
	private void writeObject(StringBuilder fileDataBuffer, Object data, int indent) {
		if (data instanceof Map) {
			fileDataBuffer.append("{\n");
			this.writeContainerData(fileDataBuffer, ((Map<String, Object>) data), indent + INDENT_SIZE);

			if (indent > 0) {
				fileDataBuffer.append(indent(indent));
			}
			fileDataBuffer.append("}");
		} else if (data instanceof List) {
			this.writeList(fileDataBuffer, ((List<Object>) data), indent + INDENT_SIZE);
		} else if (data instanceof String) {
			fileDataBuffer.append((String) data).append(";");
		} else {
			fileDataBuffer.append(data.toString()).append(";");
		}

		if (indent == 0) {
			fileDataBuffer.append("\n");
		}
	}

	private void writeContainerData(StringBuilder fileDataBuffer, Map<String, Object> containerData, int indent) {
		containerData.forEach((key, value) -> {
			if (indent > 0) {
				fileDataBuffer.append(indent(indent));
			}

			fileDataBuffer.append(key).append(" = ");
			this.writeObject(fileDataBuffer, value, indent);
			fileDataBuffer.append("\n");
		});
	}

	private void writeList(StringBuilder fileDataBuffer, List<Object> listData, int indent) {
		fileDataBuffer.append("[\n");
		listData.forEach(value -> {
			if (indent > 0) {
				fileDataBuffer.append(indent(indent));
			}

			this.writeObject(fileDataBuffer, value, indent);
			fileDataBuffer.append("\n");
		});

		int finalIndent = indent - INDENT_SIZE;

		if (finalIndent > 0) {
			fileDataBuffer.append(indent(finalIndent));
		}

		fileDataBuffer.append("]");

		if (finalIndent == 0) {
			fileDataBuffer.append("\n");
		}
	}

	public void writeToFile(File file) {
		StringBuilder fileDataBuffer = new StringBuilder();
		this.writeContainerData(fileDataBuffer, this.parserMap, 0);
		FileUtil.writeFile(file, fileDataBuffer.toString());
	}

	private static final int INDENT_SIZE = 2;
	private static final Map<Integer, String> INDENT_STZE_STRING_MAP = new HashMap<>();

	private static String indent(int indentSize) {
		return INDENT_STZE_STRING_MAP.computeIfAbsent(indentSize, size -> {
			char[] buffer = new char[size];

			for (int i = 0; i < size; ++i) {
				buffer[i] = ' ';
			}
			return String.valueOf(buffer);
		});
	}
}
