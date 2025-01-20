package bgu.spl.net.impl.stomp;

public class SendFrame implements StompFrame {

    // fields
    private int frameId;
    private String destination;
    private String[] message;

    // constructor
    public SendFrame(String[] message){
        this.message = message;
        if(message[1].equals("destination")){
            this.frameId = 0;
            this.destination = message[1];
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

