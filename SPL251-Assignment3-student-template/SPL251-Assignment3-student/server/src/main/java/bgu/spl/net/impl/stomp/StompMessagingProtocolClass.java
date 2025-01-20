package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.Connections;

public class StompMessagingProtocolClass implements StompMessagingProtocol<String[]>{

    // fields
    private boolean shouldTerminate = false;
    public StompFrame frame;
    int connectionId;
    int currFrameId;
    Connections<String[]> connections;
    
    // methods
    @Override
    public void start(int connectionId, Connections<String[]> connections){
        this.connectionId = connectionId;
        this.currFrameId = 0;
        this.connections = connections;
    }
    
    @Override
    public void process(String[] message){
        String currStompCmd = message[0];
        if (currStompCmd.equals("CONNECT")){
            frame = new ConnectFrame(message);
        }
        else if (currStompCmd.equals("SEND")){
            frame = new SendFrame(message);
        }
        else if (currStompCmd.equals("SUBSCRIBE")){
            frame = new SubscribeFrame(message);
        }
        else if (currStompCmd.equals("UNSUBSCRIBE")){
            frame = new UnsubscribeFrame(message);
        }
        else if (currStompCmd.equals("DISCONNECT")){
            frame = new DisconnectFrame(message);
        }
        else{
            // error
        }

        frame.setFrameId(currFrameId);
        currFrameId++;
        String[] response = frame.handle();
        connections.send(connectionId, response);
    }

    @Override
    public boolean shouldTerminate(){
        return shouldTerminate;
        
    }

}