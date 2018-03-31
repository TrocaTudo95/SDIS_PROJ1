
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Peer implements RMI_inteface {

	private static int ID;
	private static Services services;
	private static ConcurrentHashMap<String, ConcurrentHashMap<Integer, ArrayList<Integer>>> repDegreeAtual;
	private static MC_Dispatcher mcDispatcher;
	private static MDB_Dispatcher mdbDispatcher;
	public static ConcurrentHashMap<String, ArrayList<Integer>> savedChunks;
	public static ConcurrentHashMap<String, Integer> repDegreePerFile;
	public static int MEMORY = 10000000;
	private static int used_space = 0;
	public static ConcurrentHashMap<Integer, Integer> chunksStored;
	public static ConcurrentHashMap<String, ConcurrentHashMap<Integer, ArrayList<Integer>>> peersContainingChunks;

	@Override
	public void backup_file(File file, int replicationDegree) throws RemoteException {
		System.out.println("Starting backing up");
		String File_ID = Functions.getHashedFileID(file);

		System.out.println("FileID is:" + File_ID);

		BackupProtocol.backupFile(file, replicationDegree);

	}

	public static int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public static Services getServices() {
		return services;
	}

	public static int getUsedSpace() {
		return used_space;
	}

	public static int getRepDegreeAtual(String fileId, int chunkNo) {
		try {
			return repDegreeAtual.get(fileId).get(chunkNo).size();
		} catch (NullPointerException npe) {
			System.out.println("merdouuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu "+repDegreeAtual.size());
			return 0;
		}
	}

	public static void main(String[] args) throws UnknownHostException {
		InetAddress[] adresses = new InetAddress[3];
		ID=Integer.parseInt(args[1]);
		int[] ports = new int[] { 8000, 8001, 8002 };
		adresses[0] = InetAddress.getByName("224.0.0.0");
		adresses[1] = InetAddress.getByName("224.0.0.0");
		adresses[2] = InetAddress.getByName("224.0.0.0");
		savedChunks = new ConcurrentHashMap<>();
		repDegreeAtual= new ConcurrentHashMap<>();
		chunksStored= new ConcurrentHashMap<>();
		repDegreePerFile=new ConcurrentHashMap<>();
		peersContainingChunks=new ConcurrentHashMap<>();

		Peer peer = new Peer();
		try {
			RMI_inteface stub = (RMI_inteface) UnicastRemoteObject.exportObject(peer, 0);

			LocateRegistry.getRegistry().rebind(args[0], stub);
		} catch (RemoteException e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}

		mcDispatcher = new MC_Dispatcher(ports[0], adresses[0]);
		mdbDispatcher = new MDB_Dispatcher(ports[1], adresses[1]);
		// faltam os outros

		new Thread(mcDispatcher).start();
		new Thread(mdbDispatcher).start();
		// falta iniciar as outras threads

	}

	public static MC_Dispatcher getMcDispacther() {
		return mcDispatcher;
	}

	public static MDB_Dispatcher getMDBDispacther() {
		return mdbDispatcher;
	}

	public static void saveChunk(String file_ID, int chunkNO, int replication_degree, byte[] body) {
		if (savedChunks.containsKey(file_ID) == false) {
			System.out.println("Does not contain the file yet");
			savedChunks.put(file_ID, new ArrayList<>());
			repDegreePerFile.put(file_ID, replication_degree);
			repDegreeAtual.put(file_ID, new ConcurrentHashMap<>());

		}
		savedChunks.get(file_ID).add(chunkNO);
		if (!repDegreeAtual.get(file_ID).containsKey(chunkNO)) {
			System.out.println("saving on redDegree \n\n");
			repDegreeAtual.get(file_ID).put(chunkNO, new ArrayList<>());
			
		}

		repDegreeAtual.get(file_ID).get(chunkNO).add(Peer.getID());
		
		System.out.println("ja ta em:"+repDegreeAtual.get(file_ID).get(chunkNO).size());

		used_space += body.length;

		File chunkFile = new File("chunksDir/", file_ID + "_" + chunkNO);
		chunkFile.getParentFile().mkdirs();
		try {
			FileOutputStream out = new FileOutputStream(chunkFile);
			out.write(body);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Chunk Saved");
	}

}
