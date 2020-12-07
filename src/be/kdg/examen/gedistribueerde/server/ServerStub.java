/* Dries created on 2/12/2020 inside the package - be.kdg.examen.gedistribueerde.server */
package be.kdg.examen.gedistribueerde.server;

import be.kdg.examen.gedistribueerde.client.Document;
import be.kdg.examen.gedistribueerde.client.DocumentImpl;
import be.kdg.examen.gedistribueerde.communication.MessageManager;
import be.kdg.examen.gedistribueerde.communication.MethodCallMessage;
import be.kdg.examen.gedistribueerde.communication.NetworkAddress;

public class ServerStub implements Server {
    private final MessageManager messageManager;
    private final NetworkAddress serverAddress;
    private NetworkAddress receiveAddress;

    public ServerStub(NetworkAddress serverAddress) {
        this.serverAddress = serverAddress;
        messageManager = new MessageManager();
    }

    //======== PRIVATE METHODS =================
    //checks for empty messages and waits for a reply
    private void waitForAckMsg(){
        String val = "";
        while (!"Ok".equals(val)){
            MethodCallMessage response = messageManager.wReceive();
            if (!"result".equals(response.getMethodName())){
                continue;
            }
            val = response.getParameter("result");
        }
    }

    //======= INTERFACE METHODS ================
    @Override
    public void log(Document document) {
        MethodCallMessage message = new MethodCallMessage(messageManager.getMyAddress(), "log");
        message.setParameter("documentText", document.getText());
        messageManager.send(message, serverAddress);

        waitForAckMsg();
    }

    @Override
    public Document create(String s) {
        MethodCallMessage message = new MethodCallMessage(messageManager.getMyAddress(), "create");
        message.setParameter("stringToAdd", s);
        messageManager.send(message, serverAddress);

        MethodCallMessage resp = messageManager.wReceive();

        if (!resp.getMethodName().equals("createNewDocument")){
            throw new RuntimeException("Expected createNewDocument but instead got " + resp.getMethodName());
        } else {
            System.out.println("creating document has finished");

            String newString = messageManager.wReceive().getParameter("newString");
            return new DocumentImpl(newString);
        }
    }

    @Override
    public void toUpper(Document document) {
        MethodCallMessage message = new MethodCallMessage(messageManager.getMyAddress(), "toUpper");
        message.setParameter("document", document.getText());
        messageManager.send(message, serverAddress);

       waitForAckMsg();
    }

    @Override
    public void toLower(Document document) {
        MethodCallMessage message = new MethodCallMessage(messageManager.getMyAddress(), "toLower");
        message.setParameter("document", document.getText());
        messageManager.send(message, serverAddress);

        waitForAckMsg();
    }

    @Override
    public void type(Document document, String text) {
        MethodCallMessage message = new MethodCallMessage(messageManager.getMyAddress(), "type");
        message.setParameter("document", document.getText());
        message.setParameter("text", text);
        messageManager.send(message, serverAddress);

        waitForAckMsg();
    }

    //======= SETTERS ================

    public void setReceiveAddress(NetworkAddress receiveAddress) {
        this.receiveAddress = receiveAddress;
    }
}
