package bgu.spl.net.srv;
import bgu.spl.net.impl.stomp.MessageEncoderDecoder;
import bgu.spl.net.impl.stomp.StompMessagingProtocol;
import java.net.Socket;
import java.util.HashMap;


public class ConnectionsClass implements Connections<String[]> {

    // fields
    HashMap<Integer, ConnectionHandler<String[]>> handlerHashMap;
    int connectionId;

    // constructor
    public ConnectionsClass() {
        this.handlerHashMap = new HashMap<>();
        connectionId = 0;
    }

    // methods
    @Override
    public boolean send(int connectionId, String[] msg) {
        boolean isSent = false;
        if (handlerHashMap.containsKey(connectionId)) {
            handlerHashMap.get(connectionId).send(msg);
            isSent = true;
        }
        return isSent;
        
    }

    @Override
    public void send(String channel, String[] msg) {
        
        
    }

    @Override
    public void disconnect(int connectionId) {
        
    }

    public int addHandler(ConnectionHandler<String[]> handler) {
        handlerHashMap.put(connectionId, handler);
        connectionId++;
        return connectionId - 1; // TODO learn how to use the reactor non blocking CH
    }



}
