#include "User.h"
#include <mutex>
#include <unordered_map>

User::User(): userName(), loggedIn(false), channelToSubId(),
 receiptIdToCommand(), subIdCounter(0), receiptIdCounter(0)
    , waitingForReceipt() {}

// Destructor
User::~User()
{
}

int User::getReceiptId()
{
    return 0;
}

int User::getSubId()
{
    return 0;
}

string User::getCommandByReceipt(int recId)
{
    return string();
}

string User::getName()
{
    return userName;
}

int User::getSubIdByTopic(string &channel)
{
    return 0;
}

void User::joinChannel(string &channel, int subId)
{
}

void User::exitChannel(string &channel, int subId)
{
}

void User::setName(string &name)
{
    userName = name;
}

bool User::isLoggedIn()
{
    return loggedIn;
}

void User::commandAck(int recId)
{
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
}

bool User::isSubscribed(string channel)
{
    return false;
}

void User::setConnect(bool connected)
{
}
