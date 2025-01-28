#include "Frame.h"
#include <iostream>

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





string Frame::getFrameType()
{
    return frameType;
}

string &Frame::getCmd()
{
    return command;
}

string Frame::connectFrame()
{
    string s = "CONNECT\n";
    string nextHeader;
    string restOfCommand = command;
    int findNextHeader = command.find(' ');

    nextHeader = restOfCommand.substr(0, findNextHeader);
    s += "host:" + nextHeader + "\n";
    restOfCommand = restOfCommand.substr(findNextHeader + 1);
    findNextHeader = restOfCommand.find(" ");
    nextHeader = restOfCommand.substr(0, findNextHeader);
    s += "login:" + nextHeader + "\n";
    restOfCommand = restOfCommand.substr(findNextHeader + 1);
    s += "passcode:" + restOfCommand + "\n";
    s += "accept-version:1.2\n";
    s += '\0';
    return s;

}


string Frame::subscribeFrame(int subId, int recId)
{
    string s = "SUBSCRIBE\n";
    s += "destination:" + command + "\n";
    string header = "";
    string subIdStr = std::to_string(subId);
    string recIdStr = std::to_string(recId);
    s += "id:" + subIdStr + "\n";
    s += "receipt:" + recIdStr + "\n";
    s += '\0';
    return s;

}

string Frame::unsubscribeFrame(int subId, int recId)
{
    string s = "UNSUBSCRIBE\n";
    string subIdStr = std::to_string(subId);
    string recIdStr = std::to_string(recId);
    s += "id:" + subIdStr + "\n";
    s += "receipt:" + recIdStr + "\n";
    s += '\0';
    return s;
}

string Frame::disconnectFrame(int recId)
{
    string s = "DISCONNECT\n";
    string recIdStr = std::to_string(recId);
    s += "receipt:" + recIdStr + "\n";
    s += '\0';
    return s;
}

vector<string> Frame::reportFrame(string file, string user)
{
    std::vector<string> events;
    names_and_events parsedEvents = parseEventsFile(file);
    vector <Event> eventsVec = parsedEvents.events;
    for(Event event : eventsVec)
    {
        string s = "SEND\n";
        s += "destination:" + parsedEvents.channel_name + "\n";
        s += "event name:" + event.get_channel_name() + "\n";
        s += "city:" + event.get_city() + "\n";
        s += "date time:" + std::to_string(event.get_date_time()) + "\n";
        std::map<std::string, std::string> generalInformation = event.get_general_information();  
        s += "general information:\n";
        for(auto it = generalInformation.begin(); it != generalInformation.end(); it++)
        {
            s += it->first + ":" + it->second + "\n";
        }
        s += '\0';
       
        s += "description:" + event.get_description() + "\n";
        events.push_back(s);
    }
    return events;
    
}

string Frame::findName()
{
    string userPass = command.substr(command.find(" ") + 1);
    string name = " ";
    while(userPass.substr(0, 1) != " ")
    {
        name += userPass.substr(0, 1);
        userPass = userPass.substr(1);
    }
    return name;
}

/*string Frame::sendFrame()
{
    return string();
}*/
Frame::~Frame()
{

}