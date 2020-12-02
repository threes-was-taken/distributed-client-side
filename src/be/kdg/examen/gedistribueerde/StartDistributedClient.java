package be.kdg.examen.gedistribueerde;

import be.kdg.examen.gedistribueerde.client.Client;
import be.kdg.examen.gedistribueerde.client.DocumentImpl;
import be.kdg.examen.gedistribueerde.communication.NetworkAddress;
import be.kdg.examen.gedistribueerde.server.Server;
import be.kdg.examen.gedistribueerde.server.ServerStub;

public class StartDistributedClient {
    //Start Client component
    public static void main(String[] args) {
        //check if args contains 2 items
        if (args.length != 2){
            System.err.println("Usage: java Client <serverIP> <serverPort>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[1]);
        NetworkAddress serverStubAddress = new NetworkAddress(args[0], port);
        Server server = new ServerStub(serverStubAddress);
        DocumentImpl document = new DocumentImpl();
        Client client = new Client(server, document);
        client.run();
    }
}
