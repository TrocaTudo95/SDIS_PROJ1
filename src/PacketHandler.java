
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
		this.headerToken = HeaderExtractor();
		switch (headerToken[0]) {

		case "PUTCHUNK":
			this.body = BodyExtractor();
			PUTCHUNK_handler();
			break;
		case "STORED":
			STORED_handler();
			break;
		case "GETCHUNK":
			break;
		case "CHUNK":
			break;
		case "DELETE":
			DELETE_handler();
			break;
		case "REMOVED":
			break;
		}

	}

	public void PUTCHUNK_handler() {

		int senderID = Integer.parseInt(headerToken[2]);
		// ignore messages from his own
		if (senderID == Peer.getID())
			return;
		System.out.println("handling putchunk message");
		
		Chunk chunk = new Chunk(this.headerToken[3], Integer.parseInt(this.headerToken[4]),Integer.parseInt(this.headerToken[5]), this.body);
		int SpaceNedeed = Peer.getUsedSpace() + body.length;
		if (SpaceNedeed < Peer.MEMORY) {
			System.out.println("It has space to store");
			String File_ID = this.headerToken[3];
			int chunkNO = Integer.parseInt(this.headerToken[4]);
			int replication_degree = Integer.parseInt(this.headerToken[5]);
			if (!(Peer.savedChunks.containsKey(File_ID)) || !(Peer.savedChunks.get(File_ID).contains(chunkNO))) {
				System.out.println("saving chunk");
				Peer.saveChunk(File_ID, chunkNO, replication_degree, body);
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
		if (senderID == Peer.getID())
			return;
		System.out.println("Handling Stored message");
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
		String File_ID = this.headerToken[3];
		int chunkNO = Integer.parseInt(this.headerToken[4]);

		MDB_Dispatcher.storedReceived(File_ID, chunkNO, senderID);

	}
	
	public void DELETE_handler() {
		String File_ID = this.headerToken[3];
		Peer.deleteFile(File_ID);
		
	}

	public String[] HeaderExtractor() {
		ByteArrayInputStream stream = new ByteArrayInputStream(packet.getData());
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

		try {
			this.header = bufferedReader.readLine();
			System.out.println(this.header);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return this.header.split(("\\s+"));
	}

	public byte[] BodyExtractor() {

		ByteArrayInputStream stream = new ByteArrayInputStream(packet.getData());
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

		String linha = "NotEmpty";
		int SumLinhas = 0;
		int numLinhas = 0;

		while (!linha.isEmpty()) {
			try {
				linha = bufferedReader.readLine();
				numLinhas++;
				SumLinhas += linha.length();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		int BodyStarter = ("\r" + "\n").getBytes().length * SumLinhas + numLinhas;
		byte[] data=packet.getData();
		int length=packet.getLength();
		
		return Arrays.copyOfRange(data, BodyStarter,length );
	}

}
