package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.StompMessagingProtocol;
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
        message = rearrangeFrame(message);
        String currStompCmd = message[0];
        switch (currStompCmd) {
            case "CONNECT":
                frame = new ConnectFrame(message, handlerId);
                break;
            case "SEND":
                frame = new SendFrame(message, handlerId);
                break;
            case "SUBSCRIBE":
                frame = new SubscribeFrame(message, handlerId);
                break;
            case "UNSUBSCRIBE":
                frame = new UnsubscribeFrame(message, handlerId);
                break;
            case "DISCONNECT":
                frame = new DisconnectFrame(message, handlerId);
                break;
            default:
                break;
        }

        String[] response = frame.handle();
        sendFrame(response);
        
        if (message[0].equals("CONNECT") || message[0].equals("SEND")){
            int counter = 0;
            boolean isFound = false;
            while ((counter + 1) < message.length && !isFound){
                if (message[counter + 1] == "receipt-id"){
                    isFound = true;
                }
                    counter++;
            }
            if (isFound) {
                String[] response2 = {"RECEIPT", "receipt-id", message[counter + 1], "\n", "\u0000"};
                sendFrame(response2);
            }
        }
        if(frame.shouldTerminate()){
            shouldTerminate = true;
        }
    }

    @Override
    public boolean shouldTerminate(){
        return shouldTerminate;
        
    }

    public String[] rearrangeFrame (String[] message){
        for (int i = 0; i < message.length; i++){
            if (message[i] != null && message[i].length() >= 1){
                if (message[i].charAt(0) == '\n'){
                    message[i] = message[i].substring(1);
                }
                else if (message[i].charAt(0) == ':'){
                    message[i] = message[i].substring(1);
                }
            }
        }
        return message;
    }

    private void sendFrame(String[] response){
        switch (response[0]) {
            case "CONNECTED":
                connections.send(handlerId, response);
                break;
            case "MESSAGE":
                connections.send(response[6], response);
                break;
            case "RECEIPT":
                connections.send(handlerId, response);
                break;
            case "ERROR":
                connections.send(handlerId, response);    
                shouldTerminate = true;
                break;
            default:
                break;
        }
    }
}