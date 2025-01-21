package bgu.spl.net.impl.stomp;

public class ConnectFrame implements StompFrame{

    // fields
    private String acceptVersion;
    private String host;
    private String login;
    private String passcode;
    private int connectionId;
    private String[] message;

    // constructor
    public ConnectFrame(String[] message, int connectionId){
        this.acceptVersion = message[2];
        this.host = message[4];
        this.login = message[6];
        this.passcode = message[8];
        this. connectionId = connectionId;
        this.message = message;
    }

    // methods
    public String[] handle(){
        boolean connected = false;
        int userId = SingletonDataBase.getUserByUsrnm(this.login);
        if(userId == -1){
            User newUser = new User(this.login, this.passcode, this.connectionId);
            SingletonDataBase.addNewUser(newUser);
            connected = true;
        }
        else{
            if(SingletonDataBase.usersMap.get(userId).getPasscode() != this.passcode){
                return errorHandle("Wrong passcode");
            }
            else if(!SingletonDataBase.usersMap.get(userId).isLoggedIn()){
                SingletonDataBase.addExistingUser(SingletonDataBase.usersMap.get(userId), this.connectionId);
                connected = true;
            }
            else {
                return errorHandle("User already connected");
            }
        }
        if (connected){
            String[] connectedFrame = {"CONNECTED", "\nversion:", this.acceptVersion, "\n", "\u0000"};
            return connectedFrame;
        }
        else {
            return errorHandle("General Error");
        }
    }
    
    public String[] errorHandle(String message){
        if (message.equals("Wrong passcode")){
            String[] errorFrame = {"ERROR", "message", ": Wrong passcode", "\n", "The message:", "\n-----", "\n" + this.message, "\n-----", "Try again with a different passcode", "\u0000"};
            return errorFrame;
        }
        else if (message.equals("User already connected")){
            String[] errorFrame = {"ERROR", "message", ": User already connected", "\n", "The message:", "\n-----", "\n" + this.message, "\n-----", "You are already logged in, no need to connect again", "\u0000"};
            return errorFrame;
        }
        else{
            String[] errorFrame = {"ERROR", "message", ": General error", "\n", "The message:", "\n-----", "\n" + this.message, "\n-----", "The server could not connect you right now, please try again later", "\u0000"};
            return errorFrame;
        }
    }

    public boolean shouldTerminate(){
        return false;
    }
}
