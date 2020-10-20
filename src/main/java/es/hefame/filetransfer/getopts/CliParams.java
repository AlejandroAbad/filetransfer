package es.hefame.filetransfer.getopts;

import java.io.File;
import java.util.Map;

import es.hefame.filetransfer.TransferDirection;
import es.hefame.filetransfer.TransferProtocol;

public class CliParams {

	private String[] cliArgs;
	private TransferProtocol transferProtocol;
	private TransferDirection direction;
	private String remoteHost;
	private int remotePort;
	private String username;
	private String password;
	private String sourceFile;
	private String destination;
	private Map<String, Object> extraParameters;

	public CliParams(String[] cliArgs, TransferProtocol transferProtocol, TransferDirection direction, String remoteHost, int remotePort,
			String username, String password, String sourceFile, String destination, Map<String, Object> extraParameters) {
		this.cliArgs = cliArgs;
		this.transferProtocol = transferProtocol;
		this.direction = direction;
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
		this.username = username;
		this.password = password;
		this.sourceFile = sourceFile;
		this.destination = destination;
		this.extraParameters = extraParameters;
	}

	public String[] getCliArgs() {
		return this.cliArgs;
	}

	public TransferProtocol getTransferProtocol() {
		return transferProtocol;
	}

	public TransferDirection getDirection() {
		return direction;
	}

	public String getRemoteHost() {
		return remoteHost;
	}

	public int getRemotePort() {
		return remotePort;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getSourceFile() {
		return sourceFile;
	}

	public String getSourceFileName() {
		File f = new File(this.sourceFile);
		return f.getName();
	}

	public String getSourceFilePath() {
		File f = new File(this.sourceFile);
		return f.getPath();
	}

	public String getDestination() {
		return destination;
	}

	public Map<String, Object> getExtraParameters() {
		return this.extraParameters;
	}

	@Override
	public String toString() {
		return "CliParams [destination=" + destination + ", direction=" + direction + ", password=*******"
				+ ", remoteHost=" + remoteHost + ", remotePort=" + remotePort + ", sourceFile=" + sourceFile
				+ ", transferProtocol=" + transferProtocol + ", username=" + username + "]";
	}

}
