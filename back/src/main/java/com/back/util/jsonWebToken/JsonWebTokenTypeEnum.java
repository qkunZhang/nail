package com.back.util.jsonWebToken;

public enum JsonWebTokenTypeEnum {
    REFRESH("refresh"),
    ACCESS("access");
    public final String type;

    JsonWebTokenTypeEnum(String type){
        this.type = type;
    }
}
