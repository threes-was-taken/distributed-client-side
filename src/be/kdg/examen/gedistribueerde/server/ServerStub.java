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
        if (!"ack".equals(reply.getMethodName())) {
            throw new RuntimeException("Expected ack, got " + reply.getMethodName());
        }
    }

    //======= INTERFACE METHODS ================
    @Override
    public void log(Document document) {
        MethodCallMessage message = new MethodCallMessage(messageManager.getMyAddress(), "log");
        // send IP and PORT to server for call by ref ( by sending the location ( ip address and port ) to the server we have a call by reference situation
        // ... since the sent location is our reference.
        message.setParameter("documentText", document.getText());
        messageManager.send(message, documentServerAddress);

        MethodCallMessage resp = this.messageManager.wReceive();
        if (!resp.getMethodName().equals("logResponse")) {
            throw new RuntimeException("Expected log message response, instead got " + resp.getMethodName());
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
        //call by ref
        /*message.setParameter("documentAddress", skeletonAddress.toString());*/
        /*checkEmptyReply(this.messageManager);*/


        // call by val
        message.setParameter("upper", document.getText());
        messageManager.send(message, documentServerAddress);

        MethodCallMessage resp = this.messageManager.wReceive();
        if (!resp.getMethodName().equals("toUpperResponse")) {
            throw new RuntimeException("Expected log message response, instead got " + resp.getMethodName());
        } else {
            System.out.println(resp.getParameter("upper"));
            System.out.println("Changing document to ALL CAPS has finished");
        }
    }

    @Override
    public void toLower(@CallByRef Document document) {
        MethodCallMessage message = new MethodCallMessage(messageManager.getMyAddress(), "toLower");
       /*
       call by ref
        message.setParameter("documentAddress", skeletonAddress.toString());
        */

        //call by val
        message.setParameter("lower", document.getText());
        messageManager.send(message, documentServerAddress);

        /*checkEmptyReply(this.messageManager);*/

        MethodCallMessage resp = this.messageManager.wReceive();
        if (!resp.getMethodName().equals("toLowerResponse")) {
            throw new RuntimeException("Expected log message response, instead got " + resp.getMethodName());
        } else {
            System.out.println(resp.getParameter("lower"));
            System.out.println("Changing document to lower caps has finished");
        }
    }

    @Override
    public void type(@CallByRef Document document, String text) {
        MethodCallMessage message = new MethodCallMessage(messageManager.getMyAddress(), "type");
        /* document call by ref
        message.setParameter("documentAddress", skeletonAddress.toString());
        */
        message.setParameter("document", document.getText());
        message.setParameter("text", text);
        messageManager.send(message, documentServerAddress);

        checkEmptyReply(this.messageManager);
    }

    //======= SETTERS ================

    public void setSkeletonAddress(NetworkAddress skeletonAddress) {
        this.skeletonAddress = skeletonAddress;
    }
}
