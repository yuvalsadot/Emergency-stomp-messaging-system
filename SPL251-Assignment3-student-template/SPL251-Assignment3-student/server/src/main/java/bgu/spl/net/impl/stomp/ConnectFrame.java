package bgu.spl.net.impl.stomp;

public class ConnectFrame implements StompFrame{

    // fields
    private int frameId;
    private String acceptVersion;
    private String host;
    private String login;
    private String passcode;
    private String[] message;

    // constructor
    public ConnectFrame(String[] message){
        this.frameId = 0;
        this.acceptVersion = message[2];
        this.host = message[4];
        this.login = message[6];
        this.passcode = message[8];
        this.message = message;
    }

    // methods
    public String[] handle(){
        boolean connected = false;
        int userId = SingletonDataBase.getUser(this.login);
        if(userId == -1){
            String[] user = {this.login, this.passcode, "", ""};
            SingletonDataBase.addNew(user);
            connected = true;
        }
        else{
            if(SingletonDataBase.connectionIdToUserMap.get(userId)[1] != this.passcode){
                return errorHandle("Wrong password");
            }
            else if(SingletonDataBase.connectionIdToUserMap.get(userId)[2].equals("disconnected")){
                SingletonDataBase.addExisting(SingletonDataBase.connectionIdToUserMap.get(userId));
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

    public String[] getFrame(){
        return this.message;
    }

    public void setFrameId(int id){
        this.frameId = id;
    }

    //TODO - check if the error message is correct
    public String[] errorHandle(String message){
        if (message.equals("Wrong password")){
            String[] errorFrame = {"ERROR", "message", ": Wrong password", "\n", "The message:", "\n-----", "\n" + this.message, "\n-----", "Try again with a different passcode", "\u0000"};
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
}
