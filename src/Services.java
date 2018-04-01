
import java.io.IOException;
import java.net.DatagramPacket;



public class Services {
	
	public static final String CRLF = "\r"+"\n";
	public static final String version = "1.0";
	
	//Backup Headers
	
	public static void PUTCHUNK(Chunk chunk,int senderID) {
		System.out.println("sending putchunk");
		String header = "PUTCHUNK" + " " + version + " " + senderID + " " + chunk.getFileID() + " " + chunk.getChunkNo() + " " + chunk.getRepDegree() + " " + CRLF + CRLF;
		byte[] packet = concatB(header.getBytes(), chunk.getDados());
		sendToMDB(packet);
	}

	public static void STORED(Chunk chunk,int senderID) {
		String header = "STORED" + " " + version + " " + senderID + " " + chunk.getFileID() + " " + chunk.getChunkNo() + " " + CRLF + CRLF ;
		sendToMC(header.getBytes());
	}
	
	//Restore Headers
	void GETCHUNK(Chunk chunk,int senderID) {
		
		String header = "GETCHUNK" + " " + version + " " + senderID + " " + chunk.getFileID() + " " + chunk.getChunkNo() + " " + CRLF + CRLF;

		//Send To MC
	}
	
	public void CHUNK(Chunk chunk,int senderID) {
		String header = "CHUNK" + " " + version + " " + senderID + " "+ chunk.getFileID() + " " + chunk.getChunkNo() + " " + CRLF + CRLF;

		//send to MDR
	}
	
	//Delete Header
	
	public static void DELETE(String fileID,int senderID) {
		String header = "DELETE" + " " + version + " " + senderID + " " + fileID + " " + CRLF + CRLF;
		sendToMC(header.getBytes());
	}
	
	//Reclaim space header
	
	public void REMOVED(Chunk chunk, int senderID) {
		String header = "REMOVED" + " " + version + senderID + " " + " " + chunk.getFileID() + " " + chunk.getChunkNo() + " " + CRLF + CRLF;

		//send to mc
	}
	

	private synchronized static void sendToMC(byte[] buf) {
		DatagramPacket packet = new DatagramPacket(buf, buf.length,Peer.getMcDispacther().mc_address, Peer.getMcDispacther().mc_port);

		try {
			MC_Dispatcher.mc_socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("writing to mc");
		
	}
	
	private synchronized static void sendToMDB(byte[] buf) {
		DatagramPacket packet = new DatagramPacket(buf, buf.length,Peer.getMDBDispacther().mdb_address, Peer.getMDBDispacther().mdb_port);

		try {
			MDB_Dispatcher.mdb_socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("writing to mdb");
	}
	
	private static byte[] concatB(byte[] a, byte[] b) {

		byte[] c = new byte[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length,  b.length);

		return c;
	}
	
	

}
