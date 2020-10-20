package es.hefame.filetransfer;

import com.kstruct.gethostname4j.Hostname;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import es.hefame.filetransfer.getopts.CliParams;
import es.hefame.filetransfer.request.TransferRequest;

public class TransferLog {

	private TransferLog() {
	}

	private static class LogEntry {

		private String argumentos;
		private long timestamp;

		private int codigoRetorno;
		private String mensajeError;
		private String claseError;

		private String protocolo;
		private String sentidoTransmision;

		private String hostLocal;
		private String hostDestino;
		private int puertoDestino;

		private String usuario;

		private String ficheroOrigen;
		private String ficheroDestino;

		private long bytesTransferidos;
		private long milisegundosTranscurridos;

		public LogEntry() {
			this.timestamp = System.currentTimeMillis();
			this.hostLocal = Hostname.getHostname();
		}

		public void setArgumentos(String[] args) {
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < args.length; i++) {
				if (i > 0)
					sb.append(' ');
				sb.append(args[i]);
			}

			this.argumentos = sb.toString();
		}

		public void setCodigoRetorno(int codigoRetorno) {
			this.codigoRetorno = codigoRetorno;
		}

		public void setError(Exception error) {
			if (error != null) {
				this.claseError = error.getClass().getName();
				this.mensajeError = error.getMessage();
			}
		}

		public void setProtocolo(String protocolo) {
			this.protocolo = protocolo;
		}

		public void setSentidoTransmision(String sentidoTransmision) {
			this.sentidoTransmision = sentidoTransmision;
		}

		public void setHostDestino(String hostDestino) {
			this.hostDestino = hostDestino;
		}

		public void setPuertoDestino(int puertoDestino) {
			this.puertoDestino = puertoDestino;
		}

		public void setUsuario(String usuario) {
			this.usuario = usuario;
		}

		public void setFicheroOrigen(String ficheroOrigen) {
			this.ficheroOrigen = ficheroOrigen;
		}

		public void setFicheroDestino(String ficheroDestino) {
			this.ficheroDestino = ficheroDestino;
		}

		public void setBytesTransferidos(long bytesTransferidos) {
			this.bytesTransferidos = bytesTransferidos;
		}

		public void setMilisegundosTranscurridos(long milisegundosTranscurridos) {
			this.milisegundosTranscurridos = milisegundosTranscurridos;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("LogEntry [argumentos=");
			builder.append(argumentos);
			builder.append(", bytesTransferidos=");
			builder.append(bytesTransferidos);
			builder.append(", claseError=");
			builder.append(claseError);
			builder.append(", codigoRetorno=");
			builder.append(codigoRetorno);
			builder.append(", ficheroDestino=");
			builder.append(ficheroDestino);
			builder.append(", ficheroOrigen=");
			builder.append(ficheroOrigen);
			builder.append(", hostDestino=");
			builder.append(hostDestino);
			builder.append(", hostLocal=");
			builder.append(hostLocal);
			builder.append(", mensajeError=");
			builder.append(mensajeError);
			builder.append(", milisegundosTranscurridos=");
			builder.append(milisegundosTranscurridos);
			builder.append(", protocolo=");
			builder.append(protocolo);
			builder.append(", puertoDestino=");
			builder.append(puertoDestino);
			builder.append(", sentidoTransmision=");
			builder.append(sentidoTransmision);
			builder.append(", timestamp=");
			builder.append(timestamp);
			builder.append(", usuario=");
			builder.append(usuario);
			builder.append("]");
			return builder.toString();
		}

		public Document toMDbObject() {
			return new Document()
					.append("timestamp", this.timestamp)
					.append("argumentos", this.argumentos)
					.append("protocolo", this.protocolo)
					.append("sentidoTransmision", this.sentidoTransmision)
					.append("hostLocal", this.hostLocal)
					.append("hostDestino", this.hostDestino)
					.append("puertoDestino", this.puertoDestino)
					.append("usuario", this.usuario)
					.append("ficheroOrigen", this.ficheroOrigen)
					.append("ficheroDestino", this.ficheroDestino)
					.append("codigoRetorno", this.codigoRetorno)
					.append("claseError", this.claseError)
					.append("mensajeError", this.mensajeError)
					.append("bytesTransferidos", this.bytesTransferidos)
					.append("milisegundosTranscurridos", this.milisegundosTranscurridos);
		}
	}

	public static void logTransfer(TransferRequest transferRequest, int result) {

		LogEntry logEntry = new TransferLog.LogEntry();

		CliParams params = transferRequest.getCliParams();

		logEntry.setArgumentos(params.getCliArgs());

		logEntry.setProtocolo(params.getTransferProtocol().name());
		logEntry.setSentidoTransmision(params.getDirection().name());

		logEntry.setHostDestino(params.getRemoteHost());
		logEntry.setPuertoDestino(params.getRemotePort());

		logEntry.setUsuario(params.getUsername());

		logEntry.setFicheroDestino(params.getSourceFile());
		logEntry.setFicheroOrigen(params.getDestination());

		logEntry.setBytesTransferidos(transferRequest.getTransferredBytes());
		logEntry.setMilisegundosTranscurridos(transferRequest.getTimeElapsed());

		logEntry.setCodigoRetorno(result);
		logEntry.setError(null);

		// System.out.println(logEntry);
		TransferLog.mdb(logEntry);

	}

	public static void logTransfer(String[] args, CliParams params, int result, Exception error) {

		LogEntry logEntry = new TransferLog.LogEntry();

		if (params != null) {
			logEntry.setArgumentos(params.getCliArgs());

			logEntry.setProtocolo(params.getTransferProtocol().name());
			logEntry.setSentidoTransmision(params.getDirection().name());

			logEntry.setHostDestino(params.getRemoteHost());
			logEntry.setPuertoDestino(params.getRemotePort());

			logEntry.setUsuario(params.getUsername());

			logEntry.setFicheroDestino(params.getSourceFile());
			logEntry.setFicheroOrigen(params.getDestination());
		} else {
			logEntry.setArgumentos(args);
		}

		logEntry.setCodigoRetorno(result);
		logEntry.setError(error);

		// System.out.println(logEntry);
		TransferLog.mdb(logEntry);

	}

	public static void mdb(LogEntry logEntry) {

		// MongoCredential credential =
		// MongoCredential.createScramSha256Credential("fileTransfer", "fileTransfer",
		// "ElFuturoEsFTP".toCharArray());
		// MongoClientOptions options = MongoClientOptions.builder().build();
		try {
			String mdbstring = System.getProperty("mongodb");
			try (MongoClient mongoClient = new MongoClient(new MongoClientURI(mdbstring))) {
				MongoDatabase database = mongoClient.getDatabase("fileTransfer");
				MongoCollection<Document> collection = database.getCollection("transferencias");
				collection.insertOne(logEntry.toMDbObject());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
