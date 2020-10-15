package es.hefame.filetransfer.getopts;

public class CliParseException extends Exception {

	private static final long serialVersionUID = -257130784825745617L;

	public CliParseException(String message) {
		super(message);
	}

	public CliParseException(String message, Throwable cause) {
		super(message, cause);
	}
	
	
}
