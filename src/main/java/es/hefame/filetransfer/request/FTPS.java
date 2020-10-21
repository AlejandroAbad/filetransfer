package es.hefame.filetransfer.request;

import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.ftps.FtpsFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.ftps.FtpsMode;

import es.hefame.filetransfer.getopts.CliParams;

public class FTPS extends VSFTransfer {

	public FTPS(CliParams params) {
		super(params);
	}

	protected FileSystemOptions getFileSystemOptions() {
		FileSystemOptions opts = new FileSystemOptions();
		FtpsFileSystemConfigBuilder.getInstance().setFtpsMode(opts, FtpsMode.EXPLICIT);
		FtpsFileSystemConfigBuilder.getInstance().setPassiveMode(opts, true);
		FtpsFileSystemConfigBuilder.getInstance().setConnectTimeout(opts, 10000);
		return opts;
	}

}
