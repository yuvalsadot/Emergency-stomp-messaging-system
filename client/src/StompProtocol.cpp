#include "StompProtocol.h"



StompProtocol::StompProtocol(ConnectionHandler & ch, User & user) : ch(connectionHandler), user(user), 
isConnected(true), channels(){}
{}

void StompProtocol::proccessKeyboardInput(string &input)
{

}

void StompProtocol::processServer(string & input)
{
}

bool StompProtocol::isConnectedToServer()
{
return isConnected;
}

string StompProtocol::report(string frame)
{
return string();
}
