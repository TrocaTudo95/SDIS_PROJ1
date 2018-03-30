package utils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import Peer.Peer;

public class Functions {
	
	
	public static final String getHashedFileID(File file) {
		String fileId = file.getName() + file.length() + file.lastModified();

		return Hashsha256(fileId);
	}
	
	public static final String Hashsha256(String str) {
		try {
			MessageDigest fileID = MessageDigest.getInstance("SHA-256");
			byte[] hash = fileID.digest(str.getBytes(StandardCharsets.UTF_8));
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return null;
	}

}
