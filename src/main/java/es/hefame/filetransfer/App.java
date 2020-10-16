package es.hefame.filetransfer;


import es.hefame.filetransfer.getopts.CliParams;
import es.hefame.filetransfer.getopts.CliParamsParser;
import es.hefame.filetransfer.getopts.CliParseException;
import es.hefame.filetransfer.request.TransferException;
import es.hefame.filetransfer.request.TransferRequest;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * 
     * 
     * @param args The arguments of the program.
     */
    public static void main(String... args) {

        try {
            CliParams params = CliParamsParser.parse(args);
            TransferRequest request = params.getTransferProtocol().instanciate(params);
            request.transfer();
            System.out.println("0: File copied successfully");
            System.exit(0);
        } catch (TransferException e) {
            System.out.println("1: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        } catch (CliParseException e) {
            System.out.println("2: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(2);
        } catch (InstantiationException e) {
            System.out.println("3: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(3);
        }

        
    }


}
