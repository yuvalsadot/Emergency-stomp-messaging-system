package bgu.spl.net.impl.stomp;

import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.srv.Connections;

public class StompMessagingProtocolClass implements StompMessagingProtocol<StompFrameRaw>{

    // fields
    private volatile boolean shouldTerminate = false;
    public StompFrameAction<StompFrameRaw> frame;
    int handlerId;
    Connections<StompFrameRaw> connections;
    
    // methods
    @Override
    public void start(int handlerId, Connections<StompFrameRaw> connections){
        this.handlerId = handlerId;
        this.connections = connections;
    }
    
    @Override
    public void process(StompFrameRaw message){
        String currStompCmd = message.getCommand();
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

        // handle frame
        StompFrameRaw response = frame.handle();
        
        // send response
        if (!response.getCommand().equals("NO_RESPONSE")) {
            sendFrame(response);
        }

        // send receipt
        if (!response.getCommand().equals("ERROR") && !message.getCommand().equals("DISCONNECT") && message.getHeaders().containsKey("receipt")) {
            String command = "RECEIPT";
            ConcurrentHashMap<String, String> headers = new ConcurrentHashMap<>();
            headers.put("receipt-id", message.getHeaders().get("receipt"));
            String body = "";
            sendFrame(new StompFrameRaw(command, headers, body));
        }
        
        // check if should terminate
        if(frame.shouldTerminate()){
            shouldTerminate = true;
        }
    }

    @Override
    public boolean shouldTerminate(){
        return shouldTerminate;
        
    }

    private void sendFrame(StompFrameRaw response){
        switch (response.getCommand()) {
            case "CONNECTED":
                connections.send(handlerId, response);
                break;
            case "MESSAGE":
                connections.send(response.getHeaders().get("destination"), response);
                break;
            case "RECEIPT":
                connections.send(handlerId, response);
                break;
            case "ERROR":
                connections.send(handlerId, response);
                break;
            default:
                break;
        }
    }
}