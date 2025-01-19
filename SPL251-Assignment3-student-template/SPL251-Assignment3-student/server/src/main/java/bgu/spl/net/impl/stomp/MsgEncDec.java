package bgu.spl.net.impl.stomp;

import java.util.Arrays;
import java.nio.charset.StandardCharsets;

public class MsgEncDec implements MessageEncoderDecoder<String[]> {
    
    // fields
    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;
    private String[] frmStr = new String[1 << 10]; //start with 1k
    private int frmStrLen = 0;

    // methods
    @Override
    public String[] decodeNextByte(byte nextByte) {
        if (nextByte == '\u0000') {
            return frmStr;
        }
        else if (nextByte == ':'){
            pushString(popString());
        }
        else if (nextByte == '\n') {
            pushString(popString());
        }
        // every cell except the first one starts with /n
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

    private void pushString(String nextString) {
        if (frmStrLen >= frmStr.length) {
            frmStr = Arrays.copyOf(frmStr, frmStrLen * 2);
        }

        frmStr[frmStrLen++] = nextString;
    }

    @Override
    public byte[] encode(String[] message) {
        byte[] retEncoded = new byte[1 << 10]; //start with 1k
        int retEncodedLen = 0;
        for (String str : message) {
            byte[] encoded = (str).getBytes();
            for (byte b : encoded) {
                if (retEncodedLen >= retEncoded.length) {
                    retEncoded = Arrays.copyOf(retEncoded, retEncodedLen * 2);
                }
                retEncoded[retEncodedLen++] = b;
            }
        }
        return retEncoded;        
    }
}
