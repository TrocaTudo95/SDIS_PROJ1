package chunks;

import java.io.Serializable;

public class File implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String fileId;
	private int chunks;
	
	public File(String fileId,int chunks) {
		
		this.fileId = fileId;
		this.chunks = chunks;
		
	}
	public String getfileId() {
		return this.fileId;
	}
	public int getchunks() {
		return this.chunks;
	}
	
	@Override
	public String toString() {
		return fileId + "[" + chunks + "]";
	}
	

}
