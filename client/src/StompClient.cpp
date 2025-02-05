#include <thread>
#include <mutex>
#include "User.h"
#include "StompProtocol.h"
#include "ConnectionHandler.h"
#include "Frame.h"
#include "Channel.h"
#include "keyBoardThread.h"
bool isLoggedIn = false;


int main(int argc, char *argv[])
{
	(void)argc;
    (void)argv;
	User* user = new User();
	ConnectionHandler ch("", 0);
	StompProtocol sp(ch, *user);
	keyBoardThread kbt(sp, ch);
	std::thread t1(&keyBoardThread::run, &kbt);
	while(1)
	{
		if(isLoggedIn)
		{
			string input;
			if(ch.getFrameAscii(input, '\0'))
			{
				if(input != "")
				{
					sp.processServer(input);
				}
				else{
					std::cout << "The server had disconnect you" << std::endl;
					break;
				}
				input = "";
			}
		}
	}
	
	delete user;
	return 0;

}
//login 127.0.0.1:7777 sada 9799
//java -cp classes bgu.spl.net.impl.stomp.StompServer 7777 tpc
// /workspaces/Assignment_3/client/data/events1.json