#include "User.h"
#include <mutex>
#include <unordered_map>


// constructor
User::User(): userName(), loggedIn(false), channelToSubId(),
 receiptIdToCommand(),waitingForReceipt(), subIdCounter(0), receiptIdCounter(0){}

// getters
int User::getReceiptId()
{
    receiptIdCounter++;
    return receiptIdCounter - 1;
}

int User::getSubId()
{
    return subIdCounter;
}

void User::incrementSubId()
{
    subIdCounter++;
}

string User::getCommandByReceipt(int recId)
{
    string cmd;
    unordered_map<int, string>::iterator it = receiptIdToCommand.find(recId);
    if (it != receiptIdToCommand.end())
    {
        cmd = it->second;
    }
    return cmd;
}

string User::getName()
{
    return userName;
}

int User::getSubIdByChannel(string &channel)
{
    auto it = channelToSubId.find(channel);
    int id = (it != channelToSubId.end()) ? it->second : -1;
    return id;
}

void User::joinChannel(string &channel, int subId)
{

    std::pair<std::string, int> newChannel(channel, subId);
    channelToSubId.insert(newChannel);
}

void User::exitChannel(string &channel, int subId)
{
    (void)subId;
    channelToSubId.erase(channel);
}

void User::setName(string &name)
{
    userName = name;
}

bool User::isLoggedIn()
{
    return loggedIn;
}

void User::commandAcknowledged(int recId)
{
    string cmd;
    std::unordered_map<int, string>::iterator it = waitingForReceipt.find(recId);
    if(it != waitingForReceipt.end()){
        cmd = it->second;
        std::pair<int, string> newReceipt(recId, cmd);
        receiptIdToCommand.insert(newReceipt);
        waitingForReceipt.erase(recId);
    }    
}

void User::resetUser()
{
    subIdCounter = 0;
    receiptIdCounter = 0;
    loggedIn = false;
    receiptIdToCommand.clear();
    channelToSubId.clear();
    waitingForReceipt.clear();
}

void User::receiptCommand(int id, string cmd)
{   
    std::pair<int, string> newReceipt(id, cmd);
    waitingForReceipt.insert(newReceipt);
}

bool User::isSubscribed(string channel)
{
    unordered_map<string, int>::iterator it = channelToSubId.find(channel);
    if (it != channelToSubId.end())
    {
        return true;
    }
    return false;
}

void User::setConnect(bool connected)
{
    loggedIn = connected;
}