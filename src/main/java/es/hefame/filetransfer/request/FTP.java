package es.hefame.filetransfer.request;

import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;

import es.hefame.filetransfer.getopts.CliParams;

public class FTP extends VSFTransfer {

	public FTP(CliParams params) {
		super(params);
	}

	protected FileSystemOptions getFileSystemOptions() {
		FileSystemOptions opts = new FileSystemOptions();
		// FtpsFileSystemConfigBuilder.getInstance().setFtpsMode(opts,
		// FtpsMode.EXPLICIT);

		FtpFileSystemConfigBuilder.getInstance().setPassiveMode(opts, true);
		FtpFileSystemConfigBuilder.getInstance().setConnectTimeout(opts, 10000);
		return opts;
	}

}
