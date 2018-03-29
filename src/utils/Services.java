package utils;

import chunks.Chunk;
import Peer.Peer;

public class Services {
	
	public static final String CRLF = "\r"+"\n";
	public static final String version = "1.0";
	
	//Backup Headers
	
	void PUTCHUNK(Chunk chunk,Peer peer) {
		String header = "PUTCHUNK" + " " + version + " " + peer.getID() + " " + chunk.getFileID() + " " + chunk.getChunkNo() + " " + chunk.getRepDegree() + " " + CRLF + CRLF;
		// send To MDB
	}

	void STORED(Chunk chunk,Peer peer) {
		String header = "STORED" + " " + version + " " + peer.getID() + " " + chunk.getFileID() + " " + chunk.getChunkNo() + " " + CRLF + CRLF ;
		//send to MC
	}
	
	//Restore Headers
	void GETCHUNK(Chunk chunk,Peer peer) {
		
		String header = "GETCHUNK" + " " + version + " " + peer.getID() + " " + chunk.getFileID() + " " + chunk.getChunkNo() + " " + CRLF + CRLF;

		//Send To MC
	}
	
	public void CHUNK(Chunk chunk, Peer peer) {
		String header = "CHUNK" + " " + version + " " + peer.getID() + " "+ chunk.getFileID() + " " + chunk.getChunkNo() + " " + CRLF + CRLF;

		//send to MDR
	}
	
	//Delete Header
	
	public void DELETE(String fileID, Peer peer) {
		String header = "DELETE" + " " + version + peer.getID() + " " + fileID + " " + CRLF + CRLF;

		//sent to mc
	}
	
	//Reclaim space header
	
	public void REMOVED(Chunk chunk, Peer peer) {
		String header = "REMOVED" + " " + version + peer.getID() + " " + " " + chunk.getFileID() + " " + chunk.getChunkNo() + " " + CRLF + CRLF;

		//send to mc
	}
	
	
	

}
