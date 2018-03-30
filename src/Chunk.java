

import java.util.ArrayList;


public class Chunk {
	public static final int SIZE = 64000;
	private static final long serialVersionUID = 1L;
	private int repDegree;
	private byte[] dados;
	private String fileId;
	private int chunkNo;
	private ArrayList<Integer> peers;

	public Chunk(String fileId, int chunkNo, int repDegree, byte[] dados) {
		this.fileId = fileId;
		this.chunkNo = chunkNo;
		this.repDegree = repDegree;
		this.dados = dados;
	}

	public String getFileID() {
		return this.fileId;
	}
	public int getChunkNo() {
		return this.chunkNo;
	}
	public ArrayList<Integer> getPeers(){
		return this.peers;
	}
	public void removePeer(int ID) {
		peers.remove(ID);
	}
	public void setPeers(ArrayList<Integer> peers) {
		this.peers = peers;
	}


	public int getRepDegree() {
		return this.repDegree;
	}

	public byte[] getDados() {
		return this.dados;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.chunkNo;
		result = prime * result + ((fileId == null) ? 0 : this.fileId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chunk other = (Chunk) obj;
		if (this.chunkNo != other.chunkNo)
			return false;
		if (this.fileId == null) {
			if (other.fileId != null)
				return false;
		} else if (!this.fileId.equals(other.fileId))
			return false;
		return true;
	}
	


}
