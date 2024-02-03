package com.back.util.jsonWebToken;

import lombok.Data;

@Data
public class JsonWebTokenEntity {
    private String refreshJWT;
    private String accessJWT;
}
