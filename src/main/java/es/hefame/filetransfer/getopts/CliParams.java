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
	private String comment;
	private Map<String, Object> extraParameters;

	public CliParams(String[] cliArgs, TransferProtocol transferProtocol, TransferDirection direction, String remoteHost, int remotePort,
			String username, String password, String sourceFile, String destination, String comment, Map<String, Object> extraParameters) {
		this.cliArgs = cliArgs;
		this.transferProtocol = transferProtocol;
		this.direction = direction;
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
		this.username = username;
		this.password = password;
		this.sourceFile = sourceFile;
		this.destination = destination;
		this.comment = comment;
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

	public String getComment() {
		return comment;
	}

	public Map<String, Object> getExtraParameters() {
		return this.extraParameters;
	}

	public void setCliArgs(String[] cliArgs) {
		this.cliArgs = cliArgs;
	}

	public void setTransferProtocol(TransferProtocol transferProtocol) {
		this.transferProtocol = transferProtocol;
	}

	public void setDirection(TransferDirection direction) {
		this.direction = direction;
	}

	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
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

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setExtraParameters(Map<String, Object> extraParameters) {
		this.extraParameters = extraParameters;
	}

	@Override
	public String toString() {
		return "CliParams [destination=" + destination + ", direction=" + direction + ", password=*******"
				+ ", remoteHost=" + remoteHost + ", remotePort=" + remotePort + ", sourceFile=" + sourceFile
				+ ", transferProtocol=" + transferProtocol + ", username=" + username + "]";
	}

}
