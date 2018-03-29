package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import chunks.File;

public interface RMI_inteface extends Remote{

	void backup_file(File file, int replicationDegree) throws RemoteException;
}
