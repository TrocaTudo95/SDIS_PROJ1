package utils;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceX extends Remote {

	void Filebackup(File file, int replicationDegree) throws RemoteException;

	void Filerestore(File file) throws RemoteException;

	void Filedelete(File file) throws RemoteException;

	void spaceReclaim(int amount) throws RemoteException;

}