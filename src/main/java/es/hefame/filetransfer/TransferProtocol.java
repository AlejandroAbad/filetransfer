package es.hefame.filetransfer;


import java.lang.reflect.InvocationTargetException;

import es.hefame.filetransfer.getopts.CliParams;
import es.hefame.filetransfer.request.*;

public enum TransferProtocol {
	FTP(FTP.class), SFTP(SFTP.class), FTPS(FTPS.class), SCP(SCP.class), SMB(SMB.class);

	private Class<? extends TransferRequest> protocolClass;

	private TransferProtocol(Class<? extends TransferRequest> className) {
		this.protocolClass = className;
	}

	public TransferRequest instanciate(CliParams parameters) throws InstantiationException {

		try {
			return this.protocolClass.getConstructor(CliParams.class).newInstance(parameters);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new InstantiationException("El tipo de transferencia " + this.name() + " no tiene clase asociada");
		}

	}

	public static TransferProtocol fromName(String name) {

		for (TransferProtocol tp : TransferProtocol.values() ) {
			if (tp.name().equalsIgnoreCase(name)) {
				return tp;
			}
		}

		return null;
	} 
	
}
