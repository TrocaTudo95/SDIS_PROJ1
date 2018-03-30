package client;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.io.File;
import rmi.RMI_inteface;

public class TestClient {
	private static RMI_inteface initiatorPeer;
	private static String stub_name;
	private static String protocol;
	private static File file;
	private static int replication_degree;
	
	
	public static void main(String[] args) throws IOException{
		process_arguments(args);
		
//connect to RMI
		try {
			Registry registry = LocateRegistry.getRegistry("localhost");

			initiatorPeer =  (RMI_inteface) registry.lookup(stub_name);

		} catch (NotBoundException |RemoteException e) {
			System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
		
	}
		
		if(protocol=="BACKUP") {
			initiatorPeer.backup_file(file, replication_degree);
		}
	}


	private static boolean process_arguments(String[] args) {
		if (args.length<2)
			return false;
			
			
			stub_name=args[0];
			protocol=args[1];
			String file_name=args[2];
			
			file = new File(file_name);
			
			replication_degree=Integer.parseInt(args[3]);
			
			
		//faltam verificacoes
			
		
		
		
		return true;
		
		
	}

}
