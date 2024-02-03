package com.back.util.jsonWebToken;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.back.util.encryptorAndCodec.EncryptorAndCodecUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class JsonWebTokenUtil {
    /**
     * jwt: 头部+负载+签名
     * 头部：jwt类型+签名算法
     * 负载：签发机构+用户id+签发时间+生效时间+到期时间
     * 签名：加密{头部+负载}
     **/
    public JsonWebTokenUtil() {
    }

    @Value("${jwt.iss}")
    private String iss;//签发机构
    @Value("${jwt.key}")
    private String key;//密钥
    @Value("${jwt.a-ttl}")
    private long accessTTL;//生存时间

    @Value("${jwt.r-ttl}")
    private long refreshTTL;//生存时间
    @Value("${jwt.delay}")
    private long delay;//签发后延迟生效时间

    public String createRefreshJWTByUId(long uid) {
        return createJWTByUId(uid, JsonWebTokenTypeEnum.REFRESH.type);
    }

    public String createRefreshJWTByUId(long uid,long ttl,TimeUnit timeUnit) {
        System.out.println("timestamp: "+timeUnit.toMillis(ttl));
        return createJWTByUId(uid, JsonWebTokenTypeEnum.REFRESH.type,timeUnit.toMillis(ttl));
    }

    public String createAccessJWTByUId(long uid,long ttl,TimeUnit timeUnit) {
        System.out.println("timestamp: "+timeUnit.toMillis(ttl));
        return createJWTByUId(uid, JsonWebTokenTypeEnum.ACCESS.type,timeUnit.toMillis(ttl));
    }


    public String createAccessJWTByUId(long uid) {
        return createJWTByUId(uid, JsonWebTokenTypeEnum.ACCESS.type);
    }

    private String createJWTByUId(long uid, String jwtType) {
        Instant iat = Instant.now();
        Instant nbf = Instant.ofEpochMilli(iat.toEpochMilli()+delay);
        Instant exp = Instant.now();
        if (jwtType.equals(JsonWebTokenTypeEnum.ACCESS.type)){
            exp = Instant.ofEpochMilli(nbf.toEpochMilli()+accessTTL);
        }else {
            exp = Instant.ofEpochMilli(nbf.toEpochMilli()+refreshTTL);
        }

        Algorithm alg = Algorithm.HMAC256(EncryptorAndCodecUtil.padString(key, 64));

        Map<String, Object> header = new HashMap<>();
        header.put("typ", jwtType);//
        header.put("alg", "HS256");

        Map<String, Object> payload = new HashMap<>();
        payload.put("iss", iss);//签发机构（auth0和网站联合签发）
        payload.put("sub", uid);//令牌主体(用户id)
        payload.put("iat", iat);//签发时间 (iat)
        payload.put("nbf", nbf);//生效时间（iat+delay = nbf）
        payload.put("exp", exp);//到期时间（bnf+ttl = exp）


        return EncryptorAndCodecUtil.encode(
                JWT
                        .create()
                        .withHeader(header)
                        .withPayload(payload)
                        .sign(alg)
                , "SAFE");
    }

    private String createJWTByUId(long uid, String jwtType,long ttl) {
        Instant iat = Instant.now();
        Instant nbf = Instant.ofEpochMilli(iat.toEpochMilli()+delay);
        Instant exp = Instant.ofEpochMilli(nbf.toEpochMilli()+ttl);

        Algorithm alg = Algorithm.HMAC256(EncryptorAndCodecUtil.padString(key, 64));

        Map<String, Object> header = new HashMap<>();
        header.put("typ", jwtType);//
        header.put("alg", "HS256");

        Map<String, Object> payload = new HashMap<>();
        payload.put("iss", iss);//签发机构（auth0和网站联合签发）
        payload.put("sub", uid);//令牌主体(用户id)
        payload.put("iat", iat);//签发时间 (iat)
        payload.put("nbf", nbf);//生效时间（iat+delay = nbf）
        payload.put("exp", exp);//到期时间（bnf+ttl = exp）


        return EncryptorAndCodecUtil.encode(
                JWT
                        .create()
                        .withHeader(header)
                        .withPayload(payload)
                        .sign(alg)
                , "SAFE");
    }

    //核验令牌
    public  boolean verifyAccessJWT(String accessJWT){
        return verifyJWT(accessJWT) && getTypeFromJWT(accessJWT).equals(JsonWebTokenTypeEnum.ACCESS.type);
    }

    public  boolean verifyRefreshJWT(String accessJWT){
        return verifyJWT(accessJWT) && getTypeFromJWT(accessJWT).equals(JsonWebTokenTypeEnum.REFRESH.type);
    }
    public boolean verifyJWT(String jwt) {
        Algorithm alg = Algorithm.HMAC256(EncryptorAndCodecUtil.padString(key, 64));
        JWTVerifier jwtVerifier = JWT.require(alg).build();
        try {
            jwtVerifier.verify(EncryptorAndCodecUtil.decode(jwt, "SAFE"));
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }


    public String getTypeFromJWT(String jwt) {
        Algorithm alg = Algorithm.HMAC256(EncryptorAndCodecUtil.padString(key, 64));
        JWTVerifier jwtVerifier = JWT.require(alg).build();
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(EncryptorAndCodecUtil.decode(jwt, "SAFE"));
            return decodedJWT.getHeaderClaim("typ").asString();
        } catch (JWTVerificationException e) {
            return null; // 或者根据实际需求返回合适的值
        }
    }

    //从令牌中得到用户id
    public long getUIdFromJWT(String jwt) {
        Algorithm alg = Algorithm.HMAC256(EncryptorAndCodecUtil.padString(key, 64));
        JWTVerifier jwtVerifier = JWT.require(alg).build();
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(EncryptorAndCodecUtil.decode(jwt, "SAFE"));
            return Long.parseLong(decodedJWT.getSubject());
        } catch (JWTVerificationException e) {
            return -1L;
        }
    }

}
