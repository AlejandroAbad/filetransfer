package es.hefame.filetransfer.request;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.auth.StaticUserAuthenticator;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;

import es.hefame.filetransfer.TransferDirection;
import es.hefame.filetransfer.getopts.CliParams;

public class CIFS extends TransferRequest {

	public CIFS(CliParams params) {
		super(params);
	}

	private FileSystemOptions getFileSystemOptions() throws FileSystemException {

		String domain = "";
		String username = params.getUsername();
		String[] userParts = username.split("\\\\");
		if (userParts.length == 2) {
			System.out.println(userParts[0] + " ---- " + userParts[1]);
			domain = userParts[0];
			username = userParts[1];
		}

		StaticUserAuthenticator auth = new StaticUserAuthenticator(domain, username, params.getPassword());
		FileSystemOptions opts = new FileSystemOptions();
		DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);
		return opts;
	}

	private String getRemotePath() {
		StringBuilder sb = new StringBuilder();

		sb.append(this.params.getTransferProtocol().name().toLowerCase()).append("://").append(params.getRemoteHost());

		if (params.getDirection() == TransferDirection.UPLOAD) {
			sb.append(params.getDestination());
		} else {
			sb.append(params.getSourceFile());
		}

		System.out.println(sb.toString());

		return sb.toString();
	}

	@Override
	public void upload() throws TransferException {
		try {
			FileSystemManager manager = VFS.getManager();
			FileSystemOptions opts = this.getFileSystemOptions();

			FileObject local = manager.resolveFile(params.getSourceFile());
			FileObject remote = manager.resolveFile(this.getRemotePath(), opts);

			remote.copyFrom(local, Selectors.SELECT_SELF);
		} catch (Exception e) {
			throw new TransferException(e);
		}

	}

	@Override
	public void download() throws TransferException {
		try {
			FileSystemManager manager = VFS.getManager();
			FileSystemOptions opts = this.getFileSystemOptions();

			FileObject local = manager.resolveFile(params.getDestination());
			FileObject remote = manager.resolveFile(this.getRemotePath(), opts);

			local.copyFrom(remote, Selectors.SELECT_SELF);
		} catch (Exception e) {
			throw new TransferException(e);
		}

	}

}
