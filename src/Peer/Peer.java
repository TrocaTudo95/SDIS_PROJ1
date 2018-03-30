package Peer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import java.io.File;
import dispatchers.MC_Dispatcher;
import rmi.RMI_inteface;

public class Peer implements RMI_inteface{
	
	private static int ID;
	private static MC_Dispatcher mcdispatcher;
	
	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		Peer.ID = ID;
	}

	@Override
	public void backup_file(File file, int replicationDegree) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) throws UnknownHostException {
		InetAddress[] adresses= new InetAddress[3];
		int[] ports=new int[] {8000,8001,8002};
		adresses[0]=InetAddress.getByName("224.0.0.0");
		adresses[1]=InetAddress.getByName("224.0.0.0");
		adresses[2]=InetAddress.getByName("224.0.0.0");
		
		Peer peer=new Peer();
		try {
			RMI_inteface stub = (RMI_inteface) UnicastRemoteObject
					.exportObject(peer, 0);

			LocateRegistry.getRegistry().rebind(args[0], stub);
		} catch (RemoteException e) {
			System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
		}
		
		mcdispatcher= new MC_Dispatcher(ports[0],adresses[0]);
		//faltam os outros
		
		new Thread(mcdispatcher).start();
		//falta iniciar as outras threads
		
		
	}
	

}
