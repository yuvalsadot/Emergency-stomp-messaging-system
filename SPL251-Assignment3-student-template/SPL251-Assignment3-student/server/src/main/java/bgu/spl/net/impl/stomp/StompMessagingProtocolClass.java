package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.Connections;

public class StompMessagingProtocolClass implements StompMessagingProtocol<String[]>{

    // fields
    private boolean shouldTerminate = false;
    public StompFrame frame;
    int handlerId;
    Connections<String[]> connections;
    
    // methods
    @Override
    public void start(int handlerId, Connections<String[]> connections){
        this.handlerId = handlerId;
        this.connections = connections;
    }
    
    @Override
    public void process(String[] message){
        String currStompCmd = message[0];
        switch (currStompCmd) {
            case "CONNECT":
                frame = new ConnectFrame(message, handlerId);
            case "SEND":
                frame = new SendFrame(message, handlerId);
            case "SUBSCRIBE":
                frame = new SubscribeFrame(message, handlerId);
            case "UNSUBSCRIBE":
                frame = new UnsubscribeFrame(message, handlerId);
            case "DISCONNECT":
                frame = new DisconnectFrame(message, handlerId);
                shouldTerminate = true;        
            default:
                break;
        }
        
        String[] response = frame.handle();
        sendFrame(response);
        
        if (message[0].equals("CONNECT") || message[0].equals("SEND")){
            int counter = 1;
            while (message[counter] != "receipt-id"){
                counter++;
            }
            String[] response2 = {"RECEIPT", "receipt-id", message[counter + 1], "\n", "\u0000"};
            sendFrame(response2);
        }
    }

    @Override
    public boolean shouldTerminate(){
        return shouldTerminate;
        
    }

    private void sendFrame(String[] response){
        switch (response[0]) {
            case "CONNECTED":
                connections.send(handlerId, response);

            case "MESSAGE":
                connections.send(response[6], response);
                
            case "RECEIPT":
                connections.send(handlerId, response);            
        
            case "ERROR":
                connections.send(handlerId, response);    
                shouldTerminate = true;
            default:
                break;
        }
    }
}