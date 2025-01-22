package bgu.spl.net.impl.stomp;

import java.util.Arrays;

public class SendFrame implements StompFrame {

    // fields
    private String destination;
    private int handlerId;
    private String[] headerBody;
    private String[] message;

    // constructor
    public SendFrame(String[] message, int handlerId){
        this.destination = message[2];
        this.handlerId = handlerId;
        this.message = message;
        this.headerBody = Arrays.copyOfRange(message, 1, message.length);
    }
    
    // methods
    public String[] handle(){
        if (!SingletonDataBase.isChannelExists(destination)) {
            return errorHandle("destination channel does not exist");
        }
        else if (!SingletonDataBase.isSubscribedToChannel(destination, SingletonDataBase.getUserByHndlrId(this.handlerId))) {
            return errorHandle("user is not subscribed to the destination channel");
        }
        else {
            String[] frameBeg = {"MESSAGE", "subscription", "", "message-id", ""}; // connectionClass will fill the missing fields
            String[] frame2ret = new String[frameBeg.length + headerBody.length];
            System.arraycopy(frameBeg, 0, frame2ret, 0, frameBeg.length);
            System.arraycopy(headerBody, 0, frame2ret, frameBeg.length, headerBody.length);
            return frame2ret;
        }
    }

    public String[] errorHandle(String message){
        if (message.equals("destination channel does not exist")){
            String[] errorFrame = {"ERROR", "\nmessage", ": Wrong passcode", "\n", "The message:", "\n-----", "\n" + this.message, "\n-----", "\nThe channel you approached doesn't exist", "\u0000"};
            return errorFrame;
        }
        else if (message.equals("user is not subscribed to the destination channel")){
            String[] errorFrame = {"ERROR", "\nmessage", ": User already connected", "\n", "The message:", "\n-----", "\n" + this.message, "\n-----", "\nYou are already logged in, no need to connect again", "\u0000"};
            return errorFrame;
        }
        else{
            String[] errorFrame = {"ERROR", "\nmessage", ": General error", "\n", "The message:", "\n-----", "\n" + this.message, "\n-----", "\nThe server could not connect you right now, please try again later", "\u0000"};
            return errorFrame;
        }
    }

    public boolean shouldTerminate(){
        return false;
    }
}

