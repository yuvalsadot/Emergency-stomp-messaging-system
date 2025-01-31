package bgu.spl.net.srv;

public interface Connections<T> {

    boolean send(int handlerId, T msg);

    void send(String channel, T msg);

    void disconnect(int handlerId);
}