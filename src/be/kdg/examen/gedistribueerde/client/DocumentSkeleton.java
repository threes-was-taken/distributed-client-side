/* Dries created on 2/12/2020 inside the package - be.kdg.examen.gedistribueerde.client */
package be.kdg.examen.gedistribueerde.client;

import be.kdg.examen.gedistribueerde.communication.MessageManager;
import be.kdg.examen.gedistribueerde.communication.MethodCallMessage;
import be.kdg.examen.gedistribueerde.communication.NetworkAddress;


public class DocumentSkeleton {
    private final NetworkAddress networkAddress;
    private final MessageManager messageManager;
    private Document document;
    private boolean isListening = false;


    public DocumentSkeleton(DocumentImpl document) {
        this.messageManager = new MessageManager();
        this.networkAddress = messageManager.getMyAddress();
        this.document = document;
    }

    //======== LISTENER METHODS =================
    public void listen(){
        //check if document is not null
        if (this.document == null){
            System.err.println("Unable to start listening if document is null");
            System.exit(1);
        }

        System.out.println("Started listening on " + this.networkAddress);

        this.isListening = true;
        while (isListening){

            //wait sync for req
            MethodCallMessage request = messageManager.wReceive();

            //handle incoming request
            this.handleRequest(request);
        }
    }

    public void stopListening() {
        if (!this.isListening){
            System.err.println("Unable to stop, currently listening");
            return;
        }

        this.isListening = false;
    }

    //======== SETTERS =================
    public void setDocument(Document document) {
        this.document = document;
    }

    //======== GETTERS =================
    public NetworkAddress getNetworkAddress() {
        return networkAddress;
    }

    //======== MESSAGE CONTROL =================
    private void handleRequest(MethodCallMessage request) {
        System.out.println("Received request" + request);

        switch (request.getMethodName()){
            case "log":
                this.handleLog(request);
                break;
            case "create":
                this.handleCreate(request);
                break;
            case"toUpper":
                this.handleToUpper(request);
                break;
            case "toLower":
                this.handleToLower(request);
                break;
            case "type":
                this.handleType(request);
                break;
            default:
                System.err.println("Unrecognized request " + request.getMethodName() + " received.");
                break;
        }
    }

    private void ack(NetworkAddress to){
        MethodCallMessage ack = new MethodCallMessage(this.messageManager.getMyAddress(), "ack");
        ack.setParameter("result", "ok");
        this.messageManager.send(ack, to);
    }

    private void handleType(MethodCallMessage request) {
        this.document.setText(request.getParameter("document.text"));
        for (int i = 0; i < request.getParameter("type").length() - 1; i++) {
            this.document.append(request.getParameter("type").charAt(i));
        }
    }

    private void handleToLower(MethodCallMessage request) {

    }

    private void handleToUpper(MethodCallMessage request) {

    }

    private void handleCreate(MethodCallMessage request) {

    }

    private void handleLog(MethodCallMessage request) {

    }

}
