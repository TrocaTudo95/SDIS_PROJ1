package protocols;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

import Peer.Peer;
import chunks.Chunk;
import utils.Functions;

public class BackupProtocol implements Runnable{
	
	private static final int MIN_DELAY =0;
	private static final int MAX_DELAY =400;
	
	private int senderID;
	private int chunkNo;
	private int replicationDegree;
	private File fileName;
	
	
	public BackupProtocol(File fileName,int replicationDegree) {
		this.fileName=fileName;
		this.replicationDegree= replicationDegree;
	}
	
	public void BackupChunk(Chunk chunk,int senderID) {
		
		Peer.getMcDispacther().SaveChunksStored(chunk);
		long waitingTime = 400;
		int attempt = 0;
		boolean done = false;
		while (!done) {
			Peer.getMcDispacther().clearChunksStored(chunk);

			Peer.getServices().PUTCHUNK(chunk,senderID);

			int confirmedRepDeg = Peer.getMcDispacther().getChunksStored(chunk);

			if (confirmedRepDeg < chunk.getRepDegree()) {
				attempt++;

				if (attempt > 4) {
					System.out.println("Reached maximum number of attempts to backup chunk with desired replication degree.");
					done = true;
				} else {
					System.out.println("Desired replication degree was not reached. Trying again...");
					waitingTime *= 2;
				}
			} else {
				System.out.println("Desired replication degree reached.");
				done = true;
			}
		}
		
		Peer.getMcDispacther().stopSavingChunksStored(chunk);
		
		
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		String fileID = Functions.getHashedFileID(this.fileName);
		
//		byte[] fileData = FileStorage.loadFile(this.fileName);
//
//		int nChunks = fileData.length / Chunk.SIZE + 1;
//
//		ByteArrayInputStream stream = new ByteArrayInputStream(fileData);
//		byte[] streamConsumer = new byte[Chunk.SIZE];
//		
//		for (int i = 0; i < nChunks; i++) {
//			byte[] chunkData;
//
//			if (i == nChunks - 1 && fileData.length % Chunk.SIZE == 0) {
//				chunkData = new byte[0];
//			} else {
//				int numBytesRead = stream.read(streamConsumer, 0, streamConsumer.length);
//
//				chunkData = Arrays.copyOfRange(streamConsumer, 0, numBytesRead);
//			}
//			
//			Chunk chunk = new Chunk(fileID, i, replicationDegree, chunkData);
//
//			BackupChunk(chunk,this.senderID);
//
//		}

	}
}
