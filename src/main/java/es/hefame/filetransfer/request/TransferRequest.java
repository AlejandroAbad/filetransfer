package es.hefame.filetransfer.request;

import org.apache.commons.vfs2.FileSystemException;

import es.hefame.filetransfer.getopts.CliParams;

public abstract class TransferRequest {

	protected CliParams params;

	public TransferRequest(CliParams params) {
		this.params = params;
	}

	public final void transfer() throws FileSystemException {

		switch(params.getDirection()) {
			case UPLOAD:
				this.upload();
				return;
			case DOWNLOAD:
				this.download();
				return;
			default:
				throw new IllegalArgumentException("No se reconoce el parámetro direction. Este debe ser uno de UPLOAD o DOWNLOAD");
		}

	}



	public abstract void upload() throws FileSystemException;
	
	public abstract void download() throws FileSystemException;


}
