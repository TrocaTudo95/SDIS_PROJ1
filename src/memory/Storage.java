package memory;

import java.io.Serializable;
import java.util.ArrayList;

import chunks.Chunk;

public class Storage implements Serializable {
	
	private  ArrayList<Chunk> chunkList;
	
	public Storage() {
		chunkList = new ArrayList<Chunk>();
	}
	
	public boolean HasChunk(Chunk chunk) {
		return this.chunkList.contains(chunk);
	}
	
	
	

}
