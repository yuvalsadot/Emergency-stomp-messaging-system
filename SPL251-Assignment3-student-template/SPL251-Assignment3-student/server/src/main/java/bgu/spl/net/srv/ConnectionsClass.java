package bgu.spl.net.srv;

import java.util.concurrent.ConcurrentHashMap;
import bgu.spl.net.impl.stomp.SingletonDataBase;
import bgu.spl.net.impl.stomp.User;


public class ConnectionsClass implements Connections<String[]> {

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
    public boolean send(int handlerId, String[] msg) {
        if (SingletonDataBase.isHandlerExists(handlerId)) {
            if (msg[0].equals("MESSAGE")) {
                int getUsrSubId = SingletonDataBase.getUsrSubId(msg[6], SingletonDataBase.getUserByHndlrId(handlerId));
                msg[2] = ":" + Integer.toString(getUsrSubId);
                msg[4] = ":" + messageIdCounter;
                messageIdCounter++;
            }
            SingletonDataBase.getHandler(handlerId).send(msg);
            return true; 
        }
        return false;
    }

    @Override
    public void send(String channel, String[] msg) {
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
