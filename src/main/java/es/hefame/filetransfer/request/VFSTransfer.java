package es.hefame.filetransfer.request;

import org.apache.commons.logging.impl.NoOpLog;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.provider.UriParser;

import es.hefame.filetransfer.TransferDirection;
import es.hefame.filetransfer.getopts.CliParams;

import java.net.URI;
import java.net.URISyntaxException;

public abstract class VFSTransfer extends TransferRequest {

	protected VFSTransfer(CliParams params) {
		super(params);
	}

	protected abstract FileSystemOptions getFileSystemOptions() throws FileSystemException;

	private String getRemotePath() throws URISyntaxException {
		return this.getRemotePath(false);
	}

	private String getRemotePath(boolean hidePassword) throws URISyntaxException {

		String path = "";
		if (params.getDirection() == TransferDirection.UPLOAD) {
			path = params.getDestination();
		} else {
			path = params.getSourceFile();
		}
		if (!path.startsWith("/")) {
			path = '/' + path;
		}

		String userInfo = params.getUsername();
		if (params.getPassword() != null && params.getPassword().length() > 0) {
			userInfo += ":" + (hidePassword ? "***" : UriParser.encode(params.getPassword()));
		}

		URI uri = new URI(params.getTransferProtocol().name().toLowerCase(), userInfo, params.getRemoteHost(),
				(params.getRemotePort() > 0 ? params.getRemotePort() : -1), path, null, null);

		return uri.toString();
	}

	private String convertToAbsolutePath(String path) {
		if (path == null || path.length() == 0)
			return System.getProperty("user.dir");

		if ((path.length() > 1 && path.charAt(0) != '/' && path.charAt(1) != ':')
				|| (path.length() == 1 && path.charAt(0) != '/')) {
			return System.getProperty("user.dir") + '/' + path;
		}
		return path;

	}

	@Override
	public final void upload() throws TransferException {

		try {
			FileSystemManager manager = VFS.getManager();
			manager.setLogger(new NoOpLog());

			FileSystemOptions opts = this.getFileSystemOptions();

			// FICHERO DE ORIGEN (ES LOCAL)
			String localFile = convertToAbsolutePath(params.getSourceFile());
			FileObject local = manager.resolveFile(localFile);
			if (local.isFolder()) {
				throw new Exception("El fichero de origen es un directorio, lo cual no está soportado");
			}

			// FICHERO DE DESTINO (ES REMOTO)
			FileObject remote = manager.resolveFile(this.getRemotePath(), opts);
			// Si el destino es un directorio, probamos concatenando el nombre del fichero
			// origen al directorio.
			if (remote.isFolder()) {
				String nuevoDestino = this.getRemotePath().endsWith("/")
						? this.getRemotePath() + params.getSourceFileName()
						: this.getRemotePath() + '/' + params.getSourceFileName();
				remote = manager.resolveFile(nuevoDestino, opts);
				// Si sigue siendo un directorio, abortamos.
				if (remote.isFolder()) {
					// Recalculamos el destino sin imprimir la password !
					String destinoLog = this.getRemotePath().endsWith("/")
							? this.getRemotePath(true) + params.getSourceFileName()
							: this.getRemotePath(true) + '/' + params.getSourceFileName();
					throw new Exception("El fichero destino " + destinoLog + " ya existe y es un directorio");
				}
			}

			remote.copyFrom(local, Selectors.SELECT_SELF);

		} catch (Exception e) {
			throw new TransferException(e);
		}

	}

	@Override
	public final void download() throws TransferException {

		try {
			FileSystemManager manager = VFS.getManager();
			manager.setLogger(new NoOpLog());

			FileSystemOptions opts = this.getFileSystemOptions();

			// FICHERO DE ORIGEN (ES REMOTO)
			FileObject remote = manager.resolveFile(this.getRemotePath(), opts);
			if (remote.isFolder()) {
				throw new Exception("El fichero de origen es un directorio, lo cual no está soportado");
			}

			// FICHERO DE DESTINO (ES LOCAL)
			String localFile = convertToAbsolutePath(params.getDestination());
			FileObject local = manager.resolveFile(localFile);
			if (local.isFolder()) {
				String nuevoDestino = localFile.endsWith("/") ? localFile + params.getSourceFileName()
						: localFile + '/' + params.getSourceFileName();
				local = manager.resolveFile(nuevoDestino, opts);
				if (local.isFolder()) {
					throw new Exception("El fichero destino " + nuevoDestino + " ya existe y es un directorio");
				}
			}

			local.copyFrom(remote, Selectors.SELECT_SELF);
		} catch (Exception e) {
			throw new TransferException(e);
		}

	}

}
