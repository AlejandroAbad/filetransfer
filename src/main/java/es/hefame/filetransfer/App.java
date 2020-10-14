package es.hefame.filetransfer;

import org.apache.commons.vfs2.FileSystemException;

import es.hefame.filetransfer.getopts.CliParams;
import es.hefame.filetransfer.getopts.CliParamsParser;
import es.hefame.filetransfer.request.TransferRequest;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * 
     * @param args The arguments of the program.
     */
    public static void main(String... args) {

        CliParams params = CliParamsParser.parse(args);

        TransferRequest request = params.getTransferProtocol().instanciate(params);

        try {
            request.transfer();
            System.out.println("0: File copied successfully");
            System.exit(0);
        } catch (FileSystemException e) {
            System.out.println("1: " + e.getMessage());
            System.exit(1);
        }

        
    }


}
