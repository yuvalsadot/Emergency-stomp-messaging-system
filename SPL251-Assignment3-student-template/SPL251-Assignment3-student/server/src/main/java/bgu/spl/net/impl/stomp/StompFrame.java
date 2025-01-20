package bgu.spl.net.impl.stomp;

public interface StompFrame {

    String[] handle ();

    String[] getFrame();

    void setFrameId(int id);

    String[] errorHandle(String message);

}
