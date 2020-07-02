package com.davidluoye.support.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecureUtil {

    public static String transferToSHA1(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            byte[] sha1Hash = md.digest();
            return convertToHex(sha1Hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String transferToMD5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes("UTF-8"), 0, text.length());
            byte[] hash = md.digest();
            return convertToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            int halfByte = (data[i] >>> 4) & 0x0F;
            int two_half = 0;
            do {
                if ((0 <= halfByte) && (halfByte <= 9)) {
                    buf.append((char) ('0' + halfByte));
                } else {
                    buf.append((char) ('a' + (halfByte - 10)));
                }
                halfByte = data[i] & 0x0F;
            } while (two_half++ < 1);
        }
        return buf.toString();
    }
}
