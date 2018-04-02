
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


public class MDB_Dispatcher implements Runnable

{
	public static MulticastSocket mdb_socket;

	public InetAddress mdb_address;
	public int mdb_port;
	public static ConcurrentHashMap<String, ConcurrentHashMap<Integer, ArrayList<Integer>>> peershavingChunks;
	public static final int CHUNK_SIZE =64000;

	
	
	public static void storedReceived(String FileID, int ChunkNO,int PeerID) {
		if(!peershavingChunks.containsKey(FileID)) {
			peershavingChunks.put(FileID,  new ConcurrentHashMap<>());
		}
		
		if (!peershavingChunks.get(FileID).containsKey(ChunkNO))
			peershavingChunks.get(FileID).put(ChunkNO, new ArrayList<>());
		
		peershavingChunks.get(FileID).get(ChunkNO).add(PeerID);
			
		
	}
	
	public static int currentRepDegree(String FileID, int ChunkNO) {
		
		
		try {
			return peershavingChunks.get(FileID).get(ChunkNO).size();
		} catch (NullPointerException n) {
			return 0;
		}
	}
	
	
	
	public MDB_Dispatcher(int mdb_port,InetAddress mdb_address) {
		this.mdb_port=mdb_port;
		this.mdb_address=mdb_address;
		peershavingChunks=new ConcurrentHashMap();
	}

	
	public void connect_to_multicast_socket() {
		try {
			mdb_socket = new MulticastSocket(mdb_port);

			mdb_socket.setTimeToLive(1);
			mdb_socket.joinGroup(mdb_address);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		byte[] buffer= new byte[CHUNK_SIZE];
		connect_to_multicast_socket();
		
		
		while(true) {
			DatagramPacket mdb_packet = new DatagramPacket(buffer, buffer.length);
			try {
				mdb_socket.receive(mdb_packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("received packet");
			new Thread(new PacketHandler(mdb_packet)).start();
		} 	
		
		
		
	}

}
