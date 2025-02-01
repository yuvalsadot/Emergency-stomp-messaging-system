#pragma once

#include "StompProtocol.h"
#include "ConnectionHandler.h"
class keyBoardThread
{
    private:
        StompProtocol &protocol;
        ConnectionHandler &ch;
    public:
        // Constructor
        keyBoardThread(StompProtocol &protocol, ConnectionHandler &ch);
        
        // Run the thread
        void run();
};