#pragma once
#include <string>
#include <iostream>
#include <boost/asio.hpp>
#include "ConnectionHandler.h"
#include "Frame.h"
#include "Channel.h"
#include "User.h"
extern bool isLoggedIn;

class StompProtocol
{
    private:
    ConnectionHandler &ch;
    User &user;
    bool isConnected;
    unordered_map<string, Channel*> channels;

    public:
    StompProtocol(ConnectionHandler& ch, User& user);
    ~StompProtocol();
    void proccessKeyboardInput(string &input);
    void processServer(string &input);
};
