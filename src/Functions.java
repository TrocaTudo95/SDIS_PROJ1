
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

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
	
	public static byte[] concatB(byte[] a, byte[] b) {

		byte[] c = new byte[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length,  b.length);

		return c;
	}

}
