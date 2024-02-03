package com.back.controller;

import com.back.util.jsonWebToken.JsonWebTokenUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping(value = "/auth",produces = "application/json; charset=UTF-8")
public class AuthController {
    private final JsonWebTokenUtil jsonWebTokenUtil;

    public AuthController(JsonWebTokenUtil jsonWebTokenUtil) {
        this.jsonWebTokenUtil = jsonWebTokenUtil;
    }

    @PostMapping("/access-jwt")
    public boolean verifyAccessJWT(@RequestBody String accessJWT){
        return jsonWebTokenUtil.verifyAccessJWT(accessJWT);
    }

    @PostMapping("/refresh-jwt")
    public String verifyRefreshJWT(@RequestBody String refreshJWT) throws AccessDeniedException {
        if (jsonWebTokenUtil.verifyRefreshJWT(refreshJWT)){
            return jsonWebTokenUtil.createAccessJWTByUId(jsonWebTokenUtil.getUIdFromJWT(refreshJWT));
        }else {
            throw new AccessDeniedException("refreshJWT无效，请登录");
        }
    }
}
