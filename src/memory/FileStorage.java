package memory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileStorage {
	

	public static boolean fileExists(String fileName) {
		File file = new File(fileName);
		return file.exists() && file.isFile();
	}
	
	private static void createFolder(String folderName) {
		File file = new File(folderName);
		file.mkdir();
	}
	
	private static boolean folderExists(String folderName) {
		File file = new File(folderName);
		return file.exists() && file.isDirectory();
	}

	public static final byte[] loadFile(File file) {
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		byte[] data = new byte[(int) file.length()];

		try {
			inputStream.read(data);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}

}
