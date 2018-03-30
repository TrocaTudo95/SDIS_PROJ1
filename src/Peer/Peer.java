package Peer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import java.io.File;
import dispatchers.MC_Dispatcher;
import memory.Disks;
import rmi.RMI_inteface;
import utils.Services;

public class Peer implements RMI_inteface{
	
	private static int ID;
	private static Disks disk;
	private static Services services;
	private static MC_Dispatcher mcDispatcher;

	@Override
	public void backup_file(File file, int replicationDegree) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}
	
	public static Services getServices() {
		return services;
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
		
		mcDispatcher= new MC_Dispatcher(ports[0],adresses[0]);
		//faltam os outros
		
		new Thread(mcDispatcher).start();
		//falta iniciar as outras threads
		
		
	}
	private static void createDisk() {
		disk = new Disks();
		saveDisks();
		System.out.println("A new disk has been created");
	}
	
	private static void loadDisks() throws ClassNotFoundException, IOException {
		try {
			FileInputStream FileInput= new FileInputStream("disks.data");
			ObjectInputStream ObjectInput = new ObjectInputStream(FileInput);
			disk = (Disks) ObjectInput.readObject();
			ObjectInput.close();
		} catch (FileNotFoundException e) {
			createDisk();
			System.out.println("Disk not found");
		}
	}
	
	public static void saveDisks() {
		try {
			FileOutputStream FileOutput = new FileOutputStream("disks.data");
			ObjectOutputStream ObjectOutput = new ObjectOutputStream(FileOutput);
			ObjectOutput.writeObject(disk);
			ObjectOutput.close();
		} catch (FileNotFoundException e) {
			createDisk();
			System.out.println("Disk Not Found");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static MC_Dispatcher getMcDispacther() {
		return mcDispatcher;
	}
	
	
	

}
