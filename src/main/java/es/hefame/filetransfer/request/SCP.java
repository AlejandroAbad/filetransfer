package es.hefame.filetransfer.request;


import org.apache.commons.vfs2.FileSystemException;

import es.hefame.filetransfer.getopts.CliParams;

public class SCP extends TransferRequest {

	public SCP(CliParams params) {
		super(params);
	}


	@Override
	public void upload() throws FileSystemException {
		//
	}

	@Override
	public void download() throws FileSystemException {
		//
	}



	
}
