package es.hefame.filetransfer.request;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;

import es.hefame.filetransfer.getopts.CliParams;

public class SCP extends TransferRequest {

	public SCP(CliParams params) {
		super(params);
	}

	private Properties getTransferProperties() {
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");

		return config;
	}

	private ChannelSftp getChannel() throws JSchException {

		JSch jsch = new JSch();
		try {
			if (this.params.getPassword().length() == 0) {
				String privateKey = System.getProperty("user.home") + "/.ssh/id_rsa";
				jsch.addIdentity(privateKey);
			}
		} catch (JSchException e) {

		}

		int port = params.getRemotePort() == 0 ? 22 : params.getRemotePort();
		Session session = jsch.getSession(params.getUsername(), params.getRemoteHost(), port);
		session.setPassword(params.getPassword());
		session.setConfig(this.getTransferProperties());
		session.connect();

		ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
		channel.connect();
		return channel;
	}

	/**
	 * Comprueba si el destino indicado es un directorio. De ser así, le incluye al
	 * final el nombre del fichero origen de modo que se llamen igual.
	 * 
	 * @param channel
	 * @return
	 */
	private String getFullRemotePath(ChannelSftp channel) {

		String ficheroRemoto = params.getDestination();

		if (ficheroRemoto.endsWith("/")) {
			// Como el destino acaba en /, podemos asumir directamente que es un directorio
			return ficheroRemoto + params.getSourceFileName();
		}

		try {
			SftpATTRS remoteAttrs = channel.stat(ficheroRemoto);

			if (remoteAttrs.isDir()) {
				// El fichero remoto es un directorio
				ficheroRemoto += "/" + params.getSourceFileName();
			} else {
				// El fichero remoto es un fichero que ya existe.
				// La transferencia intentará sobreescribirlo.
			}
		} catch (Exception e) {
			// El fichero remoto no existe. Se supone que lo va a crear la transferencia
		}

		return ficheroRemoto;

	}

	@Override
	public void upload() throws TransferException {

		try {

			ChannelSftp channel = getChannel();
			String ficheroRemoto = getFullRemotePath(channel);

			File sourceFile = new File(params.getSourceFile());
			try (FileInputStream fis = new FileInputStream(sourceFile)) {
				// overwrite ? ChannelSftp.OVERWRITE : ChannelSftp.RESUME
				channel.put(fis, ficheroRemoto, ChannelSftp.OVERWRITE);
			}

		} catch (Exception e) {
			throw new TransferException(e);
		}

	}

	@Override
	public void download() throws TransferException {
		try {

			ChannelSftp channel = getChannel();
			String ficheroRemoto = params.getSourceFile();
			File destinationFile;

			String destinationFileName = params.getDestination();
			if (destinationFileName.endsWith("/")) {
				destinationFileName += params.getSourceFileName();
				destinationFile = new File(destinationFileName);
			} else {

				destinationFile = new File(destinationFileName);

				if (destinationFile.isDirectory()) {
					destinationFile = new File(destinationFileName + "/" + params.getSourceFileName());
				}
			}

			try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
				channel.get(ficheroRemoto, fos);
			}

		} catch (Exception e) {
			throw new TransferException(e);
		}

	}

}
