package protocols;

import Peer.Peer;
import chunks.Chunk;
import utils.Services;

public class BackupProtocol implements Runnable{
	

	private int senderID;
	private int replicationDegree;
	private Chunk chunk;
	
	
	public BackupProtocol(Chunk chunk,int replicationDegree,int senderID) {
		this.chunk=chunk;
		this.replicationDegree= replicationDegree;
		this.senderID=senderID;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		int attempts=0;
		
		while (attempts < 5 && Peer.getRepDegreeAtual(chunk.getFileID(),chunk.getChunkNo()) < replicationDegree) {
            Services.PUTCHUNK(chunk,senderID);
            try {
                Thread.sleep(2 ^ attempts * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            attempts++;
        }
		if (attempts == 5)
            System.out.println("Couldn't backup chunk");
        else
            System.out.println("Backup for chunk finished successfully");
    }


}

