package bgu.spl.net.impl.stomp;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
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
    private static AtomicInteger currUserId;

    // constructor
    private SingletonDataBase() {
        usersMap = new ConcurrentHashMap<>();
        channelsMap = new ConcurrentHashMap<>();
        handlersMap = new ConcurrentHashMap<>();
        currUserId = new AtomicInteger(0);
    }

    // methods
    // getters
    public static SingletonDataBase getInstance() {
        return SingletonDataBaseHolder.instance;
    }

    public static int getUserByUsrnm(String userName) {
        final int[] userId = {-1}; // Use an array to modify inside lambda
        usersMap.forEach((id, currUser) -> {
            if (currUser.getUserName().equals(userName)) {
                userId[0] = id;
            }
        });
        return userId[0];
    }

    public static int getUserByHndlrId(int handlerId) {
        final int[] currHandlerId = {-1};
        usersMap.forEach((id, currUser) -> {
            if (currUser.getCurrHandlerId() == handlerId) {
                currHandlerId[0] = id;
            }
        });
        return currHandlerId[0];
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
        newUser.setUserId(currUserId.get());
        usersMap.put(currUserId.get(), newUser);
        currUserId.getAndIncrement();
    }

    public static void addExistingUser(User existingUser, int handlerId) {
        existingUser.login();
        existingUser.setCurrHandlerId(handlerId);
    }

    public static synchronized boolean addUserToChannel(String channel, int userId, int subscriptionId) {
        channelsMap.computeIfAbsent(channel, k -> new ConcurrentHashMap<>());
        if (usersMap.get(userId).subscribe(channel, subscriptionId)){
            channelsMap.get(channel).put(userId, usersMap.get(userId));
            return true;
        }
        return false;
    }
    
    // removers
    public static synchronized boolean disconnectUser(int handlerId) {
        int id = getUserByHndlrId(handlerId);
        if (id != -1) {
            User currUser = usersMap.get(getUserByHndlrId(handlerId));
            if (currUser.isLoggedIn()){
                currUser.logout();
                currUser.setCurrHandlerId(-1);
                currUser.getChannels().values().forEach(currChannel -> {
                    SingletonDataBase.removeUserFromChannel(currChannel, id);
                });
                currUser.clearChannels();
                return true;
            }
        }
        return false;
    }

    public static void removeHandler(int handlerId) {
        handlersMap.remove(handlerId);
    }

    public static void removeUserFromChannel(String channel, int userId){   
        if (channelsMap.containsKey(channel)){
            channelsMap.get(channel).remove(userId);
        }
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