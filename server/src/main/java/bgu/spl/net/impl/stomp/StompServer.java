package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.Server;

public class StompServer {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: StompServer <port> <server type>");
            return;
        }
        
        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("invalid port number");
            return;
        }

        String serverType = args[1].toLowerCase();

        System.out.println("String serverType: " + serverType + " on port: " + port);

        switch (serverType) {
            case "tpc":
                runTPC(port);
                break;
            case "reactor":
                runReactor(port);
                break;
            default:
                System.out.println("invalid server type, please choose 'tpc' or 'reactor'");
        }
    }

    private static void runTPC(int port) {
        Server.threadPerClient(
                port, //port
                () -> new StompMessagingProtocolClass(), //protocol factory
                () -> new MsgEncDec() //message encoder decoder factory
        ).serve();
    }

    private static void runReactor(int port) {
        Server.reactor(
                Runtime.getRuntime().availableProcessors(),
                port, //port
                () -> new StompMessagingProtocolClass(), //protocol factory
                () -> new MsgEncDec() //message encoder decoder factory
        ).serve();
    }
}// login 127.0.0.1:7777 manor 9399
// /workspaces/Assignment_3/client/data/events1.json