package bgu.spl.net.impl.stomp;

public interface StompFrame {

    String[] handle ();

    String[] errorHandle(String message);
    
    boolean shouldTerminate();
}
