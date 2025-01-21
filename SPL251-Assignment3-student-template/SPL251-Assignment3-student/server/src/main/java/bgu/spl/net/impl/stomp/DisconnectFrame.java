package bgu.spl.net.impl.stomp;

public class DisconnectFrame implements StompFrame {
    
    // fields
    private String receipt;
    private int handlerId;
    private String[] message;
    boolean shouldTerminate = false;

    // constructor
    public DisconnectFrame(String[] message, int handlerId){
        this.receipt = message[2];
        this.handlerId = handlerId;
        this.message = message;
    }
    
    // methods
    public String[] handle(){
        if (SingletonDataBase.disconnectUser(handlerId)){
            shouldTerminate = true;
            String[] response = {"RECEIPT", "receipt-id", receipt, "\n", "\u0000"};
            return response;
        }
        else{
            return errorHandle("User not connected");
        }     
    }

    public String[] errorHandle(String message){
        String[] errorFrame = {"ERROR", "message", ": User is not connected", "\n", "The message:", "\n-----", "\n" + this.message, "\n-----", "You cannot disconnect if you are not connected", "\u0000"};
            return errorFrame;
    }

    public boolean shouldTerminate(){
        return shouldTerminate;
    }
}