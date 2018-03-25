package de.cognicrypt.codegenerator.primitive.providerUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

import de.cognicrypt.codegenerator.Constants;
import de.cognicrypt.codegenerator.utilities.Utils;

public class Helper {

	private String readFileLineByLine(String filePath) {
		StringBuilder contentBuilder = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contentBuilder.toString();
	}

	public LinkedHashMap<String, String> getSourceCode(File folder) {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		File[] listOfFiles = (folder).listFiles();
		for (File file : listOfFiles) {
			if (file.getName().endsWith(".java")) {
				String className = file.getName();
				String sourceCode = readFileLineByLine(file.getAbsolutePath());
				map.put(className, sourceCode);

			}
		}
		return map;
	}
}
