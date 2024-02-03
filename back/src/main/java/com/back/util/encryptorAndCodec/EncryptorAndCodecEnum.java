package com.back.util.encryptorAndCodec;

public enum EncryptorAndCodecEnum {
    SHA3("SHA3"),
    ARGON2("ARGON2"),
    AES("AES"),
    ECC("ECC"),
    XOR("XOR"),
    STANDARD("STANDARD"),
    SAFE("SAFE"),
    URL("URL"),
    HEX("HEX");


    public final String alg;

    EncryptorAndCodecEnum(String alg){
        this.alg =alg;
    }
}
