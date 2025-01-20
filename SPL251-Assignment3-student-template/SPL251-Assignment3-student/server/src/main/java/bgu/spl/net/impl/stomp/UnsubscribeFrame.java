package bgu.spl.net.impl.stomp;

public class UnsubscribeFrame implements StompFrame {
    
    // fields
    private int frameId;
    private String id;
    private String[] message;

    // constructor
    public UnsubscribeFrame(String[] message){
        this.message = message;
        if(message[1].equals("id")){
            this.frameId = 0;
            this.id = message[1];
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
