package utils;

import chunks.Chunk;
import dispatchers.MC_Dispatcher;

import java.io.IOException;
import java.net.DatagramPacket;

import Peer.Peer;

public class Services {
	
	public static final String CRLF = "\r"+"\n";
	public static final String version = "1.0";
	
	//Backup Headers
	
	public void PUTCHUNK(Chunk chunk,int senderID) {
		String header = "PUTCHUNK" + " " + version + " " + senderID + " " + chunk.getFileID() + " " + chunk.getChunkNo() + " " + chunk.getRepDegree() + " " + CRLF + CRLF;
		// send To MDB
	}

	void STORED(Chunk chunk,int senderID) {
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
	
	public void DELETE(String fileID,int senderID) {
		String header = "DELETE" + " " + version + senderID + " " + fileID + " " + CRLF + CRLF;

		//sent to mc
	}
	
	//Reclaim space header
	
	public void REMOVED(Chunk chunk, int senderID) {
		String header = "REMOVED" + " " + version + senderID + " " + " " + chunk.getFileID() + " " + chunk.getChunkNo() + " " + CRLF + CRLF;

		//send to mc
	}
	
	private void sendToMC(byte[] buf) {
		DatagramPacket packet = new DatagramPacket(buf, buf.length,Peer.getMcDispacther().mc_address, Peer.getMcDispacther().mc_port);

		try {
			MC_Dispatcher.mc_socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
