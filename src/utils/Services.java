package utils;

import chunks.Chunk;
import peers.Peer;

public class Services {
	
	public static final String CRLF = "\r"+"\n";
	public static final String version = "1.0";
	
	//Backup Headers
	
	void PUTCHUNK(Chunk chunk,Peer peer) {
		String header = "PUTCHUNK" + " " + version + " " + peer.getID() + " " + chunk.getFileID() + " " + chunk.getChunkNo() + " " + chunk.getRepDegree() + " " + CRLF + CRLF;
		// send Packet To MDB
	}

	void STORED(Chunk chunk,Peer peer) {
		String header = "STORED" + " " + version + " " + peer.getID() + " " + chunk.getFileID() + " " + chunk.getChunkNo() + " " + CRLF + CRLF ;
		//send Packet to MC
	}
	
	
	

}
