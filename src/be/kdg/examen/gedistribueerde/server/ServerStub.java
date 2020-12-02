/* Dries created on 2/12/2020 inside the package - be.kdg.examen.gedistribueerde.server */
package be.kdg.examen.gedistribueerde.server;

import be.kdg.examen.gedistribueerde.client.Document;
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
        message.setParameter("document.text", document.getText());
        messageManager.send(message, serverAddress);

        waitForAckMsg();
    }

    @Override
    public Document create(String s) {
        return null;
    }

    @Override
    public void toUpper(Document document) {

    }

    @Override
    public void toLower(Document document) {

    }

    @Override
    public void type(Document document, String text) {

    }
}
