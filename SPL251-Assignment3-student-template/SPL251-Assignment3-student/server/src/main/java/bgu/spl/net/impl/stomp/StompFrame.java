package bgu.spl.net.api;

public interface StompFrame {

    String[] handle ();

    String[] getFrame();

    void setFrameId(int id);

    String[] errorHandle();

}
