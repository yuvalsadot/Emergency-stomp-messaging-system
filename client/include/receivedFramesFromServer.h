#pragma once
#include <string>
using std::string;

class ReceivedFramesFromServer
{
    private:
        string input;
        string type;
        string frame;

    public:
        ReceivedFramesFromServer(string &input);
        virtual ~ReceivedFramesFromServer();
        string getType();
        int receiptId();
        string getErrorMsg();
        string getFrame();
};