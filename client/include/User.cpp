#include "User.h"

User::User()
{
}

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
    return string();
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
}

bool User::isLoggedIn()
{
    return false;
}

void User::commandAck(int recId)
{
}

void User::resetUser()
{
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
