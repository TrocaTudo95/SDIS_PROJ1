package utils;

import java.io.File;
import java.rmi.Remote;

public class Interface implements Remote {

	void Filebackup(File file, int replicationDegree) {
		new Thread(new Backup(file, replicationDegree)).start();
	}

	void Filerestore(File file) {
		
	}

	void Filedelete(File file) {
	
	}

	void spaceReclaim(int amount) {
		
	}
}