package es.hefame.filetransfer.request;

import es.hefame.filetransfer.getopts.CliParams;

public abstract class TransferRequest {

	protected CliParams params;
	private long transferredBytes = -1;
	private long timeElapsed = -1;

	protected void setTimeElapsed(long timeElapsed) {
		this.timeElapsed = timeElapsed;
	}

	protected void setTransferredBytes(long transferredBytes) {
		this.transferredBytes = transferredBytes;
	}

	public long getTransferredBytes() {
		return this.transferredBytes;
	}

	public long getTimeElapsed() {
		return this.timeElapsed;
	}

	public CliParams getCliParams() {
		return this.params;
	}

	public TransferRequest(CliParams params) {
		this.params = params;
	}

	public final void transfer() throws TransferException {

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



	public abstract void upload() throws TransferException;
	
	public abstract void download() throws TransferException;


}
