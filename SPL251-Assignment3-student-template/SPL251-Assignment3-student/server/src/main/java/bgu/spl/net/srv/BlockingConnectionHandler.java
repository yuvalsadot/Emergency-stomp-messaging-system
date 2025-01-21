package bgu.spl.net.srv;

import bgu.spl.net.impl.stomp.MsgEncDec;
import bgu.spl.net.impl.stomp.StompMessagingProtocolClass;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BlockingConnectionHandler<T> implements Runnable, ConnectionHandler<String[]> {

    // fields
    private final StompMessagingProtocolClass protocol;
    private final MsgEncDec encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;
    private int handlerId;
    private ConnectionsClass connections;

    // constructor
    public BlockingConnectionHandler(Socket sock, MsgEncDec reader, StompMessagingProtocolClass protocol, int handlerId, ConnectionsClass connections) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
        this.handlerId = handlerId;
        this.connections = connections;
        this.protocol.start(handlerId, connections);
    }

    // methods
    @Override
    public void run() {
        try (Socket sock = this.sock) { //just for automatic closing
            int read;

            in = new BufferedInputStream(sock.getInputStream());
            out = new BufferedOutputStream(sock.getOutputStream());

            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {
                String[] nextMessage = encdec.decodeNextByte((byte) read);
                if (nextMessage != null) {
                    protocol.process(nextMessage);
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void close() throws IOException {
        connections.disconnect(handlerId);
        connected = false;
        sock.close();
    }

    @Override
    public void send(String[] msg) {
        try {
            out.write(encdec.encode(msg));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
