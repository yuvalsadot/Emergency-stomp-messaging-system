package bgu.spl.net.impl.stomp;

import java.util.concurrent.ConcurrentHashMap;

public class SubscribeFrame implements StompFrameAction<StompFrameRaw>{
    // fields
    private String destination;
    private int subscriptionId;
    private int handlerId;
    private StompFrameRaw message;
    boolean shouldTerminate = false;

    // constructor
    public SubscribeFrame(StompFrameRaw message, int handlerId){
       this.destination = message.getHeaders().get("destination");
       this.subscriptionId = Integer.parseInt(message.getHeaders().get("id"));
       this.handlerId = handlerId;
       this.message = message;
    }

    // methods
    @Override
    public StompFrameRaw handle(){
        int userId = SingletonDataBase.getUserByHndlrId(this.handlerId);
        if(SingletonDataBase.addUserToChannel(destination, userId, subscriptionId)) { 
            String command = "NO_RESPONSE";
            ConcurrentHashMap<String, String> headers = new ConcurrentHashMap<>();
            String body = "";
            return new StompFrameRaw(command, headers, body);
        }
        else {
            return errorHandle("User already subscribed to this channel");
        }
    }

    @Override
    public StompFrameRaw errorHandle(String message){
        SingletonDataBase.disconnectUser(handlerId);
        shouldTerminate = true;
        String command = "ERROR";
        String body = "The message:\n-----\n" + message2String(this.message) + "\n-----\n";
        ConcurrentHashMap<String, String> headers = new ConcurrentHashMap<>();
        if (message.equals("User already subscribed to this channel")){
            headers.put("message", "Already subsricbed");
            body += "The user is already subscribed to this channel, no need to resubscribe";
        }
        else{
            headers.put("message", "General error");
            body += "The server could not subscribe you right now, please try again later";
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
