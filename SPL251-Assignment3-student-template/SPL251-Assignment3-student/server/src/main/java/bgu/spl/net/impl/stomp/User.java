package bgu.spl.net.impl.stomp;

import java.util.concurrent.ConcurrentHashMap;

public class User {
    // fields
    private int userId;
    private String userName;
    private String passcode;
    private boolean isLoggedIn;
    private int currHandlerId;
    private ConcurrentHashMap<Integer, String> channels;

    // constructor
    public User(String userName, String passcode, int handlerId){
        this.userId = 0;
        this.userName = userName;
        this.passcode = passcode;
        this.isLoggedIn = false;
        this.currHandlerId = handlerId;
        this.channels = new ConcurrentHashMap<Integer, String>();
    }

    // methods
    public int getUserId(){
        return this.userId;
    }

    public String getUserName(){
        return this.userName;
    }

    public String getPasscode(){
        return this.passcode;
    }

    public boolean isSubscribed(String channel){
        return channels.containsValue(channel);
    }

    public boolean subscribe(String channel, int subId){
        if(!isSubscribed(channel)){
            this.channels.put(subId, channel);
            return true;
        }
        return false;
    }

    public boolean unsubscribe(String channel, int subId){
        if(isSubscribed(channel)){
            this.channels.remove(subId);
            return true;
        }
        return false;
    }

    public boolean isLoggedIn(){
        return this.isLoggedIn;
    }

    public void login(){
        this.isLoggedIn = true;
    }

    public void logout(){
        this.isLoggedIn = false;
    }

    public void setUserId(int userId){
        this.userId = userId;
    }

    public int getCurrHandlerId(){
        return this.currHandlerId;
    }

    public void setCurrHandlerId(int handlerId){
        this.currHandlerId = handlerId;
    }
}
