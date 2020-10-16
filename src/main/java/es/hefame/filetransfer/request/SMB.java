package es.hefame.filetransfer.request;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.msfscc.FileAttributes;
import com.hierynomus.msfscc.fileinformation.FileStandardInformation;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2CreateOptions;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;
import com.hierynomus.utils.Strings;

import es.hefame.filetransfer.getopts.CliParams;

public class SMB extends TransferRequest {

	public SMB(CliParams params) {
		super(params);
	}

	private String getShareName(String path) {

		String[] pathChunks = path.split("/");

		for (int i = 0; i < pathChunks.length; i++) {
			if (pathChunks[i].length() > 0)
				return pathChunks[i];
		}

		throw new IllegalArgumentException("La ruta remota no contiene el nombre del share");
	}

	private String getRemoteFileName(String path) {

		String[] pathChunks = path.split("/");
		boolean shareFound = false;

		for (int i = 0; i < pathChunks.length; i++) {
			if (pathChunks[i].length() > 0) {
				if (shareFound) {
					String[] copy = Arrays.copyOfRange(pathChunks, i, pathChunks.length);
					return Strings.join(Arrays.asList(copy), '/');
				} else {
					shareFound = true;
				}
			}
		}

		if (shareFound)
			return "";
		throw new IllegalArgumentException("La ruta remota no contiene el nombre del share");
	}

	private SmbConfig getConnectionConfig() {
		return SmbConfig.builder()
				.withTimeout(60, TimeUnit.SECONDS)
				.withSoTimeout(90, TimeUnit.SECONDS)
				.build();
	}

	private Session getSession(Connection connection) {
		String domain = "";
		String username = params.getUsername();
		String[] userParts = username.split("\\\\");

		if (userParts.length == 2) {
			domain = userParts[0];
			username = userParts[1];
		}
		AuthenticationContext authContext = new AuthenticationContext(username, params.getPassword().toCharArray(), domain);

		return connection.authenticate(authContext);
	}

	private File openRemoteFileForWrite(DiskShare share) {
		String remoteFilePath = getRemoteFileName(params.getDestination());

		// ACCESS MASK
		HashSet<AccessMask> accessMask = new HashSet<>();
		accessMask.add(AccessMask.GENERIC_ALL);

		// FILE ATTRIBUTES
		Set<FileAttributes> fileAttributes = new HashSet<>();
		fileAttributes.add(FileAttributes.FILE_ATTRIBUTE_NORMAL);

		// CREATE OPTIONS
		Set<SMB2CreateOptions> createOptions = new HashSet<>();
		createOptions.add(SMB2CreateOptions.FILE_RANDOM_ACCESS);

		return share.openFile(remoteFilePath, accessMask, fileAttributes, SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_OVERWRITE_IF, createOptions);
	}

	private File openRemoteFileForRead(DiskShare share) {
		String remoteFilePath = getRemoteFileName(params.getSourceFile());

		// ACCESS MASK
		HashSet<AccessMask> accessMask = new HashSet<>();
		accessMask.add(AccessMask.GENERIC_READ);

		// FILE ATTRIBUTES
		Set<FileAttributes> fileAttributes = new HashSet<>();
		fileAttributes.add(FileAttributes.FILE_ATTRIBUTE_NORMAL);

		// CREATE OPTIONS
		Set<SMB2CreateOptions> createOptions = new HashSet<>();
		createOptions.add(SMB2CreateOptions.FILE_RANDOM_ACCESS);

		return share.openFile(remoteFilePath, accessMask, null, SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_OPEN, null);
	}

	private void copyFiles(Connection connection, long fileLength, InputStream in, OutputStream out) throws IOException {
		int maxReadSize = connection.getNegotiatedProtocol().getMaxReadSize();
		byte[] buffer = new byte[maxReadSize];

		long remaining = fileLength;

		while (remaining > 0) {
			int amountRead = in.read(buffer);
			if (amountRead == -1) {
				remaining = 0;
			} else {
				out.write(buffer, 0, amountRead);
				remaining -= amountRead;
			}
		}
		out.flush();

		try {
			in.close();
		} catch (IOException e) {
		}
		try {
			out.close();
		} catch (IOException e) {
		}

	}

	private long getSmbFileLength(File remoteFile) {
		return remoteFile.getFileInformation(FileStandardInformation.class).getEndOfFile();
	}

	@Override
	public void upload() throws TransferException {

		try {
			try (SMBClient client = new SMBClient(getConnectionConfig())) {
				try (Connection connection = client.connect(params.getRemoteHost())) {

					Session session = getSession(connection);

					String remoteShareName = getShareName(params.getDestination());
					try (DiskShare share = (DiskShare) session.connectShare(remoteShareName)) {

						File remoteFile = openRemoteFileForWrite(share);
						java.io.File localFile = new java.io.File(params.getSourceFile());

						copyFiles(connection, localFile.length(), new FileInputStream(localFile), remoteFile.getOutputStream());

					}
				}
			}
		} catch (Exception e) {
			throw new TransferException(e);
		}

	}

	@Override
	public void download() throws TransferException {

		try {
			try (SMBClient client = new SMBClient(getConnectionConfig())) {
				try (Connection connection = client.connect(params.getRemoteHost())) {
					Session session = getSession(connection);
					String remoteShareName = getShareName(params.getSourceFile());
					try (DiskShare share = (DiskShare) session.connectShare(remoteShareName)) {

						File remoteFile = openRemoteFileForRead(share);
						java.io.File localFile = new java.io.File(params.getDestination());

						copyFiles(connection, getSmbFileLength(remoteFile), remoteFile.getInputStream(), new FileOutputStream(localFile));

					}
				}
			}

		} catch (IOException e) {
			throw new TransferException(e);
		}

	}

}
