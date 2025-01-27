package bgu.spl.net.impl.stomp;

public interface StompFrameAction<T> {

    T handle ();

    T errorHandle(String message);
    
    boolean shouldTerminate();
}
