package es.hefame.filetransfer.getopts;

import java.util.ArrayList;
import java.util.List;

import com.hierynomus.utils.Strings;

import es.hefame.filetransfer.TransferDirection;
import es.hefame.filetransfer.TransferProtocol;
import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

public class CliParamsParser {

	private CliParamsParser() {

	}

	private static final String PROGRAM_NAME = "filetransfer";

	private static final String AVAILABLE_PROTOCOL_NAMES;
	private static final String AVAILABLE_DIRECTION_NAMES;
	static {
		TransferProtocol[] allTransferProtocols = TransferProtocol.values();
		List<String> availableProtocolNames = new ArrayList<>(allTransferProtocols.length);

		for (TransferProtocol td : allTransferProtocols) {
			availableProtocolNames.add(td.name());
		}

		AVAILABLE_PROTOCOL_NAMES = Strings.join(availableProtocolNames, '|');

		TransferDirection[] allTransferDirections = TransferDirection.values();
		List<String> availableDirectionNames = new ArrayList<>(allTransferDirections.length);

		for (TransferDirection td : allTransferDirections) {
			availableDirectionNames.add(td.name());
		}

		AVAILABLE_DIRECTION_NAMES = Strings.join(availableDirectionNames, '|');

	}

	private static final ExtendedLongOpt[] EXTENDED_LONG_OPTS = {
			new ExtendedLongOpt('h', LongOpt.NO_ARGUMENT, "Muestra esta ayuda", "help"),
			new ExtendedLongOpt('m', LongOpt.REQUIRED_ARGUMENT, "El método con el que realizar la transferencia [" + AVAILABLE_PROTOCOL_NAMES + "]", "protocol"),
			new ExtendedLongOpt('d', LongOpt.REQUIRED_ARGUMENT, "El sentido de la transferencia [" + AVAILABLE_DIRECTION_NAMES + "]", "direction"),
			new ExtendedLongOpt('H', LongOpt.REQUIRED_ARGUMENT, "El host remoto al que conectar", "host"),
			new ExtendedLongOpt('P', LongOpt.REQUIRED_ARGUMENT, "El puerto remoto al que conectar", "port"),
			new ExtendedLongOpt('u', LongOpt.REQUIRED_ARGUMENT, "El usuario con el que hacer login en el host remoto", "user", "username"),
			new ExtendedLongOpt('p', LongOpt.REQUIRED_ARGUMENT, "La contraseña del usuario", "pass", "password"),
			new ExtendedLongOpt('f', LongOpt.REQUIRED_ARGUMENT, "La ruta al fichero origen", "from"),
			new ExtendedLongOpt('t', LongOpt.REQUIRED_ARGUMENT, "La ruta destino donde se almacenará el fichero transferido", "to") };

	public static CliParams parse(String... args) throws CliParseException {

		CliParamsBuilder paramsBuilder = new CliParamsBuilder();

		LongOpt[] longOptions = ExtendedLongOpt.mergeAllLongOpts(EXTENDED_LONG_OPTS);
		String optstring = ExtendedLongOpt.generateOptstring(EXTENDED_LONG_OPTS);

		Getopt g = new Getopt(PROGRAM_NAME, args, optstring, longOptions);

		int c;
		// String arg;
		while ((c = g.getopt()) != -1) {
			switch (c) {
			case 'h':
				ExtendedLongOpt.printHelp(PROGRAM_NAME, EXTENDED_LONG_OPTS, System.out);
				System.exit(0);
				break;
			case 'm':
				paramsBuilder.setTransferProtocol(g.getOptarg());
				break;
			case 'd':
				paramsBuilder.setTransferDirection(g.getOptarg());
				break;
			case 'H':
				paramsBuilder.setRemoteHost(g.getOptarg());
				break;
			case 'P':
				paramsBuilder.setRemotePort(g.getOptarg());
				break;
			case 'u':
				paramsBuilder.setUsername(g.getOptarg());
				break;
			case 'p':
				paramsBuilder.setPassword(g.getOptarg());
				break;
			case 'f':
				paramsBuilder.setSourceFile(g.getOptarg());
				break;
			case 't':
				paramsBuilder.setDestination(g.getOptarg());
				break;
			case ':':
				throw new CliParseException("La opción '" + (char) g.getOptopt() + "' requiere un argumento");
			case '?':
				throw new CliParseException("La opción '" + (char) g.getOptopt() + "' no se reconoce");
			default:
				break;
			}
		}

		return paramsBuilder.build(args);

	}
}
