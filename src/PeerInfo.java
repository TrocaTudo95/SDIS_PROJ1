import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class PeerInfo implements Serializable{
	public static ConcurrentHashMap<String, ConcurrentHashMap<Integer, ArrayList<Integer>>> peersContainingChunks;
	public static ConcurrentHashMap<String, ArrayList<Integer>> savedChunks;
	public static ConcurrentHashMap<String, Integer> repDegreePerFile;
	
	
	public PeerInfo() {
		savedChunks = new ConcurrentHashMap<>();
		repDegreePerFile=new ConcurrentHashMap<>();
		peersContainingChunks=new ConcurrentHashMap<>();
	}
}