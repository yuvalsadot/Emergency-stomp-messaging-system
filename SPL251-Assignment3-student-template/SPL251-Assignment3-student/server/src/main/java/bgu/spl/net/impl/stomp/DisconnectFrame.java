package bgu.spl.net.impl.stomp;

public class DisconnectFrame implements StompFrame {
    
    // fields
    private int frameId;
    private String receipt;
    private String[] message;

    // constructor
    public DisconnectFrame(String[] message){
        this.frameId = 0;
        this.receipt = message[2];
        this.message = message;
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

    public String[] errorHandle(String message){

    }
}
