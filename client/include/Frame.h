#pragma once

#include <string>
#include <vector>
#include "event.h"

using std::string;
using std::vector;

//  A class that represents a frame in the STOMP protocol 
//  Wthich receives a string and parses it into a frame
class Frame
{
    private:
    string input;
    string command;
    string frameType;
    string channel;

    public:
    Frame(string &input);
    // gettters
    string getFrameType();
    string& getCmd();
    // 5 options for a frame
    string connectFrame();
    string subscribeFrame(int subId, int recId);
    string unsubscribeFrame(int subId, int recId);
    string disconnectFrame(int recId);
    vector<string> reportFrame(string file, string user);
    string findName();
};
