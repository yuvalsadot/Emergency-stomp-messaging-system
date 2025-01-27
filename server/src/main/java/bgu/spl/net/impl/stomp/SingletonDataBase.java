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
    static ConcurrentHashMap<Integer, ConnectionHandler<StompFrameRaw>> handlersMap;
    private static int currUserId;

    // constructor
    private SingletonDataBase() {
        usersMap = new ConcurrentHashMap<>();
        channelsMap = new ConcurrentHashMap<>();
        handlersMap = new ConcurrentHashMap<>();
        currUserId = 0;
    }

    // methods
    public static SingletonDataBase getInstance() {
        return SingletonDataBaseHolder.instance;
    }

    // getters
    public static int getUserByUsrnm(String userName) {
        for (User currUser : usersMap.values()) {
            if (currUser.getUserName().equals(userName)) {
                return currUser.getUserId();
            }
        }
        return -1;
    }

    public static int getUserByHndlrId(int handlerId) {
        for (User currUser : usersMap.values()) {
            if (currUser.getCurrHandlerId() == handlerId) {
                return currUser.getUserId();
            }
        }
        return -1;
    }

    public static ConnectionHandler<StompFrameRaw> getHandler(int handlerId) {
        return handlersMap.get(handlerId);
    }

    public static ConcurrentHashMap<Integer, User> getChannel(String channel) {
        return channelsMap.get(channel);
    }

    public static int getUsrSubId(String channel, int userId) {
        return usersMap.get(userId).getChannelId(channel);
    }

    // adders
    public static void addHandler(int handlerId, ConnectionHandler<StompFrameRaw> handler) {
        handlersMap.put(handlerId, handler);
    }

    public static void addNewUser(User newUser) {
        newUser.login();
        newUser.setUserId(currUserId);
        usersMap.put(currUserId, newUser);
        currUserId++;
    }

    public static void addExistingUser(User existingUser, int handlerId) {
        existingUser.login();
        existingUser.setCurrHandlerId(handlerId);
    }

    public static boolean addUserToChannel(String channel, int userId, int subscriptionId) {
        if (channelsMap.containsKey(channel)) {
            if (usersMap.get(userId).subscribe(channel, subscriptionId)){
                channelsMap.get(channel).put(userId, usersMap.get(userId));
                return true;
            }
            else {
                return false;
            }
        }
        else {
            channelsMap.put(channel, new ConcurrentHashMap<>());
            usersMap.get(userId).subscribe(channel, subscriptionId);
            channelsMap.get(channel).put(userId, usersMap.get(userId));
            return true;
        }
    }
    
    // removers
    public static boolean disconnectUser(int handlerId) {
        User currUser = usersMap.get(getUserByHndlrId(handlerId));
        if(currUser.isLoggedIn()){
            currUser.logout();
            currUser.setCurrHandlerId(-1);
            for(String currChannel : currUser.getChannels().values()){
                SingletonDataBase.removeUserFromChannel(currChannel, currUser.getUserId());
            }
            currUser.clearChannels();
            return true;
        }
        return false;
    }

    public static void removeHandler(int handlerId) {
        handlersMap.remove(handlerId);
    }

    public static void removeUserFromChannel(String channel, int userId){   
        channelsMap.get(channel).remove(userId);
    }

    public static boolean unsubscribe(int userId, int subscriptionId){
        User currUser = usersMap.get(userId);
        String channel = currUser.getChannels().get(subscriptionId);
        if(currUser.unsubscribe(subscriptionId)){
            channelsMap.get(channel).remove(userId);
            return true;
        }       
        return false;
    }

    // checkers
    public static boolean isChannelExists(String channel){
        return channelsMap.containsKey(channel);
    }

    public static boolean isSubscribedToChannel(String channel, int userId){
        return channelsMap.get(channel).containsKey(userId);
    }

    public static boolean isHandlerExists(int handlerId){
        return handlersMap.containsKey(handlerId);
    }
}