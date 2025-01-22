package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.Server;

public class StompServer {

    public static void main(String[] args) {
        Server.threadPerClient(
                7777, //port
                () -> new StompMessagingProtocolClass(), //protocol factory
                () -> new MsgEncDec() //message encoder decoder factory
        ).serve();
    }
}//login 127.0.0.1:7777 manor 9399