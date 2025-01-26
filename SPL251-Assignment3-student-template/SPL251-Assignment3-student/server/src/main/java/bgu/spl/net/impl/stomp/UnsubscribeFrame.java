package bgu.spl.net.impl.stomp;

import java.util.concurrent.ConcurrentHashMap;

public class UnsubscribeFrame implements StompFrameAction<StompFrameRaw> {
    
    // fields
    private int subscriptionId;
    private int handlerId;
    private StompFrameRaw message;
    boolean shouldTerminate = false;
    
    // constructor
    public UnsubscribeFrame(StompFrameRaw message, int handlerId){
        this.subscriptionId = Integer.parseInt(message.getHeaders().get("id"));
        this.handlerId = handlerId;
        this.message = message;
    }
    
    // methods
    @Override
    public StompFrameRaw handle(){
        int userId = SingletonDataBase.getUserByHndlrId(handlerId);
        if(!SingletonDataBase.unsubscribe(userId, subscriptionId)){
            return errorHandle("subId was not found");
        }
        else{
           String command = "NO_RESPONSE";
            ConcurrentHashMap<String, String> headers = new ConcurrentHashMap<>();
            String body = "";
            return new StompFrameRaw(command, headers, body);
        }
    }

    @Override
    public StompFrameRaw errorHandle(String message){
        SingletonDataBase.disconnectUser(handlerId);
        shouldTerminate = true;
        String command = "ERROR";
        ConcurrentHashMap<String, String> headers = new ConcurrentHashMap<>();
        headers.put("message", "Subscription Id was not found");
        String body = "The message:\n-----\n" + message2String(this.message) + "\n-----\n" + "The subId is connected to any channel";
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
