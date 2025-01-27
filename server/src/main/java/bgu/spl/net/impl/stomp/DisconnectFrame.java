package bgu.spl.net.impl.stomp;

import java.util.concurrent.ConcurrentHashMap;

public class DisconnectFrame implements StompFrameAction<StompFrameRaw> {
    
    // fields
    private String receipt;
    private int handlerId;
    private StompFrameRaw message;
    boolean shouldTerminate = false;

    // constructor
    public DisconnectFrame(StompFrameRaw message, int handlerId){
        this.receipt = message.getHeaders().get("receipt");
        this.handlerId = handlerId;
        this.message = message;
    }
    
    // methods
    @Override
    public StompFrameRaw handle(){
        if (checkHeadres()){
            return errorHandle("Missing headers");
        }
        if (SingletonDataBase.disconnectUser(handlerId)){
            shouldTerminate = true;
            String command = "RECEIPT";
            ConcurrentHashMap<String, String> headers = new ConcurrentHashMap<>();
            headers.put("receipt-id", receipt);
            String body = "";
            return new StompFrameRaw(command, headers, body);
        }
        else{
            return errorHandle("User not connected");
        }     
    }

    private boolean checkHeadres(){
        return receipt == null;
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
        if (message.equals("User is not connected")) {
            headers.put("message", "User is not connected");
            body += "You cannot disconnect if you are not connected";
        }
        else if (message.equals("Missing headers")){
            headers.put("message", "Missing headers");
            body += "You must include the receipt header";
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