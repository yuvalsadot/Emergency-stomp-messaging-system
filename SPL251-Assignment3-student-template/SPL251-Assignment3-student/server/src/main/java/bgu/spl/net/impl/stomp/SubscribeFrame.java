package bgu.spl.net.impl.stomp;

public class SubscribeFrame implements StompFrame{
    // fields
    private String destination;
    private int subscriptionId;
    private int handlerId;
    private String receipt;
    private String[] message;

    // constructor
    public SubscribeFrame(String[] message, int handlerId){
       this.destination = message[2];
       this.subscriptionId = Integer.parseInt(message[4]);
       this.receipt = message[6];
       this.handlerId = handlerId;
       this.message = message;
    }

    // methods
    public String[] handle(){
        int userId = SingletonDataBase.getUserByHndlrId(this.handlerId);
        if(SingletonDataBase.addUserToChannel(destination, userId, subscriptionId)) { 
            String[] response = {"RECEIPT", "receipt-id", ":" + receipt, "\n", "\u0000"};
            return response;
        }
        else {
            return errorHandle("User already subscribed to this channel");
        }
    }

    public String[] errorHandle(String message){
        if (message.equals("User already subscribed to this channel")){
            String[] errorFrame = {"ERROR", "\nmessage", ": Already subsricbed", "\n", "The message:", "\n-----", "\n" + this.message, "\n-----", "\nThe user is already subscribed to this channel, no need to resubscribe", "\u0000"};
            return errorFrame;
        }
        else{
            String[] errorFrame = {"ERROR", "\nmessage", ": General error", "\n", "The message:", "\n-----", "\n" + this.message, "\n-----", "\nThe server could not subscribe you right now, please try again later", "\u0000"};
            return errorFrame;
        }
    }

    public boolean shouldTerminate(){
        return false;
    }
}
