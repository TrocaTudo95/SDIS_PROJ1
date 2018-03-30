
package memory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileStorage {
	

	public static boolean fileExists(String fileName) {
		File file = new File(fileName);
		if(file.exists() && file.isFile()) {
			return true;
		}else {
			return false;
		}
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