package Peer;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.io.File;
import dispatchers.MC_Dispatcher;
import dispatchers.MDB_Dispatcher;
import rmi.RMI_inteface;
import utils.Services;
import chunks.Chunk;

public class Peer implements RMI_inteface{
	
	private static int ID;
	private static Services services;
	public static ConcurrentHashMap<String, ConcurrentHashMap<Integer, ArrayList<Integer>>> repDegreeAtual;
	private static MC_Dispatcher mcDispatcher;
	private static MDB_Dispatcher mdbDispatcher;
	//public static ConcurrentHashMap<String, ArrayList<Integer>> savedChunks;
	public static ConcurrentHashMap<String, ArrayList<Chunk>>savedChunks;
	public static int MEMORY= 10000000;
	private static int used_space=0;

	@Override
	public void backup_file(File file, int replicationDegree) throws RemoteException {
		String File_ID;
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
	            return 0;
	        }
	    }

	
	public static void main(String[] args) throws UnknownHostException {
		InetAddress[] adresses= new InetAddress[3];
		int[] ports=new int[] {8000,8001,8002};
		adresses[0]=InetAddress.getByName("224.0.0.0");
		adresses[1]=InetAddress.getByName("224.0.0.0");
		adresses[2]=InetAddress.getByName("224.0.0.0");
		savedChunks=new ConcurrentHashMap<>();
		
		Peer peer=new Peer();
		try {
			RMI_inteface stub = (RMI_inteface) UnicastRemoteObject
					.exportObject(peer, 0);

			LocateRegistry.getRegistry().rebind(args[0], stub);
		} catch (RemoteException e) {
			System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
		}
		
		mcDispatcher= new MC_Dispatcher(ports[0],adresses[0]);
		//faltam os outros
		
		new Thread(mcDispatcher).start();
		//falta iniciar as outras threads
		
		
	}

	public static MC_Dispatcher getMcDispacther() {
		return mcDispatcher;
	}
	
	public static MDB_Dispatcher getMDBDispacther() {
		return mdbDispatcher;
	}

	public static void saveChunk(String string, Chunk chunk) {
		if(savedChunks.containsKey(string)) {
			savedChunks.get(string).add(chunk);
		}
		else
			savedChunks.put(string,new ArrayList<Chunk>());
		
	}
	
	
	

}
