import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
			GETCHUNK_handler();
			break;
		case "CHUNK":
			CHUNK_handler();
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
			if (!(Peer.info.savedChunks.containsKey(File_ID)) || !(Peer.info.savedChunks.get(File_ID).contains(chunkNO))) {
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
	
		String File_ID = this.headerToken[3];
		int chunkNO = Integer.parseInt(this.headerToken[4]);

		MDB_Dispatcher.storedReceived(File_ID, chunkNO, senderID);
		
		if(!Peer.info.peersContainingChunks.contains(File_ID)) 
			Peer.info.peersContainingChunks.put(File_ID, new ConcurrentHashMap<>());
		
		if(!Peer.info.peersContainingChunks.get(File_ID).contains(chunkNO))
			Peer.info.peersContainingChunks.get(File_ID).put(chunkNO, new ArrayList<>());
		
		Peer.info.peersContainingChunks.get(File_ID).get(chunkNO).add(senderID);
	

	}
	
	public void DELETE_handler() {
		int senderID = Integer.parseInt(headerToken[2]);
		if (senderID == Peer.getID())
			return;
		System.out.println("Handling Delete message");
		String File_ID = this.headerToken[3];

		Peer.deleteFile(File_ID);
	}
	
	public void GETCHUNK_handler() {
		int senderID = Integer.parseInt(headerToken[2]);
		if (senderID == Peer.getID())
			return;
		System.out.println("Handling GETCHUNK message");
		String fileID = this.headerToken[3];
		int chunkNo = Integer.parseInt(this.headerToken[4]);
		if(Peer.info.savedChunks.get(fileID).contains(chunkNo)) { // Peer have a copy of that chunk
			String chunkName =fileID + "_" + chunkNo;
			File chunkFile = new File("chunksDir/" + chunkName);
			try {
				FileInputStream inputStream = new FileInputStream(chunkFile);
				byte[] body = new byte[(int) chunkFile.length()];
				try {
					inputStream.read(body);
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Random random= new Random();
			
			try {
	            Thread.sleep(random.nextInt(401)); //random sleep time between 0 and 400
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }

			
			Chunk chunk = new Chunk(fileID,chunkNo,0,body);
			Services.CHUNK(chunk, Peer.getID());

		}
		
	}
	
	public void CHUNK_handler() {
		int senderID = Integer.parseInt(headerToken[2]);
		if (senderID == Peer.getID())
			return;
		System.out.println("Handling Chunk message");
		String File_ID = this.headerToken[3];
		int chunkNO = Integer.parseInt(this.headerToken[4]);
		byte[] body = BodyExtractor();
		Chunk chunk=new Chunk(File_ID,chunkNO,0,body);
		Peer.info.chunksToRestore.add(chunk);
		RestoreProtocol.AssembleFile();
		
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