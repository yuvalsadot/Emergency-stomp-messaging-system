package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.Connections;

public class StompMessagingProtocolClass implements StompMessagingProtocol<String[]>{

    // fields
    private boolean shouldTerminate = false;
    public StompFrame frame;
    int connectionId;
    Connections<String[]> connections;
    
    // methods
    @Override
    public void start(int connectionId, Connections<String[]> connections){
        this.connectionId = connectionId;
        this.connections = connections;
    }
    
    @Override
    public void process(String[] message){
        // remove the first character if it is '\n' or ':'
        for (String s : message){
            if (s.length() > 0){
                if (s.charAt(0) == '\n' || s.charAt(0) == ':'){
                    s = s.substring(1);
                }
            }
        }

        String currStompCmd = message[0];
        if (currStompCmd.equals("CONNECT")){
            frame = new ConnectFrame(message, connections);
        }
        else if (currStompCmd.equals("SEND")){
            frame = new SendFrame(message, connections);
        }
        else if (currStompCmd.equals("SUBSCRIBE")){
            frame = new SubscribeFrame(message, connections);
        }
        else if (currStompCmd.equals("UNSUBSCRIBE")){
            frame = new UnsubscribeFrame(message, connections);
        }
        else if (currStompCmd.equals("DISCONNECT")){
            frame = new DisconnectFrame(message, connections);
        }
        else{
            // error
        }

        frame.setFrameId(0);
        String[] response = frame.handle();
        connections.send(connectionId, response);
    }

    @Override
    public boolean shouldTerminate(){
        return shouldTerminate;
        
    }

}