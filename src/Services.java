
import java.io.IOException;
import java.net.DatagramPacket;



public class Services {
	
	public static final String CRLF = "\r"+"\n";
	public static final String version = "1.0";
	
	//Backup Headers
	
	public static void PUTCHUNK(Chunk chunk,int senderID) {
		System.out.println("sending putchunk");
		String header = "PUTCHUNK" + " " + version + " " + senderID + " " + chunk.getFileID() + " " + chunk.getChunkNo() + " " + chunk.getRepDegree() + " " + CRLF + CRLF;
		byte[] packet = Functions.concatB(header.getBytes(), chunk.getDados());
		sendToMDB(packet);
	}

	public static void STORED(Chunk chunk,int senderID) {
		String header = "STORED" + " " + version + " " + senderID + " " + chunk.getFileID() + " " + chunk.getChunkNo() + " " + CRLF + CRLF ;
		sendToMC(header.getBytes());
	}
	
	//Restore Headers
	public static void GETCHUNK(String fileId,int ChunkNo,int senderID) {
		
		String header = "GETCHUNK" + " " + version + " " + senderID + " " + fileId + " " + ChunkNo + " " + CRLF + CRLF;
		sendToMC(header.getBytes());
	}
	
	public static void CHUNK(Chunk chunk,int senderID) {
		String header = "CHUNK" + " " + version + " " + senderID + " "+ chunk.getFileID() + " " + chunk.getChunkNo() + " " + CRLF + CRLF;
		byte[] packet = Functions.concatB(header.getBytes(), chunk.getDados());
		sendToMDR(packet);
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
	
	private synchronized static void sendToMDR(byte[] buf) {
		DatagramPacket packet = new DatagramPacket(buf, buf.length,Peer.getMDRDispacther().mdr_address, Peer.getMDRDispacther().mdr_port);

		try {
			MDR_Dispatcher.mdr_socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("writing to mdr");
	}
	
	
	

}
