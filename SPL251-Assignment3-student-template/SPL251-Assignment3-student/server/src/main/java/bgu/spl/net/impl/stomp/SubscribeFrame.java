package bgu.spl.net.api;
import bgu.spl.net.srv.Connections;

public class SubscribeFrame implements StompFrame{
    // fields
    private int frameId;
    private String destination;
    private String id;
    private String[] message;
    private Connections<String[]> connections;

    // constructor
    public SubscribeFrame(String[] message, Connections<String[]> connections){
        int counter = 1;
        int destination = 0, id = 0;
        while((destination != 0 || id != 0) && counter <= 3 && counter < message.length){
            if(message[counter].equals("destination")){
                destination = counter;
            }
            else if(message[counter].equals("id")){
                id = counter;
            }
            else{
                break;
            }
            counter += 2;
        }
        if(destination == 0 || id == 0){
            return; //Error
        }
        else{
            this.frameId = 0;
            this.destination = message[destination + 1];
            this.id = message[id + 1];
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


}
