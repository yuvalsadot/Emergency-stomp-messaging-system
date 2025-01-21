package bgu.spl.net.srv;

import java.io.IOException;

public interface Connections<T> {

    boolean send(int handlerId, T msg);

    void send(String channel, T msg);

    void disconnect(int handlerId);
}
