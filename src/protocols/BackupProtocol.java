package protocols;


public class BackupProtocol {
	
	private static final int MIN_DELAY =0;
	private static final int MAX_DELAY =400;
	
	private String senderID;
	private int chunkNo;
	private int replicationDegree;
	private String fileName;
	
	
	public BackupProtocol(String fileName,int replicationDegree) {
		this.fileName=fileName;
		this.replicationDegree= replicationDegree;
		System.out.println("ola");
	}

}
