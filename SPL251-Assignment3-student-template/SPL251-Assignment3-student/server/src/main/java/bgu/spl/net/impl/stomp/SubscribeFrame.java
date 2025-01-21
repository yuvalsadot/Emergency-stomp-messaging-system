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
        switch (SingletonDataBase.addUserToChannel(destination, userId, subscriptionId)) {
            case 0:
                String[] response = {"RECEIPT", "receipt-id", receipt, "\n", "\u0000"};
                return response;
            case 1:
                return errorHandle("User already subscribed to this channel");
            case 2:
                return errorHandle("Channel does not exist"); 
            default:
                return null;
        }
    }

    public String[] errorHandle(String message){
        if (message.equals("User already subscribed to this channel")){
            String[] errorFrame = {"ERROR", "message", ": Already subsricbed", "\n", "The message:", "\n-----", "\n" + this.message, "\n-----", "The user is already subscribed to this channel, no need to resubscribe", "\u0000"};
            return errorFrame;
        }
        else if (message.equals("Channel does not exist")){
            String[] errorFrame = {"ERROR", "message", ": Channel does not exist", "\n", "The message:", "\n-----", "\n" + this.message, "\n-----", "You need to create this channel before you subscribe", "\u0000"};
            return errorFrame;
        }
        else{
            String[] errorFrame = {"ERROR", "message", ": General error", "\n", "The message:", "\n-----", "\n" + this.message, "\n-----", "The server could not subscribe you right now, please try again later", "\u0000"};
            return errorFrame;
        }
    }

    public boolean shouldTerminate(){
        return false;
    }
}
