#include "Channel.h"
#include "StompProtocol.h"
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
    if (!isLoggedIn) {
        std::cout << "You must be logged in to view channel summaries." << std::endl;
        return;
    }
    std::ofstream file;
    file.open(fileName);
    if (!file.is_open()) {
        std::cout << "Error opening file." << std::endl;
        return;
    }
    int totalReports = 0;
    int activeReports = 0;
    int forcesAtScene = 0;
    std::vector<std::pair<std::string, Event>> sortedReports;
    if (userUpdates.find(user) != userUpdates.end()){
        for (const Event& event : userUpdates[user]) {
            totalReports++;
            if(event.isActive()){
                activeReports++;
            }
            if(event.isForcesArrivalAtScene()){
                forcesAtScene++;
            }
            sortedReports.push_back({user, event});
        }  
    }
    std::sort(sortedReports.begin(), sortedReports.end(), [](const std::pair<std::string, Event>& a, const std::pair<std::string, Event>& b) {
        if (a.second.get_date_time() != b.second.get_date_time())
            return a.second.get_date_time() < b.second.get_date_time();
        return a.second.get_name() < b.second.get_name();
    });
    file << "Channel: " << name << std::endl;
    file << "Stats: " << std::endl;
    file << "Total: " << totalReports << std::endl;
    file << "Active: " << activeReports << std::endl;
    file << "Forces arrival at scene: " << forcesAtScene << std::endl;
    file << "\nEvent Reports: " << std::endl;
    int reportNum = 1;
    for(const auto& report : sortedReports) {
        const Event& event = report.second;
        file << "Report_" << reportNum++ << ":" << std::endl;
        file << "   City: " << event.get_city() << std::endl;
        file << "   Date time: " << epochToDateTime(event.get_date_time()) << std::endl;
        file << "   Event name: " << event.get_name() << std::endl;
        std::string summary = event.get_description().substr(0, 27);
        if (event.get_description().length() > 27) {
            summary += "...";
        }
        file << "   Summary: " << summary << "\n" << std::endl;
    }
    file.close();
    std::cout << "Summary written to " << fileName << std::endl;
}

string Channel::getName()
{
    return name;
}

void Channel::addChannelEvent(string name, Event &event)
{
    if (userUpdates.find(name) == userUpdates.end()) {
        std::pair<std::string, std::vector<Event>> user(name, std::vector<Event>());
        userUpdates.insert(user);
    }
    userUpdates[name].push_back(event);
}

string Channel::epochToDateTime(time_t epoch) {
    struct tm *timeinfo;
    char buffer[20];  // "dd/mm/yy hh:mm"
    timeinfo = localtime(&epoch);
    strftime(buffer, sizeof(buffer), "%d/%m/%y %H:%M", timeinfo);
    return string(buffer);
}