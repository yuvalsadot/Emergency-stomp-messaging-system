package bgu.spl.net.impl.stomp;

import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.srv.ConnectionHandler;

public class SingletonDataBase {
    // SingletonDataBaseHolder class
    private static class SingletonDataBaseHolder {
        private static SingletonDataBase instance = new SingletonDataBase();
    }

    // fields
    static ConcurrentHashMap<Integer, User> usersMap;
    static ConcurrentHashMap<String, ConcurrentHashMap<Integer, User>> channelsMap;
    static ConcurrentHashMap<Integer, ConnectionHandler<String[]>> handlersMap;
    private static int currConnectionId;

    // constructor
    private SingletonDataBase() {
        usersMap = new ConcurrentHashMap<>();
        channelsMap = new ConcurrentHashMap<>();
        handlersMap = new ConcurrentHashMap<>();
        currConnectionId = 0;
    }

    // methods
    public static SingletonDataBase getInstance() {
        return SingletonDataBaseHolder.instance;
    }

    public static void addNewUser(User newUser) {
        newUser.login();
        newUser.setUserId(currConnectionId);
        usersMap.put(currConnectionId, newUser);
        currConnectionId++;
    }

    public static void addExistingUser(User existingUser, int connectionId) {
        existingUser.login();
        existingUser.setCurrHandlerId(connectionId);
    }

    //TODO subscribe
    public static void addUserToChannel(String channel, int connectionId) {

    }

    public static int getUser(String userName) {
        for (User currUser : usersMap.values()) {
            if (currUser.getUserName().equals(userName)) {
                return currUser.getUserId();
            }
        }
        return -1;
    }
    //TODO disconnect
    public static void disconnectUser(int connectionId) {
        
    }

    public static void addHandler(ConnectionHandler<String[]> handler) {
        handlersMap.put(currConnectionId, handler);
    }

    public static ConnectionHandler<String[]> getHandler(int connectionId) {
        return handlersMap.get(connectionId);
    }

    public static void removeHandler(int connectionId) {
        handlersMap.remove(connectionId);
    }
}