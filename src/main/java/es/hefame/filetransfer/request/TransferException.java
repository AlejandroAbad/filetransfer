package es.hefame.filetransfer.request;

public class TransferException extends Exception {

	private static final long serialVersionUID = 7616263417365340689L;

	public TransferException(Throwable cause) {
		super(cause);
	}

	@Override
	public String getMessage() {
		return this.getCause().getMessage();
	}
	
}
