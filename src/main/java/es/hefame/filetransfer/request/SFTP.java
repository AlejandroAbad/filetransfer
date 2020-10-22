package es.hefame.filetransfer.request;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import java.io.File;
import org.apache.commons.vfs2.provider.sftp.IdentityInfo;
import org.apache.commons.vfs2.provider.sftp.IdentityProvider;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

import es.hefame.filetransfer.getopts.CliParams;

public class SFTP extends VFSTransfer {

	public SFTP(CliParams params) {
		super(params);
	}

	@Override
	protected FileSystemOptions getFileSystemOptions() throws FileSystemException {

		FileSystemOptions opts = new FileSystemOptions();

		// 
		File sshKeyFile = new File( System.getProperty("user.home") + "/.ssh/id_rsa");
		if (sshKeyFile.exists()) {
			IdentityProvider ip = new IdentityInfo(sshKeyFile);
			SftpFileSystemConfigBuilder.getInstance().setIdentityProvider(opts, ip);
		}

		SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
		SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);
		SftpFileSystemConfigBuilder.getInstance().setSessionTimeoutMillis(opts, 10000);

		return opts;
	}

}
