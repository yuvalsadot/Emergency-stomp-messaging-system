package bgu.spl.net.impl.stomp;

public class DisconnectFrame implements StompFrame {
    
    // fields
    private int frameId;
    private String receipt;
    private String[] message;

    // constructor
    public DisconnectFrame(String[] message){
        this.message = message;
        if(message[1].equals("receipt")){
            this.frameId = 0;
            this.receipt = message[1];
            this.message = message;
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
