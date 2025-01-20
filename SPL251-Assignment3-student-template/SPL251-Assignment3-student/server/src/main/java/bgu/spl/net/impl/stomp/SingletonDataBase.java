package bgu.spl.net.impl.stomp;

import java.util.concurrent.ConcurrentHashMap;

public class SingletonDataBase {
    // SingletonDataBaseHolder class
    private static class SingletonDataBaseHolder {
        private static SingletonDataBase instance = new SingletonDataBase();
    }

    // fields
    static ConcurrentHashMap<Integer, String[]> connectionIdToUserMap;
    static ConcurrentHashMap<String, ConcurrentHashMap<Integer, Integer>> channelsMap;
    private static int currConnectionId;

    // constructor
    private SingletonDataBase() {
        connectionIdToUserMap = new ConcurrentHashMap<>();
        currConnectionId = 0;
    }

    // methods
    public static SingletonDataBase getInstance() {
        return SingletonDataBaseHolder.instance;
    }

    public static void addNew(String[] user) {
        user[2] = "connected";
        user[3] = Integer.toString(currConnectionId);
        connectionIdToUserMap.put(currConnectionId,user);
        currConnectionId++;
    }

    public static void addExisting(String[] user) {
        user[2] = "connected";
    }

    public static void addToChannel(String channel, int connectionId) {
        if (!channelsMap.containsKey(channel)) {
            channelsMap.put(channel, new ConcurrentHashMap<>());
        }
        channelsMap.get(channel).put(connectionId, connectionId);
    }

    public static int getUser(String userName) {
        for (String[] user : connectionIdToUserMap.values()) {
            if (user[0].equals(userName)) {
                return Integer.parseInt(user[3]);
            }
        }
        return -1;
    }

    public static void disconnect(int connectionId) {
        connectionIdToUserMap.get(connectionId)[2] = "disconnected";
    }
}