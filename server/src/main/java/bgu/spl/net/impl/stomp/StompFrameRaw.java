package bgu.spl.net.impl.stomp;

import java.util.concurrent.ConcurrentHashMap;

public class StompFrameRaw {
    // fields
    private String command;
    private ConcurrentHashMap<String, String> headers;
    private String body;

    // constructor
    public StompFrameRaw(String command, ConcurrentHashMap<String, String> headers, String body) {
        this.command = command;
        this.headers = headers;
        this.body = body;
    }

    // methods
    //getters
    public String getCommand() {
        return command;
    }

    public ConcurrentHashMap<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    //setters
    public void setBody(String body) {
        this.body = body;
    }
}
