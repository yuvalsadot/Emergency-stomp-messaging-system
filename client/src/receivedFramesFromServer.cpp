#include "receivedFramesFromServer.h"


// constructor
ReceivedFramesFromServer::ReceivedFramesFromServer(string &input): input(input), type(""), frame(""){
    string start="";
    int counter = 0;
    bool flag = false;
    for(size_t i = 0; i < input.length() && !flag; i++)
    {
        if(input[i] == '\n')
        {
            flag = true;
        }
        else if(i != input.length() - 1)
        {
            start.append(&input[i], 1);
        }
        else if(i == input.length() - 1)
        { //for commands with one word
            start.append(&input[i], 1);
            flag = true;
        }
        counter++;
    }
    type = start;
    frame = input.substr(counter); //the command without the first word
}

string ReceivedFramesFromServer::getType()
{
    return type;
}

int ReceivedFramesFromServer::receiptId()
{
    int size = frame.find('\n') - frame.find(":") - 1;
    int id = stoi(frame.substr(frame.find(':') + 1, size));
    return id;
}

string ReceivedFramesFromServer::getErrorMsg()
{
    return frame;
}

string ReceivedFramesFromServer::getFrame()
{
    return frame;
}


string ReceivedFramesFromServer::getSendingUser()
{
    string userName;
    int start = frame.find("user: ") + 6;
    int end = frame.find('\n', start);
    userName = frame.substr(start, end - start);
    return userName;
}