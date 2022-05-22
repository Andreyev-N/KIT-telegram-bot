package com.kit.wb.wbbot;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class CredentialUtils {

	public static String getBotName() {
		return getFileContent("credentials/botName.txt");
	}

	public static String getBotToken() {
		return getFileContent("credentials/botToken.txt");
	}

	private static String getFileContent(String filePath) {
		try {
			Path path = new File(filePath).toPath();
			return Files.readString(path);
		} catch (IOException e) {
			throw new RuntimeException("Error reading file", e);
		}
	}
}
