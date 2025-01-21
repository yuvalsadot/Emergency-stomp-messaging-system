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
}
