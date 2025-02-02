#pragma once
#include <string>
#include <iostream>
#include <boost/asio.hpp>
#include "Frame.h"
#include "ConnectionHandler.h"
#include "User.h"


class Channel;

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
