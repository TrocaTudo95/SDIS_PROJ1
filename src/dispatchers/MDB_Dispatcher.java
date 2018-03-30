	package dispatchers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.HashMap;

import utils.PacketHandler;

public class MDB_Dispatcher implements Runnable

{
	public static MulticastSocket mdb_socket;

	public InetAddress mdb_address;
	public int mdb_port;
	//private volatile HashMap<Integer, ArrayList<Integer>> chunksStored;
	public static final int CHUNK_SIZE =64000;

	
	
	public MDB_Dispatcher(int mdb_port,InetAddress mdbS_address) {
		this.mdb_port=mdb_port;
		this.mdb_address=mdb_address;
	}

	
	public void connect_to_multicast_socket() {
		try {
			mdb_socket = new MulticastSocket(mdb_port);

			mdb_socket.setTimeToLive(1);
			mdb_socket.joinGroup(mdb_address);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		byte[] buffer= new byte[CHUNK_SIZE];
		connect_to_multicast_socket();
		
		
		while(true) {
			DatagramPacket mdb_packet = new DatagramPacket(buffer, buffer.length);
			try {
				mdb_socket.receive(mdb_packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//missing verification to see if the peers are the same
			
			new Thread(new PacketHandler(mdb_packet)).start();
		} 	
		
		
		
	}

}
