package bgu.spl.net.srv;

import bgu.spl.net.impl.stomp.MsgEncDec;
import bgu.spl.net.impl.stomp.SingletonDataBase;
import bgu.spl.net.impl.stomp.StompMessagingProtocolClass;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Supplier;

public abstract class BaseServer<T> implements Server<T> {

    private final int port;
    private final Supplier<StompMessagingProtocolClass> protocolFactory;
    private final Supplier<MsgEncDec> encdecFactory;
    private ServerSocket sock;
    private ConnectionsClass connections;
    private int handlerId;
    
    public BaseServer(
            int port,
            Supplier<StompMessagingProtocolClass> protocolFactory,
            Supplier<MsgEncDec> encdecFactory) {

        this.port = port;
        this.protocolFactory = protocolFactory;
        this.encdecFactory = encdecFactory;
		this.sock = null;
        this.connections = new ConnectionsClass();
        this.handlerId = 0;
    }

    @Override
    public void serve() {

        try (ServerSocket serverSock = new ServerSocket(port)) {
			System.out.println("Server started");

            this.sock = serverSock; //just to be able to close

            while (!Thread.currentThread().isInterrupted()) {

                Socket clientSock = serverSock.accept();
                System.err.println("Accepted connection from: " + clientSock.getRemoteSocketAddress());

                BlockingConnectionHandler<T> handler = new BlockingConnectionHandler<>(
                        clientSock,
                        encdecFactory.get(),
                        protocolFactory.get(),
                        handlerId,
                        connections);
                SingletonDataBase.addHandler(handlerId, handler);
                handlerId++;
                execute(handler);
            }
        } catch (IOException ex) {
        }

        System.out.println("server closed!!!");
    }

    @Override
    public void close() throws IOException {
		if (sock != null)
			sock.close();
    }

    protected abstract void execute(BlockingConnectionHandler<T>  handler);
}
