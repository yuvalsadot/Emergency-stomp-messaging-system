package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.nio.charset.StandardCharsets;

public class MsgEncDec implements MessageEncoderDecoder<StompFrameRaw> {
    
    // fields
    private byte[] bytes = new byte[1 << 5];
    private int len = 0;
    private String command;
    private ConcurrentHashMap<String, String> headers;
    private String body;
    private boolean isCommand = true;
    private boolean isHeader = false;

    // methods
    @Override
    public StompFrameRaw decodeNextByte(byte nextByte) {
        if (nextByte == '\u0000') {
            body = popString().substring(2); // discard the first two characters '\n'
            if (headers.get("destination") != null && headers.get("destination").charAt(0) == '/'){ // delete the char '/' in the begining of the destination topic
                headers.put("destination", headers.get("destination").substring(1));
            }
            StompFrameRaw message = new StompFrameRaw(command, headers, body);
            // reset fields
            command = "";
            headers = null;
            body = "";
            isCommand = true;
            isHeader = false;
            return message;
        }
        else if (isCommand && nextByte == '\n') {
            command = popString();
            isCommand = false;
            isHeader = true;
        }
        else if (len == 1 && nextByte == '\n') { // empty line == end of headers
            isHeader = false;
        }
        else if (isHeader && nextByte == '\n') {
            putHeader(popString().substring(1));
        }
        pushByte(nextByte);
        return null; //not a frame yet
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private String popString() {
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }

    private void putHeader(String nextString) {
        if (headers == null) {
            headers = new ConcurrentHashMap<>();
        }
        String[] header = nextString.split(":");
        headers.put(header[0], header[1]);
    }

    @Override
    public byte[] encode(StompFrameRaw frame) {
        byte[] retEncoded = new byte[1 << 5];
        int retEncodedLen = 0;
        ArrayList<byte[]> toEncode = new ArrayList<>();
        // encode command
        byte[] encoded = (frame.getCommand() + "\n").getBytes();
        toEncode.add(encoded);
        // encode headers
        ConcurrentHashMap<String,String> currHeadres = frame.getHeaders();
        if (currHeadres.get("destination") != null && currHeadres.get("destination").charAt(0) != '/'){ // add the char '/' in the begining of the destination topic, only once
            currHeadres.put("destination", '/' + currHeadres.get("destination"));
        }
        for (String key : currHeadres.keySet()) {
            encoded = (key + ":" + currHeadres.get(key) + "\n").getBytes();
            toEncode.add(encoded);
        }
        // encode body
        encoded = ("\n" + frame.getBody() + "\n\u0000").getBytes();
        toEncode.add(encoded);
        // concatenate all encoded parts
        for (byte[] arr : toEncode) {
            for (byte b : arr) {
                if (retEncodedLen >= retEncoded.length) {
                    retEncoded = Arrays.copyOf(retEncoded, retEncodedLen * 2);
                }
                retEncoded[retEncodedLen++] = b;
            }
        }
        return Arrays.copyOf(retEncoded, retEncodedLen);
    }
}
