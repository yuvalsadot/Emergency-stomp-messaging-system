#pragma once

#include <vector>
#include <string>
#include <unordered_map>
#include <mutex>

using std::vector;
using std::string;
using std::unordered_map;

class User
{
    private:
        string userName;
        bool loggedIn;
        unordered_map<string, int> channelToSubId;
        unordered_map<int, string> receiptIdToCommand;
        unordered_map<int, string> waitingForReceipt;
        int subIdCounter;
        int receiptIdCounter;
        

    public:
        User();
        // getters
        int getReceiptId();
        int getSubId();
        void incrementSubId();
        string getCommandByReceipt(int recId);
        string getName();
        int getSubIdByChannel(string &channel);

        void joinChannel(string &channel, int subId);
        void exitChannel(string &channel, int subId);
        
        void setName(string& name);
        bool isLoggedIn();
        void commandAcknowledged(int recId);
        void resetUser();
        void receiptCommand(int id, string cmd);
        bool isSubscribed(string channel);
        void setConnect(bool connected);
};
