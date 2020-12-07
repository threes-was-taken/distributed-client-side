package be.kdg.examen.gedistribueerde;

import be.kdg.examen.gedistribueerde.client.Client;
import be.kdg.examen.gedistribueerde.client.ClientFrame;
import be.kdg.examen.gedistribueerde.client.DocumentImpl;
import be.kdg.examen.gedistribueerde.client.DocumentSkeleton;
import be.kdg.examen.gedistribueerde.communication.NetworkAddress;
import be.kdg.examen.gedistribueerde.server.Server;
import be.kdg.examen.gedistribueerde.server.ServerStub;

public class StartDistributedClient {
    //Start Client component
    public static void main(String[] args) {
        //check if args contains 2 items ( check on server connection details )
        if (args.length != 2){
            System.err.println("Usage: java Client <serverIP> <serverPort>");
            System.exit(1);
        }

        //Create connection address
        int port = Integer.parseInt(args[1]);
        NetworkAddress serverStubAddress = new NetworkAddress(args[0], port);

        //Create serverstub
        ServerStub serverStub = new ServerStub(serverStubAddress);

        //Create document skeleton for listening to incoming requests
        DocumentSkeleton documentSkeleton = new DocumentSkeleton();

        //Create document
        DocumentImpl document = new DocumentImpl();

        //Set document on skeleton
        documentSkeleton.setDocument(document);

        //Set listen address in document server stub
        serverStub.setReceiveAddress(documentSkeleton.getNetworkAddress());

        //create client
        Client client = new Client(serverStub, document);

        client.run();
    }
}
