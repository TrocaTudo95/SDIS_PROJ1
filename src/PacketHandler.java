
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PacketHandler implements Runnable {

	private DatagramPacket packet;
	private String[] headerToken;
	private String header;
	private byte[] body;

	public PacketHandler(DatagramPacket packet) {
		this.packet = packet;
	}

	public void run() {
		System.out.println("handling message");
		this.headerToken = HeaderExtractor();
		this.body = BodyExtractor();
		switch (headerToken[0]) {

		case "PUTCHUNK":
			PUTCHUNK_handler();
			break;
		case "STORED":
			STORED_handler();
			break;
		// if(this.headerToken[0] == "PUTCHUNK") {
		// PUTCHUNK_handler();
		// }else if(this.headerToken[0] == "STORED") {
		// //STORED Handler
		// }else if(this.headerToken[0] == "GETCHUNK") {
		// System.out.println("GETCHUNK");
		//
		// }else if(this.headerToken[0] == "CHUNK") {
		// System.out.println("CHUNK");
		//
		// }else if(this.headerToken[0] == "DELETE") {
		// System.out.println("DELETE");
		//
		// }else if(this.headerToken[0] == "REMOVED") {
		// System.out.println("REMOVED");
		//
		// }
		}

	}

	public void PUTCHUNK_handler() {

		System.out.println("handling putchunk message");
		int senderID = Integer.parseInt(headerToken[2]);
		// ignore messages from his own
		if (senderID == Peer.getID())
			return;

		Chunk chunk = new Chunk(this.headerToken[3], Integer.parseInt(this.headerToken[4]),
				Integer.parseInt(this.headerToken[5]), this.body);

		if (Peer.getUsedSpace() + body.length < Peer.MEMORY) {
			System.out.println("it has space to store");
			String File_ID = this.headerToken[3];
			System.out.println(File_ID);
			int chunkNO = Integer.parseInt(this.headerToken[4]);
			int replication_degree = Integer.parseInt(this.headerToken[5]);
			if (Peer.savedChunks.containsKey(File_ID)==false || !Peer.savedChunks.get(File_ID).contains(chunkNO)) {
				System.out.println("saving chunk");
				Peer.saveChunk(File_ID, chunkNO, replication_degree, body);
				// send store message
				System.out.println("Sending Stored message");
				Services.STORED(chunk, Peer.getID());

			}

		} else {
			System.out.println("Not enough space");
			return;
		}
	}

	public void STORED_handler() {

		int senderID = Integer.parseInt(headerToken[2]);
		System.out.println("Handling Stored message");
		if (senderID == Peer.getID())
			return;
		int chunkNo = Integer.parseInt(headerToken[4]);
		int value = 0;
		if (Peer.chunksStored.contains(chunkNo)) {
			Peer.chunksStored.getOrDefault(chunkNo, value);
			value++;
			Peer.chunksStored.remove(chunkNo);
			Peer.chunksStored.put(chunkNo, value);
		} else {
			Peer.chunksStored.put(chunkNo, 1);
		}

		String fileID = headerToken[3];
		if (Peer.peersContainingChunks.contains(fileID)) {
			if (Peer.peersContainingChunks.get(fileID).contains(chunkNo)) {
				Peer.peersContainingChunks.get(fileID).get(chunkNo).add(senderID);
			} else {
				ArrayList<Integer> list = new ArrayList<Integer>();
				list.add(senderID);
				Peer.peersContainingChunks.get(fileID).put(chunkNo, list);
			}
		} else {
			ArrayList<Integer> list = new ArrayList<Integer>();
			list.add(senderID);
			ConcurrentHashMap<Integer, ArrayList<Integer>> chunks = new ConcurrentHashMap<Integer, ArrayList<Integer>>();
			chunks.put(chunkNo, list);
			Peer.peersContainingChunks.put(fileID, chunks);
		}

	}

	public String[] HeaderExtractor() {
		ByteArrayInputStream stream = new ByteArrayInputStream(packet.getData());
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

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
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

		String linha = "linha";
		int SumLinhas = 0;
		int numLinhas = 0;

		while (!linha.isEmpty()) {
			try {
				linha = reader.readLine();
				SumLinhas += linha.length();
				numLinhas++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		int BodyStarter = ("\r" + "\n").getBytes().length * SumLinhas + numLinhas;

		return Arrays.copyOfRange(packet.getData(), BodyStarter, packet.getLength());
	}

}
