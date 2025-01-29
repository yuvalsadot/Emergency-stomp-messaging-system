package bgu.spl.net.impl.stomp;

import java.util.concurrent.ConcurrentHashMap;

public class SubscribeFrame implements StompFrameAction<StompFrameRaw>{
    // fields
    private String destination;
    private String subscriptionId;
    private int handlerId;
    private StompFrameRaw message;
    boolean shouldTerminate = false;

    // constructor
    public SubscribeFrame(StompFrameRaw message, int handlerId){
       this.destination = message.getHeaders().get("destination");
       this.subscriptionId = message.getHeaders().get("id");
       this.handlerId = handlerId;
       this.message = message;
    }

    // methods
    @Override
    public StompFrameRaw handle(){
        if (checkHeadres()){
            return errorHandle("Missing headers");
        }
        int userId = SingletonDataBase.getUserByHndlrId(this.handlerId);
        if(SingletonDataBase.addUserToChannel(destination, userId, Integer.parseInt(subscriptionId))) { 
            String command = "NO_RESPONSE";
            ConcurrentHashMap<String, String> headers = new ConcurrentHashMap<>();
            String body = "";
            return new StompFrameRaw(command, headers, body);
        }
        else {
            return errorHandle("User already subscribed to this channel");
        }
    }

    private boolean checkHeadres(){
        return destination == null || subscriptionId == null;
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
        if (message.equals("User already subscribed to this channel")){
            headers.put("message", "Already subsricbed");
            body += "The user is already subscribed to this channel, no need to resubscribe";
        }
        else if (message.equals("Missing headers")){
            headers.put("message", "Missing headers");
            body += "You must include the destination and id headers";
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
