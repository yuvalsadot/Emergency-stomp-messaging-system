#include "Channel.h"
#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <list>
#include <unordered_map>
#include <algorithm>
#include <string>

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
