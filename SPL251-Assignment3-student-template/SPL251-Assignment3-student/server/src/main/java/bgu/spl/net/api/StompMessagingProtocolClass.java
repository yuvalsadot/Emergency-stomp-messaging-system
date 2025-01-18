package bgu.spl.net.api;

import bgu.spl.net.srv.Connections;

public class StompMessagingProtocolClass implements StompMessagingProtocol<String[]>{

    // fields
    private boolean shouldTerminate = false;
    
    // methods
    @Override
    public void start(int connectionId, Connections<String[]> connections){

    }
    
    @Override
    public void process(String[] message){
        String currStompCmd = message[0];
        if (currStompCmd.equals("CONNECT")){
            connectedCmd(message);
        }
        else if (currStompCmd.equals("SEND")){
            messageCmd(message);
        }
        else if (currStompCmd.equals("SUBSCRIBE")){
            subscribeCmd(message);
        }
        else if (currStompCmd.equals("UNSUBSCRIBE")){
            unsubsrcibeCmd(message);
        }
        else if (currStompCmd.equals("DISCONNECT")){
            disconnectCmd(message);
        }
        else{
            errorHandle(message);
        }
    }

    public void connectedCmd(String[] message){
        try{
            int counter = 1;
            int header1 = 0, header2 = 0, header3 = 0, header4 = 0;
            while((header1 != 0 || header2 != 0 || header3 != 0 || header4 != 0) && counter <= 7 && counter < message.length){
                if(message[counter].equals("accept-version")){
                    header1 = counter;
                }
                else if(message[counter].equals("host")){
                    header2 = counter;
                }
                else if(message[counter].equals("login")){
                    header3 = counter;
                }
                else if(message[counter].equals("passcode")){
                    header4 = counter;
                }
                else{
                    break;
                }
                counter += 2;
            }
            if(header1 == 0 || header2 == 0 || header3 == 0 || header4 == 0){
                return; //TODO
            }
            else{
                // handle message
            }
        }
        //TODO
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void messageCmd(String[] message){
        try{
            int header1 = 0;
            if(1 < message.length){
                if(message[1].equals("destination")){
                    header1 = 1;
                }
                else{
                    return;
                }
            }
            // handle message
        }
        //TODO
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void subscribeCmd(String[] message){
        try{
            int counter = 1;
            int header1 = 0, header2 = 0;
            while((header1 != 0 || header2 != 0) && counter <= 3 && counter < message.length){
                if(message[counter].equals("destination")){
                    header1 = counter;
                }
                else if(message[counter].equals("id")){
                    header2 = counter;
                }
                else{
                    break;
                }
                counter += 2;
            }
            if(header1 == 0 || header2 == 0){
                return; //TODO
            }
            else{
                // handle message
            }
        }
        //TODO
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void unsubsrcibeCmd(String[] message){
        try{
            int header1 = 0;
            if(1 < message.length){
                if(message[1].equals("id")){
                    header1 = 1;
                }
                else{
                    return;
                }
            }
            // handle message
        }
        //TODO
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void disconnectCmd(String[] message){
        try{
            int header1 = 0;
            if(1 < message.length){
                if(message[1].equals("receipt")){
                    header1 = 1;
                }
                else{
                    return;
                }
            }
            // handle message
        }
        //TODO
        catch (Exception e){
            e.printStackTrace();
        }
    }

	
    public void errorHandle(String[] errorMsg)
    {
        String[] errorMsgToSend = new String[1 << 10];
        errorMsgToSend[0] = "ERROR";
        // TODO
    }
    @Override
    public boolean shouldTerminate(){
        return shouldTerminate;
        
    }

}