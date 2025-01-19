package bgu.spl.net.api;
import bgu.spl.net.srv.Connections;

public class UnsubscribeFrame implements StompFrame {
    
    // fields
    private int frameId;
    private String id;
    private String[] message;
    private Connections<String[]> connections;

    // constructor
    public UnsubscribeFrame(String[] message, Connections<String[]> connections){
        this.message = message;
        if(message[1].equals("id")){
            this.frameId = 0;
            this.id = message[1];
            this.message = message;
            this.connections = connections;
        }
        else{
            // error

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
