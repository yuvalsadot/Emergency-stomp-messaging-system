#include <thread>
#include <mutex>
#include "User.h"
#include "StompProtocol.h"
#include "ConnectionHandler.h"
#include "Frame.h"
#include "Channel.h"
#include "keyBoardThread.h"


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
		if(isLogged)
		{
			std::cout << isLogged << std::endl;
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