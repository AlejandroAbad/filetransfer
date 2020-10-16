package es.hefame.filetransfer.getopts;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import gnu.getopt.LongOpt;

/**
 * Clase que permite el manejo de conjuntos de gnu.getopt.LongOpt, permitiendo
 * que se definan múltiples valores de longOpt de manera sencilla
 * 
 * <pre>
 * ExtendedLongOpt extendedOption = new ExtendedLongOpt('l', ExtendedLongOpt.NO_ARGUMENT, "Esta opción lista algo",
 * 		"list", "ls");
 * for (LongOpt opt : extendedOption.generateLongOpts()) {
 * 	System.out.println((char) opt.getVal() + " -> " + opt.getName());
 * }
 * </pre>
 * 
 * Generará 2 LongOpt con los siguientes valores de [val, name] con la opción
 * ExtendedLongOpt.NO_ARGUMENT
 * 
 * <pre>
 * list -> l
 * ls -> l
 * </pre>
 * 
 */
public class ExtendedLongOpt {

	public static final int NO_ARGUMENT = LongOpt.NO_ARGUMENT;
	public static final int OPTIONAL_ARGUMENT = LongOpt.OPTIONAL_ARGUMENT;
	public static final int REQUIRED_ARGUMENT = LongOpt.REQUIRED_ARGUMENT;

	private final char optChar;
	private final int argumentFlag;
	private final String description;
	private final String[] longOptions;

	/**
	 * 
	 * @param optChar
	 * @param argumentFlag
	 * @param description
	 * @param longOptions
	 */
	public ExtendedLongOpt(char optChar, int argumentFlag, String description, String... longOptions) {
		this.optChar = optChar;
		this.argumentFlag = argumentFlag;
		this.description = description;
		this.longOptions = longOptions;
	}

	/**
	 * Genera la lista de LongOpt derivada de este objeto. Generará un LongOpt con
	 * las opciones de flag y val indicadas en argumentFlag y optChar
	 * respectivamente para cada string que se encuentre en el array de longOptions.
	 * 
	 * @return La lista de LongOpt resultante.
	 */
	public LongOpt[] generateLongOpts() {
		List<LongOpt> generatedOptions = new ArrayList<>(longOptions.length);
		for (String longOption : this.longOptions) {
			generatedOptions.add(new LongOpt(longOption, argumentFlag, null, optChar));
		}
		return generatedOptions.toArray(new LongOpt[0]);
	}

	/**
	 * Dado una serie de ExtendedLongOpts, genera un array con la suma de todos los
	 * LongOpt que cada uno de los ExtendedLongOpts deriva al llamar a
	 * generateLongOpts().
	 * 
	 * @param extendedLongOpts Lista de ExtendedLongOpt cuyas LongOpt vamos a
	 *                         generar y agrupar en un único array.
	 * @return El conjunto de todos los LongOpt generados por los ExtendedLongOpt
	 */
	public static LongOpt[] mergeAllLongOpts(ExtendedLongOpt[] extendedLongOpts) {

		List<LongOpt> finalLongOptList = new LinkedList<>();

		for (ExtendedLongOpt extendedLongOpt : extendedLongOpts) {
			Collections.addAll(finalLongOptList, extendedLongOpt.generateLongOpts());
		}

		return finalLongOptList.toArray(new LongOpt[0]);

	}

	/**
	 * Genera el optstring con el que habría que llamar a GetOpt a partir de un
	 * array de ExtendedLongOption.
	 * https://www.gnu.org/software/gnuprologjava/api/gnu/getopt/Getopt.html
	 * 
	 * @param extendedLongOpts Lista de ExtendedLongOptions con los valores posibles
	 * @param returnInOrder    Indica si se debe activar el modo "return in order".
	 * @param silentMode       Indica si activar el modo silencioso.
	 * @return Una cadena en la sintaxis de optstring
	 */
	public static String generateOptstring(ExtendedLongOpt[] extendedLongOpts, boolean returnInOrder,
			boolean silentMode) {

		StringBuilder sb = new StringBuilder();

		if (returnInOrder)
			sb.append('-');
		if (silentMode)
			sb.append(':');

		for (ExtendedLongOpt extendedLongOpt : extendedLongOpts) {

			sb.append(extendedLongOpt.optChar);

			switch (extendedLongOpt.argumentFlag) {
				case ExtendedLongOpt.REQUIRED_ARGUMENT:
					sb.append(':');
					break;
				case ExtendedLongOpt.OPTIONAL_ARGUMENT:
					sb.append(':').append(':');
					break;
				default:
					break;
			}

		}

		return sb.toString();
	}

	/**
	 * Atajo equivalente a llamar a generateOptstring con returnInOrder y silentMode
	 * activados.
	 * 
	 * 
	 * @param extendedLongOpts Lista de ExtendedLongOptions con los va
	 * @return Una cadena en la sintaxis de optstring
	 */
	public static String generateOptstring(ExtendedLongOpt[] extendedLongOpts) {
		return ExtendedLongOpt.generateOptstring(extendedLongOpts, true, true);
	}


	public static void printHelp(String programName, ExtendedLongOpt[] extendedLongOpts, PrintStream stream ) {

		StringBuilder sb = new StringBuilder();

		sb.append("Uso: " + programName + " <opciones>\n\nOpciones disponibles:\n");

		for (ExtendedLongOpt extendedLongOpt : extendedLongOpts) {



			sb.append("\n-" + extendedLongOpt.optChar);
			for (String longOptName : extendedLongOpt.longOptions) {
				sb.append(", --" + longOptName);
			}
			if (extendedLongOpt.argumentFlag == ExtendedLongOpt.REQUIRED_ARGUMENT) {
				sb.append(" <valor>");	
			} else if (extendedLongOpt.argumentFlag == ExtendedLongOpt.OPTIONAL_ARGUMENT) { 
				sb.append(" [ <valor> ]");
			}

			sb.append("\n\t" + extendedLongOpt.description);

		}

		stream.println(sb.toString());
		
	}

}
