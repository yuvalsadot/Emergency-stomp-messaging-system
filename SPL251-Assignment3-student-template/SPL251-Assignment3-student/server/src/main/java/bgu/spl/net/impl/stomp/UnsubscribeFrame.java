package bgu.spl.net.impl.stomp;

public class UnsubscribeFrame implements StompFrame {
    
    // fields
    private int subscriptionId;
    private int handlerId;
    private String receipt;
    private String[] message;
    
    // constructor
    public UnsubscribeFrame(String[] message, int handlerId){
        this.subscriptionId = Integer.parseInt(message[2]);
        this.handlerId = handlerId;
        this.receipt = message[6];
        this.message = message;
    }
    
    // methods
    public String[] handle(){
        int userId = SingletonDataBase.getUserByHndlrId(handlerId);
        if(!SingletonDataBase.unsubscribe(userId, subscriptionId)){
            return errorHandle("subId was not found");
        }
        else{
            String[] response = {"RECEIPT", "receipt-id", receipt, "\n", "\u0000"};
            return response;
        }
    }

    public String[] errorHandle(String message){
        String[] errorFrame = {"ERROR", "message", ": subId was not found", "\n", "The message:", "\n-----", "\n" + this.message, "\n-----", "The subId is connected to any channel", "\u0000"};
            return errorFrame;
    }

    public boolean shouldTerminate(){
        return false;
    }
}
