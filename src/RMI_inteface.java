

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.File;


public interface RMI_inteface extends Remote{

	void backup_file(File file, int replicationDegree) throws RemoteException;
	
	//void delete_file(File file) throws RemoteException;
}
