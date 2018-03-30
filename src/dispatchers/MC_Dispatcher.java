package dispatchers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.HashMap;

import chunks.Chunk;
import utils.PacketHandler;

public class MC_Dispatcher implements Runnable {
	
	
	public static MulticastSocket mc_socket;

	public InetAddress mc_address;
	public int mc_port;
	private volatile HashMap<Chunk, ArrayList<Integer>> chunksStored;
	public static final int CHUNK_SIZE =64000;

	
	
	public MC_Dispatcher(int mc_port,InetAddress mc_address) {
		this.mc_port=mc_port;
		this.mc_address=mc_address;
	}
	
	public void connect_to_multicast_socket() {
		try {
			mc_socket = new MulticastSocket(mc_port);

			mc_socket.setTimeToLive(1);
			mc_socket.joinGroup(mc_address);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void SaveChunksStored(Chunk chunk) {
		if (!chunksStored.containsKey(chunk)) {
			chunksStored.put(chunk, new ArrayList<Integer>());
		}
	}

	public  void clearChunksStored(Chunk chunk) {
		chunksStored.get(chunk).clear();
	}

	public  int getChunksStored(Chunk chunk) {
		return chunksStored.get(chunk).size();
	}

	public  void stopSavingChunksStored(Chunk chunk) {
		chunksStored.remove(chunk);
	}

	@Override
	public void run() {
		byte[] buffer= new byte[CHUNK_SIZE];
		connect_to_multicast_socket();
		
		
		while(true) {
			DatagramPacket mc_packet = new DatagramPacket(buffer, buffer.length);
			try {
				mc_socket.receive(mc_packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//missing verification to see if the peers are the same
			
			new Thread(new PacketHandler(mc_packet)).start();
		} 	
		
		
	}

}
	