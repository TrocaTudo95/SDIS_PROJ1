package utils;

import java.net.DatagramPacket;

public class PacketHandler implements Runnable {

	private DatagramPacket packet;
	private String header;
	private byte[] body;
	
	public PacketHandler(DatagramPacket packet) {
		this.packet = packet;
	}
	

	
	@Override
	public void run() {
		
		
	}

}
