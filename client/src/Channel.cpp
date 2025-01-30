#include "Channel.h"
extern bool isLoggedIn;

Channel::Channel(string name) : name(name), userUpdates() {}
Channel::~Channel() {
}

void Channel::summary(string user, string fileName) {
}

string Channel::getName()
{
    return name;
}

void Channel::addChannelEvent(string name, string update) {
};
