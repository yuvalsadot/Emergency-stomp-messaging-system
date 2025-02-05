#include "StompProtocol.h"
#include <string>
#include <iostream>
#include "ConnectionHandler.h" 
#include "Frame.h"
#include "Channel.h"
#include "User.h"
#include "event.h"
#include <unordered_map>
#include <vector>
#include <fstream>
#include "receivedFramesFromServer.h"
extern bool isLoggedIn;


// constructor
StompProtocol::StompProtocol(ConnectionHandler & ch, User & user) : ch(ch), user(user), 
isConnected(true), channels(){}

// destructor
StompProtocol::~StompProtocol(){
    for(std::unordered_map<string, Channel*>::iterator it = channels.begin(); it != channels.end(); it++){
        channels.erase(it);
        delete it->second;
    }
}

// methods
void StompProtocol::proccessKeyboardInput(string &input)
{
    Frame frame = Frame(input);
    string messageType = frame.getFrameType();
    string operation = "";
    if(messageType == "login"){
        if(user.isLoggedIn()){
            std::cout << "User is already logged in" << std::endl;
        }
        else{
            operation = frame.connectFrame();
            string name = frame.findName();
            user.setName(name);
        }
        if(operation != ""){
            ch.sendFrameAscii(operation, '\0');
        }
    }
    else if (isLoggedIn){
        if(messageType == "join"){
            int subId = user.getSubId();
            int recId = user.getReceiptId();
            operation = frame.subscribeFrame(subId, recId);
            user.receiptCommand(recId, "Joined channel " + frame.getCmd());
        }
        else if(messageType == "exit"){
            int id = user.getSubIdByChannel(frame.getCmd());
            int recId = user.getReceiptId();
            operation = frame.unsubscribeFrame(id, recId);
            channels.erase(frame.getCmd());
            user.receiptCommand(recId, "Exited channel " + frame.getCmd());
        }
        else if(messageType == "logout"){
            int recId = user.getReceiptId();
            operation = frame.disconnectFrame(recId);
            user.receiptCommand(recId, "logout");
            isConnected = false;
            isLoggedIn = false;
        }
        else if(messageType == "report"){
            string frameName = frame.getCmd();
            names_and_events newEvents = parseEventsFile(frameName);
            string channel = newEvents.channel_name;
            if(user.isSubscribed(channel)){
                vector<string> sendFrames = frame.reportFrame(frameName, user.getName());
                vector<Event> events = newEvents.events;
                for(string event : sendFrames){
                    ch.sendFrameAscii(event, '\0');
                }
                std::cout << "reported" << std::endl;
            }
            else{
                std::cout << "User is not subscribed to the channel" << std::endl;
            }
        }
        else if(messageType == "summary"){
            string cmd = frame.getCmd();
            // get channel name
            string channel = "";
            while(cmd.substr(0, 1) != " "){
                channel += cmd.substr(0, 1);
                cmd = cmd.substr(1);
            }
            cmd = cmd.substr(1);
            // get user name
            string name = "";
            while(cmd.substr(0, 1) != " "){
                name += cmd.substr(0, 1);
                cmd = cmd.substr(1);
            }
            cmd = cmd.substr(1);
            // get file name
            string fileName = "";
            int i = 0;
            int len = cmd.length();
            while(i < len){
                fileName += cmd.substr(0, 1);
                cmd = cmd.substr(1);
                i++;
            }
            std::unordered_map<string, Channel*>::iterator it = channels.find(channel);
            if(it != channels.end()){
                it->second->summary(name, fileName);
            }
            else{
                std::cout << "Channel does not exist" << std::endl;
            }
        }
        else{
            std::cout << "Invalid command" << std::endl;
        }

        if(operation != ""){
            ch.sendFrameAscii(operation, '\0');
        }
    }
    else{
        std::cout << "You need to log in first" << std::endl;
    }
}

void StompProtocol::processServer(string &input)
{
    ReceivedFramesFromServer rFrame(input);
    string messageType = rFrame.getType();
    if(messageType == "CONNECTED"){
        std::cout << "Login successful" << std::endl;
        user.setConnect(true);
    }
    else if(messageType == "ERROR"){
        std::cout << rFrame.getErrorMsg() << std::endl;
        std::cout << "user is logged out" << std::endl;
        isLoggedIn = false;
        isConnected = false;
        for(std::unordered_map<string, Channel*>::iterator it = channels.begin(); it != channels.end(); it++){
            channels.erase(it);
            delete it->second;
        }
        user.resetUser();
        ch.close();
    }
    else if(messageType == "RECEIPT"){
        int recId = rFrame.receiptId();
        user.commandAcknowledged(recId);
        string cmd = user.getCommandByReceipt(recId);
        if(cmd != "logout"){
            size_t pos = user.getCommandByReceipt(recId).find("channel") + 8;
            string channel = user.getCommandByReceipt(recId).substr(pos);
            if(cmd.substr(0, 1) == "J"){
                if(channels.find(channel) == channels.end()){
                    Channel *newChannel = new Channel(channel);
                    std::pair<string, Channel*> newPair(channel, newChannel);
                    channels.insert(newPair);
                }
                user.joinChannel(channel, user.getSubId());
                user.incrementSubId();                
            }
            else{
                user.exitChannel(channel, recId);
                std::unordered_map<string, Channel*>::iterator it = channels.find(channel);
                if(it != channels.end()){
                    channels.erase(it);
                    delete it->second;
                }
            }
            std::cout << cmd << std::endl;
        }
        else{
            std::cout << "user is logged out" << std::endl;
            for(auto it = channels.begin(); it != channels.end();){
                delete it->second;
                it = channels.erase(it);
            }
            user.resetUser();
            ch.close();
        }       
    }
    else if(messageType == "MESSAGE"){
        string frame = rFrame.getFrame();
        // get channel name
        string tmp = frame;
        string channelName = "";
        int start = tmp.find("destination:/") + 13;
        int end = tmp.find("\n", start) - 1;
        channelName = tmp.substr(start, end - start + 1);
        // add event to channel by username
        string sendingUser = rFrame.getSendingUser();
        int startBody = tmp.find("\n\n") + 2;
        Event *event = new Event(frame.substr(startBody));
        channels[channelName]->addChannelEvent(sendingUser, *event);
    }     
}