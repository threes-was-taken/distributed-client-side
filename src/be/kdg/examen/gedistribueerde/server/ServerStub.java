/* Dries created on 2/12/2020 inside the package - be.kdg.examen.gedistribueerde.server */
package be.kdg.examen.gedistribueerde.server;

import be.kdg.examen.gedistribueerde.client.Document;
import be.kdg.examen.gedistribueerde.client.DocumentImpl;
import be.kdg.examen.gedistribueerde.communication.MessageManager;
import be.kdg.examen.gedistribueerde.communication.MethodCallMessage;
import be.kdg.examen.gedistribueerde.communication.NetworkAddress;

public class ServerStub implements Server {
    private final MessageManager messageManager;
    private final NetworkAddress documentServerAddress;
    private NetworkAddress skeletonAddress;

    public ServerStub(NetworkAddress documentServerAddress) {
        this.documentServerAddress = documentServerAddress;
        messageManager = new MessageManager();
    }

    //======== PRIVATE METHODS =================
    //checks for empty messages and waits for a reply
    private static void checkEmptyReply(MessageManager messageManager) {
        MethodCallMessage reply = messageManager.wReceive();
        if (!"ok".equals(reply.getMethodName())) {
            throw new RuntimeException("Expected Ok, got " + reply.getMethodName());
        }
    }

    //======= INTERFACE METHODS ================
    @Override
    public void log(Document document) {
        MethodCallMessage message = new MethodCallMessage(messageManager.getMyAddress(), "log");
        // send IP and PORT to server for call by ref ( by sending the location ( ip address and port ) to the server we have a call by reference situation
        // ... since the sent location is our reference.
        message.setParameter("documentAddress", skeletonAddress.toString());
        message.setParameter("documentText", document.getText());
        messageManager.send(message, documentServerAddress);

        MethodCallMessage resp = this.messageManager.wReceive();
        if (!resp.getMethodName().equals("logResponse")) {
            throw new RuntimeException("Expected log message reply, instead got " + resp.getMethodName());
        }
    }

    @Override
    public Document create(String s) {
        MethodCallMessage message = new MethodCallMessage(messageManager.getMyAddress(), "create");
        message.setParameter("stringToAdd", s);
        messageManager.send(message, documentServerAddress);

        MethodCallMessage resp = messageManager.wReceive();

        if (!resp.getMethodName().equals("createNewDocument")) {
            throw new RuntimeException("Expected createNewDocument but instead got " + resp.getMethodName());
        } else {
            System.out.println("creating document has finished");
            return new DocumentImpl(resp.getParameter("newString"));
        }
    }

    @Override
    public void toUpper(@CallByRef Document document) {
        MethodCallMessage message = new MethodCallMessage(messageManager.getMyAddress(), "toUpper");
        message.setParameter("documentAddress", skeletonAddress.toString());
        messageManager.send(message, documentServerAddress);

        checkEmptyReply(this.messageManager);
    }

    @Override
    public void toLower(@CallByRef Document document) {
        MethodCallMessage message = new MethodCallMessage(messageManager.getMyAddress(), "toLower");
        message.setParameter("documentAddress", skeletonAddress.toString());
        messageManager.send(message, documentServerAddress);

        checkEmptyReply(this.messageManager);
    }

    @Override
    public void type(@CallByRef Document document, String text) {
        MethodCallMessage message = new MethodCallMessage(messageManager.getMyAddress(), "type");
        message.setParameter("documentAddress", skeletonAddress.toString());
        message.setParameter("text", text);
        messageManager.send(message, documentServerAddress);

        checkEmptyReply(this.messageManager);
    }

    //======= SETTERS ================

    public void setSkeletonAddress(NetworkAddress skeletonAddress) {
        this.skeletonAddress = skeletonAddress;
    }
}
