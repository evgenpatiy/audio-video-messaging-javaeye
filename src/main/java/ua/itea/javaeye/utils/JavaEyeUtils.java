package ua.itea.javaeye.utils;

import java.awt.Dimension;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaEyeUtils {
    //public InetAddress localAddress = null;
    public static final Dimension dimension = new Dimension(320, 240);
    public static final int streamServerPort = 20000;
    public static final String DBNAME = "javaeye.db";
    public static final int SOUND_PORT = 50005;
    public static final int SAMPLE_RATE = 22050;
    public static final int CHANNELS = 2;
    public static final int SAMPLE_SIZE = 8;

    public boolean validIP(String s) {
        String validIP = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        Pattern p = Pattern.compile(validIP);
        Matcher m = p.matcher(s);
        return m.matches();
    }

    public String getStringFromMAC(byte[] mac) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
        }
        return sb.toString();
    }
}
