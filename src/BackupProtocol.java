
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;


public class BackupProtocol implements Runnable{
	

	private int senderID;
	private int replicationDegree;
	private static Chunk chunk;
	
	
	public BackupProtocol(Chunk chunk,int replicationDegree,int senderID) {
		this.replicationDegree= replicationDegree;
		this.senderID=senderID;
		this.chunk=chunk;
	}
	
    public static void backupFile(File file, int repDegree) {
    	
       
        int chunkNo = 1;

        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
    
            int nChunks = (int) (file.length()/Chunk.SIZE);
            int lastChunk= (int) (file.length()%Chunk.SIZE);
			if(nChunks > 0) {
            		for(int i=0; i < nChunks; i++) {
            				byte[] buffer = new byte[Chunk.SIZE];
            				inputStream.read(buffer);          		
            				Chunk chunk= new Chunk(Functions.getHashedFileID(file),chunkNo,repDegree,buffer);
            				BackupProtocol backup = new BackupProtocol(chunk,repDegree,Peer.getID());
            				backup.run();
 
            				chunkNo++;
            			}
            	
            }
            byte[] lastData = new byte[lastChunk];
    			inputStream.read(lastData);
    			Chunk chunk= new Chunk(Functions.getHashedFileID(file),chunkNo,repDegree,lastData);
    			BackupProtocol backup = new BackupProtocol(chunk,repDegree,Peer.getID());
    			backup.run();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		int attempts=0;
		Random rand = new Random();
		
		while (attempts < 5 && MDB_Dispatcher.currentRepDegree(chunk.getFileID(),chunk.getChunkNo()) < replicationDegree) {
            Services.PUTCHUNK(chunk,senderID);
            try {
            		System.out.println("thread going to sleep");
            		int n_sleep= rand.nextInt(400);
            		Thread.sleep(n_sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            attempts++;
        }
		if (attempts == 5)
            System.out.println("Couldn't backup chunk");
        else {
        	if( MDB_Dispatcher.currentRepDegree(chunk.getFileID(),chunk.getChunkNo()) == replicationDegree && replicationDegree>0)
            System.out.println("Backup for chunk finished successfully. there are "+MDB_Dispatcher.currentRepDegree(chunk.getFileID(),chunk.getChunkNo()) +" peers with this chunk");
        	else
        		System.out.println("It wasn't possible to backup the file with the required replication degree");
        }
    }
	
	


}

