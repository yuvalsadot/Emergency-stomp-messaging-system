package bgu.spl.net.srv;

import bgu.spl.net.impl.stomp.SingletonDataBase;


public class ConnectionsClass implements Connections<String[]> {

    // fields
    SingletonDataBase dataBase;

    // constructor
    public ConnectionsClass() {
        dataBase = SingletonDataBase.getInstance();
    }

    // methods
    @Override
    public boolean send(int connectionId, String[] msg) {
        boolean isSent = false;
        return isSent; 
    }

    @Override
    public void send(String channel, String[] msg) {
        
    }

    @Override
    public void disconnect(int connectionId) {
        
    }
}
