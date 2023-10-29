package me.xtrm.delta.client.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionHelper {
	
	public static String getPName() {
		return new String("dKlKxcvu".replace('d', 'p').replace('K', 'a').replace('x', 'd').replace('u', 'm').replace('c', 'i').replace('v', 'u')).toLowerCase();
	}

	public static String getPMGRName() {
		return new String("axbxcxdxefg".replace("a", "p").replace("x", "a").replace("b", "l").replace("c", "m").replace("d", "n").replace("g", "r").replace("e", "g").replace("f", "e")).toLowerCase();
	}
	
	public static String MD5(String md5) {
		try {
		    MessageDigest md = MessageDigest.getInstance("MD5");
		    byte[] array = md.digest(md5.getBytes());
		    StringBuffer sb = new StringBuffer();
		    for (int i = 0; i < array.length; ++i) {
		    	sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
		    }
		    return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) { }
		return "";
	}
	
	public static String getMD5(File file) {
        if (file.isDirectory()) {
            return "0";
        }
        if (!file.exists()) {
            return "0";
        }
        StringBuffer stringBuffer = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] arrby = new byte[1024];
            int n = 0;
            while ((n = fileInputStream.read(arrby)) != -1) {
                messageDigest.update(arrby, 0, n);
            }
            byte[] arrby2 = messageDigest.digest();
            stringBuffer = new StringBuffer();
            int n2 = 0;
            do {
                if (n2 >= arrby2.length) {
                    if (fileInputStream == null) return stringBuffer.toString();
                    fileInputStream.close();
                    return stringBuffer.toString();
                }
                stringBuffer.append(Integer.toString((arrby2[n2] & 255) + 256, 16).substring(1));
                ++n2;
            } while (true);
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return null;
        } catch (FileNotFoundException fileNotFoundException) {
            return stringBuffer.toString();
        } catch (IOException iOException) { }
        return stringBuffer.toString();
    }

}