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

	public void setTransferProtocol(String transferProtocolName) throws CliParseException {

		transferProtocolName = transferProtocolName.toUpperCase();
		this.transferProtocol = TransferProtocol.fromName(transferProtocolName);

		if (this.transferProtocol == null) {
			throw new CliParseException("No se reconoce el protocolo " + transferProtocolName);
		}

	}

	public void setTransferDirection(String directionName) throws CliParseException {

		directionName = directionName.toUpperCase();
		this.direction = TransferDirection.fromName(directionName);

		if (this.direction == null) {
			throw new CliParseException("No se reconoce el sentido de la comunicación. Indique UPLOAD o DOWNLOAD");
		}

	}

	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	public void setRemotePort(String remotePortString) throws CliParseException {

		try {
			this.remotePort = Integer.parseInt(remotePortString);

			if (this.remotePort < 1 || this.remotePort > 65535) {
				throw new CliParseException("Indique un número de puerto en el rango 1-65535");
			}

		} catch (NumberFormatException e) {
			throw new CliParseException("Indique un número de puerto válido");
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

	public CliParams build() throws CliParseException {

		if (transferProtocol == null)
			throw new CliParseException("No se ha especificado el protocolo de transferencia");
		if (remoteHost == null)
			throw new CliParseException("No se ha especificado el host remoto de la transferencia");
		if (sourceFile == null)
			throw new CliParseException("No se ha especificado la ruta del fichero origen");
		if (destination == null)
			throw new CliParseException("No se ha especificado la ruta del fichero de destino");

		if (direction == null)
			direction = TransferDirection.UPLOAD;

		return new CliParams(transferProtocol, direction, remoteHost, remotePort, username, password, sourceFile,
				destination, null);
	}


}
