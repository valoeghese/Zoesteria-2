package tk.valoeghese.common.util;

import java.io.File;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class FileUtils {
	private FileUtils() {
	}

	public static void trailFilesOfExtension(File root, String extension, BiConsumer<File, String> callback) {
		assert root.isDirectory() : "root file is not a directory! (FileUtils#trailFilesOfExtension)";
		extension = "." + extension;
		uTrailFilesOfExtension(root, extension, callback, "");
	}

	private static void uTrailFilesOfExtension(File root, String extension, BiConsumer<File, String> callback, String directoryTrail) {
		for (File file : root.listFiles()) {
			if (file.isDirectory()) {
				uTrailFilesOfExtension(file, extension, callback, directoryTrail + "/" + file.getName());
			} else if (file.getName().endsWith(extension)) {
				callback.accept(file, directoryTrail);
			}
		}
	}

	public static void forEachFileOfExtension(File directory, String extension, Consumer<File> callback) {
		assert directory.isDirectory() : "directory file is not a directory! (FileUtils#forEachFileOfExtension)";
		extension = "." + extension;

		for (File file : directory.listFiles()) {
			if (!file.isDirectory() && file.getName().endsWith(extension)) {
				callback.accept(file);
			}
		}
	}

	public static void forEachDirectory(File directory, Consumer<File> callback) {
		assert directory.isDirectory() : "directory file is not a directory! (FileUtils#forEachDirectory)";

		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				callback.accept(file);
			}
		}
	}
}