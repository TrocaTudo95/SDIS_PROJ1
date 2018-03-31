

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.io.File;


public class TestClient {
	private static RMI_inteface initiatorPeer;
	private static String stub_name;
	private static File file;
	private static int replication_degree;
	
	
	public static void main(String[] args) throws IOException{
		process_arguments(args);
		
//connect to RMI
		try {
			Registry registry = LocateRegistry.getRegistry();

			initiatorPeer =  (RMI_inteface) registry.lookup(stub_name);

		} catch (NotBoundException |RemoteException e) {
			System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
		
	}
		String protocol = args[1].toUpperCase();
		System.out.println(protocol);
		switch (protocol) {
		case"BACKUP":
			System.out.println("Backup");
			initiatorPeer.backup_file(file, replication_degree);
			break;
		case "DELETE":
			System.out.println("Delete");
			initiatorPeer.delete_file(file);
			break;
		}
	}


	private static boolean process_arguments(String[] args) {
		if (args.length<2)
			return false;
			
			
			stub_name=args[0];
			//protocol=args[1];
			String file_name=args[2];
			
			file = new File(file_name);
			
			replication_degree=Integer.parseInt(args[3]);
			
			
		//faltam verificacoes
			
		
		
		
		return true;
		
		
	}

}
