package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.Connections;

public class ConnectFrame implements StompFrame{

    // fields
    private int frameId;
    private String acceptVersion;
    private String host;
    private String login;
    private String passcode;
    private String[] message;
    private Connections<String[]> connections;

    // constructor
    public ConnectFrame(String[] message, Connections<String[]> connections){
        int counter = 1;
        int acceptVersion = 0, host = 0, login = 0, passcode = 0;
        while((acceptVersion != 0 || host != 0 || login != 0 || passcode != 0) && counter <= 7 && counter < message.length){
            if(message[counter].equals("accept-version")){
                acceptVersion = counter;
            }
            else if(message[counter].equals("host")){
                host = counter;
            }
            else if(message[counter].equals("login")){
                login = counter;
            }
            else if(message[counter].equals("passcode")){
                passcode = counter;
            }
            else{
                break;
            }
            counter += 2;
        }
        if(acceptVersion == 0 || host == 0 || login == 0 || passcode == 0){
            // error
        }
        else{
            this.frameId = 0;
            this.acceptVersion = message[acceptVersion + 1];
            this.host = message[host + 1];
            this.login = message[login + 1];
            this.passcode = message[passcode + 1];
            this.message = message;
            this.connections = connections;
        }
    }

    // methods
    public String[] handle(){

    }

    public String[] getFrame(){
        return this.message;
    }

    public void setFrameId(int id){
        this.frameId = id;
    }

    public String[] errorHandle(){

    }


}
