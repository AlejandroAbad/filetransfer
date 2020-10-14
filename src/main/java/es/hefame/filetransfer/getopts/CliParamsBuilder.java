package es.hefame.filetransfer.getopts;

import es.hefame.filetransfer.TransferDirection;
import es.hefame.filetransfer.TransferProtocol;

class CliParamsBuilder {

	private TransferProtocol transferProtocol;
	private TransferDirection direction;
	private String remoteHost;
	private int remotePort = 0;
	private String username;
	private String password;
	private String sourceFile;
	private String destination;

	public CliParamsBuilder() {
		//
	}

	public void setTransferProtocol(String transferProtocolName) {

		transferProtocolName = transferProtocolName.toUpperCase();
		this.transferProtocol = TransferProtocol.valueOf(transferProtocolName);

		if (this.transferProtocol == null) {
			System.err.println("No se reconoce el protocolo " + transferProtocolName);
			System.exit(1);	
		}

	}

	public void setTransferDirection(String directionName) {

		directionName = directionName.toUpperCase();
		this.direction = TransferDirection.valueOf(directionName);

		if (this.direction == null) {
			System.err.println("No se reconoce el sentido de la comunicación " + directionName + ". Indique UPLOAD o DOWNLOAD");
			System.exit(1);
		}

	}

	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	public void setRemotePort(String remotePortString) {

		try {
			this.remotePort = Integer.parseInt(remotePortString);

			if (this.remotePort < 1 || this.remotePort > 65535) {
				System.err.println("Indique un número de puerto en el rango 1-65535");
				System.exit(1);	
			}


		} catch (NumberFormatException e) {
			System.err.println("Indique un número de puerto válido");
			System.exit(1);
		}


		
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public CliParams build() throws IllegalStateException {

		if (transferProtocol == null)
			throw new IllegalStateException("No se ha especificado el protocolo de transferencia");
		if (remoteHost == null)
			throw new IllegalStateException("No se ha especificado el host remoto de la transferencia");
		if (sourceFile == null)
			throw new IllegalStateException("No se ha especificado la ruta del fichero origen");
		if (destination == null)
			throw new IllegalStateException("No se ha especificado la ruta del fichero de destino");


		if (direction == null)
			direction = TransferDirection.UPLOAD;

		return new CliParams(transferProtocol, direction, remoteHost, remotePort, username, password, sourceFile, destination, null);
	}

}
