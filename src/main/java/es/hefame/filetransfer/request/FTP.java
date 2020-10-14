package es.hefame.filetransfer.request;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;

import es.hefame.filetransfer.TransferDirection;
import es.hefame.filetransfer.getopts.CliParams;

public class FTP extends TransferRequest {

	public FTP(CliParams params) {
		super(params);
	}

	private FileSystemOptions getFileSystemOptions() {
		FileSystemOptions opts = new FileSystemOptions();
		//FtpsFileSystemConfigBuilder.getInstance().setFtpsMode(opts, FtpsMode.EXPLICIT);
		
		FtpFileSystemConfigBuilder.getInstance().setPassiveMode(opts, true);
		FtpFileSystemConfigBuilder.getInstance().setConnectTimeout(opts, 10000);
		return opts;
	}

	private String getRemotePath() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.params.getTransferProtocol().name().toLowerCase()).append("://").append(params.getUsername())
				.append(":").append(params.getPassword()).append("@").append(params.getRemoteHost());

		if (params.getDirection() == TransferDirection.UPLOAD) {
			sb.append(params.getDestination());
		} else {
			sb.append(params.getSourceFile());
		}

		return sb.toString();
	}

	@Override
	public void upload() throws FileSystemException {

		FileSystemManager manager = VFS.getManager();
		FileSystemOptions opts = this.getFileSystemOptions();

		FileObject local = manager.resolveFile(params.getSourceFile());
		FileObject remote = manager.resolveFile(this.getRemotePath(), opts);

		remote.copyFrom(local, Selectors.SELECT_SELF);

	}

	@Override
	public void download() throws FileSystemException {

		FileSystemManager manager = VFS.getManager();
		FileSystemOptions opts = this.getFileSystemOptions();

		FileObject local = manager.resolveFile(params.getDestination());
		FileObject remote = manager.resolveFile(this.getRemotePath(), opts);

		local.copyFrom(remote, Selectors.SELECT_SELF);
		
	}

}
