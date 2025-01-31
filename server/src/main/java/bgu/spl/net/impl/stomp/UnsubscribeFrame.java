package bgu.spl.net.impl.stomp;

import java.util.concurrent.ConcurrentHashMap;

public class UnsubscribeFrame implements StompFrameAction<StompFrameRaw> {
    
    // fields
    private String subscriptionId;
    private int handlerId;
    private StompFrameRaw message;
    boolean shouldTerminate = false;
    
    // constructor
    public UnsubscribeFrame(StompFrameRaw message, int handlerId){
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
        int userId = SingletonDataBase.getUserByHndlrId(handlerId);
        if(!SingletonDataBase.unsubscribe(userId, Integer.parseInt(subscriptionId))){
            return errorHandle("subId was not found");
        }
        else{
           String command = "NO_RESPONSE";
            ConcurrentHashMap<String, String> headers = new ConcurrentHashMap<>();
            String body = "";
            return new StompFrameRaw(command, headers, body);
        }
    }

    private boolean checkHeadres(){
        return subscriptionId == null;
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
        if (message.equals("subId was not found")) {
            headers.put("message", "Subscription Id was not found");
            body += "The subscription Id isn't connected to any channel";
        }
        else if (message.equals("Missing headers")){
            headers.put("message", "Missing headers");
            body += "You must include the id header";
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
