#include <string>
#include <iostream>
#include <thread>
#include <mutex>
#include "keyBoardThread.h"
extern bool isLoggedIn;

// constructor
keyBoardThread::keyBoardThread(StompProtocol &protocol, ConnectionHandler &ch):protocol(protocol), ch(ch){}

// methods
void keyBoardThread::run(){
    while(1){
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize); // Get user input
        std::string line(buf); // Convert char array to string
        int i = line.find(" ");
        if(line.substr(0, i)=="login"){
            if(!isLoggedIn){//if not log in create connection hendler
                std::string cmd = line.substr(i+1);
                i = cmd.find(":");
                std::string host = cmd.substr(0, i);
                cmd = cmd.substr(i + 1);
                i=cmd.find(" ");
                int port = stoi(cmd.substr(0, i));
                ch.setValues(host, port);
                if (!ch.connect()) {
                    std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
                }
                isLoggedIn=true;
            }
        }
        protocol.proccessKeyboardInput(line);//will also cover if we alredy were logged in
    }
}