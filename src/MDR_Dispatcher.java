import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MDR_Dispatcher implements Runnable {
	
	public static MulticastSocket mdr_socket;

	public InetAddress mdr_address;
	public int mdr_port;
	public static final int CHUNK_SIZE =64000;
	
	public MDR_Dispatcher(int mdr_port,InetAddress mdr_address) {
		this.mdr_port=mdr_port;
		this.mdr_address=mdr_address;
	}
	
	public void connect_to_multicast_socket() {
		try {
			mdr_socket = new MulticastSocket(mdr_port);

			mdr_socket.setTimeToLive(1);
			mdr_socket.joinGroup(mdr_address);
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
			DatagramPacket mdr_packet = new DatagramPacket(buffer, buffer.length);
			try {
				mdr_socket.receive(mdr_packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("received packet");
			new Thread(new PacketHandler(mdr_packet)).start();
		} 	
		
		
		
	}


}
