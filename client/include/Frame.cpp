#include "Frame.h"

Frame::Frame(string &input) : input(input), command(), frameType(), channel()
{
    string s = "";
    int counter = 0;
    bool flag = false;
    for(int i = 0; i < input.length() && !flag; i++)
    {
        if(input[i] == ' ')
        {
            flag = true;
        }
     else if(input[i] != input.length() - 1)
     {
            s.append(&input[i], 1);
     }
     else if(i==(int)input.length()-1)
     { //for commands with one word
           s.append(&input[i], 1);
           flag=true;
     }
       counter++;
    }
    frameType = s;
    command = input.substr(counter); //the command without the first word
}



Frame::~Frame()
{

}

string Frame::getFrameCommand()
{
    return frameType;
}

string &Frame::getCmd()
{
    // TODO: insert return statement here
}

string Frame::connectFrame()
{
    return string();
}

string Frame::sendFrame()
{
    return string();
}

string Frame::subscribeFrame(int subId, int recId)
{
    return string();
}

string Frame::unsubscribeFrame(int subId, int recId)
{
    return string();
}

string Frame::disconnectFrame(int recId)
{
    return string();
}

vector<string> Frame::reportFrame(string file, string user)
{
    return vector<string>();
}

string Frame::findName()
{
return string();
}
