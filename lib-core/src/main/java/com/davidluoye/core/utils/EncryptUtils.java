/*
 * Copyright 2021 The authors David Yang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.davidluoye.core.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptUtils {

    public static String encrypt(String value) {
        String result;
        try {
            final MessageDigest message = MessageDigest.getInstance("MD5");
            message.update(value.getBytes("UTF-8"), 0, value.length());
            result = bytesToHexString(message.digest());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            result = String.valueOf(value.hashCode());
        }
        return result;
    }

    public static String encode(String value) {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] bytes = encoder.encode(value.getBytes());
        return new String(bytes);
    }

    public static String decode(String value) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(value);
        return new String(bytes);
    }

    private static String bytesToHexString(byte[] inputBytes) {
        StringBuilder outputHexString = new StringBuilder();
        for (int i = 0; i < inputBytes.length; i++) {
            String hex = Integer.toHexString(inputBytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            outputHexString.append(hex.toUpperCase());
        }
        return outputHexString.toString();
    }
}
