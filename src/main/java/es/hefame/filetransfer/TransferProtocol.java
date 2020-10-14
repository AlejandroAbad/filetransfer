package es.hefame.filetransfer;


import java.lang.reflect.InvocationTargetException;

import es.hefame.filetransfer.getopts.CliParams;
import es.hefame.filetransfer.request.*;

public enum TransferProtocol {
	SFTP(SFTP.class), FTPS(FTPS.class), SCP(SCP.class);

	private Class<? extends TransferRequest> protocolClass;

	private TransferProtocol(Class<? extends TransferRequest> className) {
		this.protocolClass = className;
	}

	public TransferRequest instanciate(CliParams parameters) {

		try {
			return this.protocolClass.getConstructor(CliParams.class).newInstance(parameters);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}

	}
	
}
