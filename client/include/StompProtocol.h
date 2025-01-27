#pragma once
#include <string>
#include <iostream>
#include <boost/asio.hpp>
#include "ConnectionHandler.h"
#include "Frame.h"
#include "User.h"
#include "Game.h"
#include "receivedFrames.h"
// TODO: implement the STOMP protocol
class StompProtocol
{
private:
ConnectionHandler &ch;
User &user;
bool isConnected;
unordered_map<string, Channel*> channels;

public:
StompProtocol(ConnectionHandler& ch, User& user);
void proccessKeyboardInput(string &input);
void processServer(string &input);
bool isConnectedToServer();
string report(string frame);

};
