package memory;

import java.io.Serializable;

import Peer.Peer;

public class Disks implements Serializable {
	
	private int capacity;
	private int capacity_used;
	private int freeSpace;
	
	public Disks(){
		this.capacity = 10000000; //10MB
		this.capacity_used = 0;
		this.freeSpace = this.capacity - this.capacity_used;
		
	}
	
	public int getCapacity() {
		return this.capacity;
	}
	public int getCapacityUsed() {
		return this.capacity_used;
	}
	public int getFreeSpace() {
		return this.freeSpace;
	}
	public void setCapacity(int Capacity) {
		this.capacity = Capacity;
		Peer.saveDisks();
		printDisk();
	}
	public void addCapacity(int bytes) {
		this.capacity += bytes;
		Peer.saveDisks();
		printDisk();
	}
	public void save(int bytes) {
		if (bytes > this.freeSpace) {
			System.out.println("Insuficient Space to Save");
		} else {
			this.capacity_used += bytes;
			Peer.saveDisks();
			printDisk();
		}
	}
	public void remove(int bytes) {
		this.capacity_used -= bytes;
		Peer.saveDisks();
		printDisk();
	}
	
	public void printDisk() {
		System.out.println("DISK");
		System.out.println("Capacity: "+this.capacity);
		System.out.println("Free Space: "+this.freeSpace);
	}
	

}
