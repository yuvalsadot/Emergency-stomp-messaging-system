package bgu.spl.net.impl.stomp;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectFrame implements StompFrameAction<StompFrameRaw>{

    // fields
    private String acceptVersion;
    private String host;
    private String login;
    private String passcode;
    private int handlerId;
    private StompFrameRaw message;
    boolean shouldTerminate = false;

    // constructor
    public ConnectFrame(StompFrameRaw message, int handlerId){
        this.acceptVersion = message.getHeaders().get("accept-version");
        this.host = message.getHeaders().get("host");
        this.login = message.getHeaders().get("login");
        this.passcode = message.getHeaders().get("passcode");
        this. handlerId = handlerId;
        this.message = message;
    }

    // methods
    @Override
    public StompFrameRaw handle(){
        boolean connected = false;
        int userId = SingletonDataBase.getUserByUsrnm(this.login);
        if(userId == -1){
            User newUser = new User(this.login, this.passcode, this.handlerId);
            SingletonDataBase.addNewUser(newUser);
            connected = true;
        }
        else{
            if(!SingletonDataBase.usersMap.get(userId).getPasscode().equals(this.passcode)){
                return errorHandle("Wrong passcode");
            }
            else if(!SingletonDataBase.usersMap.get(userId).isLoggedIn()){
                SingletonDataBase.addExistingUser(SingletonDataBase.usersMap.get(userId), this.handlerId);
                connected = true;
            }
            else {
                return errorHandle("User already connected");
            }
        }
        if (connected){
            String command = "CONNECTED";
            ConcurrentHashMap<String, String> headers = new ConcurrentHashMap<>();
            headers.put("version", this.acceptVersion);
            String body = "";
            return new StompFrameRaw(command, headers, body);
        }
        else {
            return errorHandle("General Error");
        }
    }
    
    @Override
    public StompFrameRaw errorHandle(String message){
        SingletonDataBase.disconnectUser(handlerId);
        shouldTerminate = true;
        String command = "ERROR";
        String body = "The message:\n-----\n" + message2String(this.message) + "\n-----\n";
        ConcurrentHashMap<String, String> headers = new ConcurrentHashMap<>();
        if (message.equals("Wrong passcode")){
            headers.put("message", "Wrong passcode");
            body += "Try again with a different passcode";
        }
        else if (message.equals("User already connected")){
            headers.put("message", "User already connected");
            body += "You are already logged in, no need to connect again";
        }
        else{
            headers.put("message", "General error");
            body += "The server could not connect you right now, please try again later";
        }
        return new StompFrameRaw(command, headers, body);
    }

    @Override
    public boolean shouldTerminate(){
        return shouldTerminate;
    }

    private String message2String(StompFrameRaw message){
        String output = message.getCommand() + "\n";
        for (String key : message.getHeaders().keySet()){
            output += key + ":" + message.getHeaders().get(key) + "\n";
        }
        output += "\n" + message.getBody();
        return output;
    }
}
