/* Dries created on 2/12/2020 inside the package - be.kdg.examen.gedistribueerde.client */
package be.kdg.examen.gedistribueerde.client;

import be.kdg.examen.gedistribueerde.communication.MessageManager;
import be.kdg.examen.gedistribueerde.communication.MethodCallMessage;
import be.kdg.examen.gedistribueerde.communication.NetworkAddress;


public class DocumentSkeleton implements Runnable{
    private final NetworkAddress networkAddress;
    private final MessageManager messageManager;
    private Document document;
    private boolean isListening = false;


    public DocumentSkeleton() {
        this.messageManager = new MessageManager();
        this.networkAddress = messageManager.getMyAddress();
    }

    // == LISTENER =========================
    public void listen() {
        // check if client has been set
        if (this.document == null) {
            System.err.println("I can't start up if there's no document ready");
            System.exit(1);
        }

        this.isListening = true;
        while (isListening) {

            // wait sync for request
            MethodCallMessage request = messageManager.wReceive();

            // handle request
            this.handleRequest(request);
        }
    }

    public void stopListening() {
        if (!this.isListening) {
            System.err.println("I can't stop now, i'm listening");
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
            case "getText":
                handleGetText(request);
                break;
            case "setText":
                handleSetText(request);
                break;
            case "appendText":
                handleAppend(request);
                break;
            case "setChar":
                handleSetChar(request);
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

    //======== DELEGATIONS =================

    private void handleSetChar(MethodCallMessage request) {
        int position = Integer.parseInt(request.getParameter("positionChar").trim());
        char c = request.getParameter("character").trim().charAt(0);

        this.document.setChar(position, c);

        ack(request.getOriginator());
    }

    private void handleAppend(MethodCallMessage request) {
        String text = request.getParameter("toAppend");

        this.document.append(text.charAt(0));

        ack(request.getOriginator());
    }

    private void handleSetText(MethodCallMessage request) {
        String text = request.getParameter("textToSet");

        this.document.setText(text);

        MethodCallMessage resp = new MethodCallMessage(messageManager.getMyAddress(), "setText");
        messageManager.send(resp, request.getOriginator());
    }

    private void handleGetText(MethodCallMessage request) {
        //get text from doc
        String text = this.document.getText();

        //send text to server as response
        MethodCallMessage resp = new MethodCallMessage(this.messageManager.getMyAddress(), request.getMethodName());
        resp.setParameter("value", text);
        this.messageManager.send(resp, request.getOriginator());
    }

    @Override
    public void run() {
        listen();
    }
}
