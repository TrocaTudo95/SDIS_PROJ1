import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RestoreProtocol{
	
	private static boolean full = false;
	private static String file_ID;
	

	public static void RestoreFile(String fileId) {
		file_ID = fileId;
		int nChunks = PeerInfo.savedChunks.get(fileId).size();
		for(int i=1;i <= nChunks;i++) {
			
			Services.GETCHUNK(fileId,i,Peer.getID());
			
			if(i==nChunks)
				full=true;
			
		}
		
	}
	public static void AssembleFile() {
		byte[] fileDados = new byte[0];
		int nChunks = PeerInfo.savedChunks.get(file_ID).size();
		if(full) {
			for(int i=1;i <= nChunks;i++) {
				for(int k=0;k < PeerInfo.chunksToRestore.size();k++) {
					if(PeerInfo.chunksToRestore.get(k).getChunkNo() == i) {
						Functions.concatB(fileDados,PeerInfo.chunksToRestore.get(k).getDados());
					}
				}
			}
			
			File chunkFile = new File("chunksDir/", file_ID);
			chunkFile.getParentFile().mkdirs();
			try {
				FileOutputStream out = new FileOutputStream(chunkFile);
				out.write(fileDados);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("File Restored");
		
		}else
			return;

}
	

}
