package es.hefame.filetransfer;

public enum TransferDirection {
	UPLOAD, DOWNLOAD;


	public static TransferDirection fromName(String name) {

		for (TransferDirection td : TransferDirection.values() ) {
			if (td.name().equalsIgnoreCase(name)) {
				return td;
			}
		}

		return null;
	} 
}
