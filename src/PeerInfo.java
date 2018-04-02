import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class PeerInfo implements Serializable{

	public  ConcurrentHashMap<String, ConcurrentHashMap<Integer, ArrayList<Integer>>> peersContainingChunks;
	public  ConcurrentHashMap<String, ArrayList<Integer>> savedChunks;
	public  ConcurrentHashMap<String, Integer> repDegreePerFile;
	public  ArrayList<Chunk> chunksToRestore;

	
	public PeerInfo() {
		savedChunks = new ConcurrentHashMap<>();
		repDegreePerFile=new ConcurrentHashMap<>();
		peersContainingChunks=new ConcurrentHashMap<>();
		chunksToRestore = new ArrayList<>();
	}
}