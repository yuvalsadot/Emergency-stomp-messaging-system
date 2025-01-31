package bgu.spl.net.srv;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import bgu.spl.net.impl.stomp.SingletonDataBase;
import bgu.spl.net.impl.stomp.StompFrameRaw;
import bgu.spl.net.impl.stomp.User;

public class ConnectionsClass implements Connections<StompFrameRaw> {

    // fields
    private AtomicInteger messageIdCounter;    

    // constructor
    public ConnectionsClass() {
        SingletonDataBase.getInstance();
        messageIdCounter = new AtomicInteger(0);
    }

    // methods
    @Override
    public synchronized boolean send(int handlerId, StompFrameRaw msg) {
        if (SingletonDataBase.isHandlerExists(handlerId)) {
            if (msg.getCommand().equals("MESSAGE")) {
                int getUsrSubId = SingletonDataBase.getUsrSubId(msg.getHeaders().get("channel"), SingletonDataBase.getUserByHndlrId(handlerId));
                msg.getHeaders().put("subscription", Integer.toString(getUsrSubId));
                msg.getHeaders().put("message-id", Integer.toString(messageIdCounter.getAndIncrement()));
            }
            SingletonDataBase.getHandler(handlerId).send(msg);
            return true; 
        }
        return false;
    }

    @Override
    public void send(String channel, StompFrameRaw msg) {
        ConcurrentHashMap<Integer, User> currChannel = SingletonDataBase.getChannel(channel);
        synchronized (currChannel) {
            for (User currUser : currChannel.values()) {
                // Check if the user is still subscribed before sending the message
                if (SingletonDataBase.isSubscribedToChannel(channel, currUser.getUserId())) {
                    send(currUser.getCurrHandlerId(), msg);
                }
            }
        }
    }

    @Override
    public void disconnect(int handlerId) {
        SingletonDataBase.removeHandler(handlerId);
    }
}
