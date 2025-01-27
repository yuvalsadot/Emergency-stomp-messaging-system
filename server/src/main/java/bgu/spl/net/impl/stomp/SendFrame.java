package bgu.spl.net.impl.stomp;

import java.util.concurrent.ConcurrentHashMap;

public class SendFrame implements StompFrameAction<StompFrameRaw> {

    // fields
    private String destination;
    private int handlerId;
    private StompFrameRaw message;
    boolean shouldTerminate = false;

    // constructor
    public SendFrame(StompFrameRaw message, int handlerId){
        this.destination = message.getHeaders().get("destination");
        this.handlerId = handlerId;
        this.message = message;
    }
    
    // methods
    @Override
    public StompFrameRaw handle(){
        if (checkHeadres()){
            return errorHandle("Missing headers");
        }
        if (!SingletonDataBase.isChannelExists(destination)) {
            return errorHandle("destination channel does not exist");
        }
        else if (!SingletonDataBase.isSubscribedToChannel(destination, SingletonDataBase.getUserByHndlrId(this.handlerId))) {
            return errorHandle("user is not subscribed to the destination channel");
        }
        else {
            String command = "MESSAGE";
            ConcurrentHashMap<String, String> headers = new ConcurrentHashMap<>();
            headers.put("subscription", "");
            headers.put("message-id", ""); // connectionClass will fill the missing fields
            headers.put("destination", destination);
            String body = message.getBody();
            return new StompFrameRaw(command, headers, body);
        }
    }

    private boolean checkHeadres(){
        return destination == null;
    }

    @Override
    public StompFrameRaw errorHandle(String message){
        SingletonDataBase.disconnectUser(handlerId);
        shouldTerminate = true;
        String command = "ERROR";
        String body = "The message:\n-----\n" + message2String(this.message) + "\n-----\n";
        ConcurrentHashMap<String, String> headers = new ConcurrentHashMap<>();
        if (this.message.getHeaders().get("receipt") != null){
            headers.put("receipt-id", this.message.getHeaders().get("receipt"));
        }
        if (message.equals("destination channel does not exist")){
            headers.put("message", "Wrong channel");
            body += "The channel you approached doesn't exist";
        }
        else if (message.equals("user is not subscribed to the destination channel")){
            headers.put("message", "User isn't subscribed");
            body += "You should subscribe to the channel before sending messages";
        }
        else if (message.equals("Missing headers")){
            headers.put("message", "Missing headers");
            body += "You must include the destination header";
        }
        return new StompFrameRaw(command, headers, body);
    }

    @Override
    public boolean shouldTerminate(){
        return shouldTerminate;
    }

    private String message2String(StompFrameRaw message){
        String output = message.getCommand() + "\n";
        for (String key : message.getHeaders().keySet()){
            output += key + ":" + message.getHeaders().get(key) + "\n";
        }
        output += "\n" + message.getBody();
        return output;
    }
}

