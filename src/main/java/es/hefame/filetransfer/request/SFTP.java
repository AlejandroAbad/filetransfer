package es.hefame.filetransfer.request;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

import es.hefame.filetransfer.getopts.CliParams;

public class SFTP extends VSFTransfer {

	public SFTP(CliParams params) {
		super(params);
	}

	@Override
	protected FileSystemOptions getFileSystemOptions() throws FileSystemException {

		FileSystemOptions opts = new FileSystemOptions();
		SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
		SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);
		SftpFileSystemConfigBuilder.getInstance().setSessionTimeoutMillis(opts, 10000);

		return opts;
	}

}
