package bgu.spl.net.srv;

import java.util.concurrent.ConcurrentHashMap;
import bgu.spl.net.impl.stomp.SingletonDataBase;
import bgu.spl.net.impl.stomp.StompFrameRaw;
import bgu.spl.net.impl.stomp.User;


public class ConnectionsClass implements Connections<StompFrameRaw> {

    // fields
    private SingletonDataBase dataBase;
    private int messageIdCounter;    

    // constructor
    public ConnectionsClass() {
        dataBase = SingletonDataBase.getInstance();
        messageIdCounter = 0;
    }

    // methods
    @Override
    public boolean send(int handlerId, StompFrameRaw msg) {
        if (SingletonDataBase.isHandlerExists(handlerId)) {
            if (msg.getCommand().equals("MESSAGE")) {
                int getUsrSubId = SingletonDataBase.getUsrSubId(msg.getHeaders().get("channel"), SingletonDataBase.getUserByHndlrId(handlerId));
                msg.getHeaders().put("subscription", Integer.toString(getUsrSubId));
                msg.getHeaders().put("message-id", Integer.toString(messageIdCounter));
                messageIdCounter++;
            }
            SingletonDataBase.getHandler(handlerId).send(msg);
            return true; 
        }
        return false;
    }

    @Override
    public void send(String channel, StompFrameRaw msg) {
        ConcurrentHashMap<Integer, User> currChannel = SingletonDataBase.getChannel(channel);
        for (User currUser : currChannel.values()) {
            send(currUser.getCurrHandlerId(), msg);
        }
    }

    @Override
    public void disconnect(int handlerId) {
        SingletonDataBase.removeHandler(handlerId);
    }
}
