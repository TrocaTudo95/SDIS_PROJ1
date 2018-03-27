package utils;

import java.io.File;

public class Backup implements Runnable {
	
	private File file;
	private int replicationDegree;

	public Backup(File file, int replicationDegree) {
		this.file = file;
		this.replicationDegree = replicationDegree;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		//build Backup
		
	}

}
