package com.back.util.encryptorAndCodec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class EncryptorAndCodecUtil {
    //16 字节（128 位）、24 字节（192 位）或 32 字节（256 位）
    final private static String keyAES = padString("/thisIsKeyOfAes/", 16);
    //48 字节（192位）以上
    final private static String factorECC = padString("/thisIsFactorToGenerateKeyOfEcc/", 64);
    final private static String keyXOR = "/thisIsKeyOfXor/";

    //    向外暴露的方法
    public static String encrypt(String text, String algorithm) throws Exception {
        return switch (algorithm.toUpperCase()) {
            case "AES" ->//模式：ECB；填充：PKCS7；密钥：128bit，16进制；密文：16进制
                    encryptAES(text);
            case "SHA3" ->//算法：SHA3-256；密文：16进制
                    encryptSHA3_256(text);
            case "ARGON2" -> encryptArgon2(text);
            case "ECC" -> encryptECC(text);
            case "XOR" -> encryptXOR(text);
            default -> throw new IllegalArgumentException("Unknown algorithm");
        };
    }

    public static String decrypt(String text, String algorithm) throws Exception {
        return switch (algorithm.toUpperCase()) {
            case "AES" -> decryptAES(text);
            case "ECC" -> decryptECC(text);
            case "XOR" -> decryptXOR(text);
            default -> throw new IllegalArgumentException("Unknown algorithm");
        };
    }

    public static String encode(String text, String method) {
        return switch (method.toUpperCase()) {
            case "SAFE" -> encodeBASE64URLSafe(text);
            case "STANDARD" -> encodeBASE64Standard(text);
            case "HEX" -> encodeHEX(text);
            case "URL" -> encodeURL(text);
            default -> throw new IllegalArgumentException("Unknown method");
        };
    }

    public static String decode(String text, String method) {
        return switch (method.toUpperCase()) {
            case "SAFE" -> decodeToStringBASE64URLSafe(text);
            case "HEX" -> decodeToStringHEX(text);
            case "STANDARD" -> decodeToStringBASE64Standard(text);
            case "URL" -> decodeToStringURL(text);
            default -> throw new IllegalArgumentException("Unknown method");
        };
    }

    public static Boolean match(String tobeMatchedText, String encryptedText, String algorithm) throws Exception {
        return switch (algorithm.toUpperCase()) {
            case "ARGON2" -> matchArgon2(tobeMatchedText, encryptedText);
            case "SHA3" -> matchSHA3_256(tobeMatchedText, encryptedText);
            default -> throw new IllegalArgumentException("Unknown algorithm");
        };
    }


    //    工具方法
    private static String bytesToHex(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("Input byte array cannot be null");
        }
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static byte[] hexToBytes(String hex) {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException("Input hex string must have an even number of characters");
        }
        hex = hex.toUpperCase(); // Convert input to uppercase to handle case-insensitive hex strings
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    //接收一个密钥生成因子，生成固定密钥对
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("EC");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(EncryptorAndCodecUtil.factorECC.getBytes(StandardCharsets.UTF_8));
        keyPairGen.initialize(256, random);
        return keyPairGen.generateKeyPair();
    }

    public static KeyPair getKeyPair(String factorECC, Integer length) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("EC");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(padString(factorECC, length).getBytes(StandardCharsets.UTF_8));
        keyPairGen.initialize(256, random);
        return keyPairGen.generateKeyPair();
    }

    public static String padString(String originalText, int targetLength) {
        StringBuilder paddedString = new StringBuilder(originalText);
        while (paddedString.length() < targetLength) {
            paddedString.append(originalText);
        }
        return paddedString.substring(0, targetLength);
    }


    //    加密与解密
    //接受任意字符串，返回base64编码过的加密字符串
    private static String encryptAES(String unencryptedText) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        SecretKeySpec secretKey = new SecretKeySpec(EncryptorAndCodecUtil.keyAES.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // 指定填充模式为PKCS5Padding
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return encodeBASE64URLSafe(cipher.doFinal(unencryptedText.getBytes(StandardCharsets.UTF_8)));
    }

    //接收base64编码过的加密字符串，返回明文
    private static String decryptAES(String encryptedText) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        SecretKeySpec secretKey = new SecretKeySpec(EncryptorAndCodecUtil.keyAES.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // 指定填充模式为PKCS5Padding
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(decodeToBytesBASE64URLSafe(encryptedText)), StandardCharsets.UTF_8);
    }

    //接受任意字符串，返回base64编码过的加密字符串
    private static String encryptSHA3_256(String unEncryptedText) throws NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());
        MessageDigest messageDigest = MessageDigest.getInstance("SHA3-256");
        final byte[] hashedBytes = messageDigest.digest(unEncryptedText.getBytes(StandardCharsets.UTF_8));
        return encodeBASE64URLSafe(hashedBytes);
    }


    //接受任意字符串，返回base64编码过的加密字符串
    private static String encryptArgon2(String unEncryptedText) {
        return encodeBASE64URLSafe(Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8().encode(unEncryptedText));
    }

    //接受任意字符串，返回base64编码过的加密字符串
    private static String encryptECC(String unEncryptedText) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(getKeyPair().getPublic().getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        Cipher cipher = Cipher.getInstance("ECIES", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generatePublic(x509KeySpec));
        return encodeBASE64URLSafe(cipher.doFinal(unEncryptedText.getBytes()));
    }

    //接收base64编码过的加密字符串，返回明文
    private static String decryptECC(String encryptedText) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(getKeyPair().getPrivate().getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("ECIES", "BC");
        cipher.init(Cipher.DECRYPT_MODE, keyFactory.generatePrivate(pkcs8KeySpec));
        return new String(cipher.doFinal(decodeToBytesBASE64URLSafe(encryptedText)), StandardCharsets.UTF_8);
    }

    //接受任意字符串，返回base64编码过的加密字符串
    private static String encryptXOR(String unEncryptedText) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < unEncryptedText.length(); i++) {
            result.append((char) (unEncryptedText.charAt(i) ^ keyXOR.charAt(i % keyXOR.length())));
        }
        return encodeBASE64URLSafe(String.valueOf(result));
    }

    private static String encryptXOR(String unEncryptedText, String keyXOR) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < unEncryptedText.length(); i++) {
            result.append((char) (unEncryptedText.charAt(i) ^ keyXOR.charAt(i % keyXOR.length())));
        }
        return encodeBASE64URLSafe(String.valueOf(result));
    }

    //接收base64编码过的加密字符串，返回明文
    private static String decryptXOR(String encryptedText) {
        return decodeToStringBASE64URLSafe(encryptXOR(decodeToStringBASE64URLSafe(encryptedText)));
    }


    public static String decryptXOR(String encryptedText, String keyXOR) {
        return decodeToStringBASE64URLSafe(encryptXOR(decodeToStringBASE64URLSafe(encryptedText), keyXOR));
    }


    //    hash匹配
    private static Boolean matchSHA3_256(String toBeMatchedText, String encryptedText) throws Exception {
        return encryptSHA3_256(toBeMatchedText).equals(encryptedText);
    }

    public static Boolean matchArgon2(String toBeMatchedText, String encryptedText) {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8().matches(toBeMatchedText, decodeToStringBASE64Standard(encryptedText));
    }

    //    编码与解码
    //接收任意字符串，返回base64url安全编码过的字符串
    private static String encodeBASE64URLSafe(String unEncodedText) {
        return new String(Base64.getUrlEncoder().withoutPadding().encode(unEncodedText.getBytes()), StandardCharsets.UTF_8);
    }

    //接收任意字节数组，返回base64url安全编码过的字符串
    private static String encodeBASE64URLSafe(byte[] unEncodedBytes) {
        return new String(Base64.getUrlEncoder().withoutPadding().encode(unEncodedBytes), StandardCharsets.UTF_8);
    }

    //接收base64url安全编码过的字符串，返回解码过的字节数组
    private static byte[] decodeToBytesBASE64URLSafe(String encodedText) {
        return Base64.getUrlDecoder().decode(encodedText);
    }

    //接收base64url安全编码过的字符串，返回解码过的字符串
    private static String decodeToStringBASE64URLSafe(String encodedText) {
        return new String(Base64.getUrlDecoder().decode(encodedText), StandardCharsets.UTF_8);
    }

    //接收任意字符串，返回标准base64编码过的字符串
    private static String encodeBASE64Standard(String unEncodedText) {
        return new String(Base64.getEncoder().encode(unEncodedText.getBytes()), StandardCharsets.UTF_8);
    }

    //接收任意字节数组，返回标准base64编码过的字符串
    public static String encodeBASE64Standard(byte[] unEncodedBytes) {
        return new String(Base64.getEncoder().encode(unEncodedBytes), StandardCharsets.UTF_8);
    }

    //接收任意标准base64编码过的字符串，返回解码过的字节数组
    public static byte[] decodeToBytesBASE64Standard(String encodedText) {
        return Base64.getDecoder().decode(encodedText);
    }

    //接收任意标准base64编码过的字符串，返回解码过的字符串
    private static String decodeToStringBASE64Standard(String encodedBytes) {
        return new String(Base64.getDecoder().decode(encodedBytes), StandardCharsets.UTF_8);
    }

    //接收任意字符串，返回16进制形式
    private static String encodeHEX(String unEncodedText) {
        return bytesToHex(unEncodedText.getBytes());
    }

    //接收任意字节数组，返回16进制形式
    private static String encodeHEX(byte[] unEncodedBytes) {
        return bytesToHex(unEncodedBytes);
    }

    //接收16进制字符串，返回UTF-8字符串
    private static String decodeToStringHEX(String encodeText) {
        return new String(hexToBytes(encodeText), StandardCharsets.UTF_8);
    }

    private static byte[] decodeToBytesHEX(String encodeText) {
        return hexToBytes(encodeText);
    }

    private static String encodeURL(String unEncodedText) {
        return URLEncoder.encode(unEncodedText, StandardCharsets.UTF_8);
    }

    private static String decodeToStringURL(String encodedString) {
        return URLDecoder.decode(encodedString, StandardCharsets.UTF_8);
    }

}