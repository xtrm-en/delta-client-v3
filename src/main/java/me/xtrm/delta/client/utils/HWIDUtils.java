package me.xtrm.delta.client.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HWIDUtils {
	
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String generateHWID() {
        try {
            MessageDigest hash = MessageDigest.getInstance("MD5");

            String s = System.getProperty("os.name") +
                    System.getProperty("os.arch") +
                    System.getProperty("os.version") +
                    Runtime.getRuntime().availableProcessors() +
                    System.getenv("PROCESSOR_IDENTIFIER") +
                    System.getenv("PROCESSOR_ARCHITECTURE") +
                    System.getenv("PROCESSOR_ARCHITEW6432") +
                    System.getenv("NUMBER_OF_PROCESSORS");
            return bytesToHex(hash.digest(s.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new Error("Algorithm wasn't found.", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
