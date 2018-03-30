package utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.util.*;
import Peer.Peer;
import chunks.Chunk;

public class PacketHandler implements Runnable {

	private DatagramPacket packet;
	private String[] headerToken;
	private String header;
	private byte[] body;
	
	public PacketHandler(DatagramPacket packet) {
		this.packet = packet;
	}
	

	
	public void run() {
		
		this.headerToken = HeaderExtractor();
		this.body = BodyExtractor();
		if(this.headerToken[0] == "PUTCHUNK") {
			// PUTCHUNK Handler
		}else if(this.headerToken[0] == "STORED") {
			//STORED Handler
		}else if(this.headerToken[0] == "GETCHUNK") {
			System.out.println("GETCHUNK");
			
		}else if(this.headerToken[0] == "CHUNK") {
			System.out.println("CHUNK");
			
		}else if(this.headerToken[0] == "DELETE") {
			System.out.println("DELETE");
			
		}else if(this.headerToken[0] == "REMOVED") {
			System.out.println("REMOVED");
			
		}
		
		
	}
	
	
	public void PUTCHUNK_handler() {
		
			
			int senderID=Integer.parseInt(headerToken[2]);
			//ignore messages from his own
			if(senderID==Peer.getID())
				return;
			
			//Chunk chunk= new Chunk(this.headerToken[3],Integer.parseInt(this.headerToken[4]),Integer.parseInt(this.headerToken[5]),this.body);
			
			if(Peer.getUsedSpace()+body.length< Peer.MEMORY) {
				String File_ID=this.headerToken[3];
				int chunkNO=Integer.parseInt(this.headerToken[4]);
				int replication_degree=Integer.parseInt(this.headerToken[5]);
				if (!Peer.savedChunks.containsKey(File_ID) || !Peer.savedChunks.get(File_ID).contains(chunkNO)) {
					Peer.saveChunk(File_ID,chunkNO,replication_degree);
					//enviar stored
				}
			
		}
			else {
				System.out.println("Not enough space");
            return;
			}
	}
	public String[] HeaderExtractor() {
		ByteArrayInputStream stream = new ByteArrayInputStream(packet.getData());
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));

		try {
			this.header = reader.readLine();
			System.out.println(this.header);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return this.header.split(("\\s+"));
	}
	
	public byte[] BodyExtractor() {

		ByteArrayInputStream stream = new ByteArrayInputStream(packet.getData());
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));

		String linha = "linha";
		int SumLinhas = 0;
		int numLinhas = 0;

		while(!linha.isEmpty()) {
			try {
				linha = reader.readLine();
				SumLinhas += linha.length();
				numLinhas++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		int BodyStarter = ("\r"+"\n").getBytes().length * SumLinhas + numLinhas;

		return Arrays.copyOfRange(packet.getData(), BodyStarter,packet.getLength());
	}


}
