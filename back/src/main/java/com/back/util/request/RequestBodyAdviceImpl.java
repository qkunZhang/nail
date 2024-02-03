package com.back.util.request;


import com.back.entity.relationalEntity.PasswordResettingUserEntity;
import com.back.entity.relationalEntity.SignInUserEntity;
import com.back.entity.relationalEntity.UserEntity;
import com.back.util.encryptorAndCodec.EncryptorAndCodecUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;

@RestControllerAdvice
public class RequestBodyAdviceImpl implements RequestBodyAdvice {




    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (body instanceof UserEntity){
            String encryptedInvitationCode =((UserEntity) body).getInvitationCode();//邀请码是前后两端各加上了一个随机字符，用“INVITATION_CODE”异或后的base64url编码字符串
            String encryptedUsername =((UserEntity) body).getUsername();//用户名是前后两端各加上了一个随机字符，用“USERNAME”异或后的base64url编码字符串
            String encryptedPassword =((UserEntity) body).getPassword();//密码是明文SHA3-256加密后，前后两端各加上了一个随机字符，用“PASSWORD”异或后的base64url编码字符串
            String encryptedEmail =((UserEntity) body).getEmail();//邮箱是前后两端各加上了一个随机字符，用“EMAIL”异或后的base64url编码字符串
            String encryptedVerificationCode = ((UserEntity) body).getVerificationCode();//验证码是前后两端各加上了一个随机字符，用“VERIFICATION_CODE”异或后的base64url编码字符串

            String decryptedInvitationCode = EncryptorAndCodecUtil.decryptXOR(encryptedInvitationCode,"INVITATION_CODE");
            String decryptedEmail = EncryptorAndCodecUtil.decryptXOR(encryptedEmail,"EMAIL");
            String decryptedVerificationCode = EncryptorAndCodecUtil.decryptXOR(encryptedVerificationCode,"VERIFICATION_CODE");
            String decryptedUsername = EncryptorAndCodecUtil.decryptXOR(encryptedUsername,"USERNAME");
            String decryptedPassword = EncryptorAndCodecUtil.decryptXOR(encryptedPassword,"PASSWORD");

            ((UserEntity) body).setInvitationCode(decryptedInvitationCode.substring(1,decryptedInvitationCode.length()-1));
            ((UserEntity) body).setEmail(decryptedEmail.substring(1,decryptedEmail.length()-1));
            ((UserEntity) body).setVerificationCode(decryptedVerificationCode.substring(1,decryptedVerificationCode.length()-1));
            ((UserEntity) body).setUsername(decryptedUsername.substring(1,decryptedUsername.length()-1));
            ((UserEntity) body).setPassword(decryptedPassword.substring(1,decryptedPassword.length()-1));
        }


        if (body instanceof SignInUserEntity){
            String encryptedUsername =((SignInUserEntity) body).getUsername();//用户名是前后两端各加上了一个随机字符，用“USERNAME”异或后的base64url编码字符串
            String encryptedPassword =((SignInUserEntity) body).getPassword();//密码是明文SHA3-256加密后，前后两端各加上了一个随机字符，用“PASSWORD”异或后的base64url编码字符串

            String decryptedUsername = EncryptorAndCodecUtil.decryptXOR(encryptedUsername,"USERNAME");
            String decryptedPassword = EncryptorAndCodecUtil.decryptXOR(encryptedPassword,"PASSWORD");

            ((SignInUserEntity) body).setUsername(decryptedUsername.substring(1,decryptedUsername.length()-1));
            ((SignInUserEntity) body).setPassword(decryptedPassword.substring(1,decryptedPassword.length()-1));
        }


        if (body instanceof PasswordResettingUserEntity){
            String encryptedUsername =((PasswordResettingUserEntity) body).getUsername();//用户名是前后两端各加上了一个随机字符，用“USERNAME”异或后的base64url编码字符串
            String encryptedPassword =((PasswordResettingUserEntity) body).getPassword();//密码是明文SHA3-256加密后，前后两端各加上了一个随机字符，用“PASSWORD”异或后的base64url编码字符串
            String encryptedEmail =((PasswordResettingUserEntity) body).getEmail();//邮箱是前后两端各加上了一个随机字符，用“EMAIL”异或后的base64url编码字符串
            String encryptedVerificationCode = ((PasswordResettingUserEntity) body).getVerificationCode();//验证码是前后两端各加上了一个随机字符，用“VERIFICATION_CODE”异或后的base64url编码字符串

            String decryptedEmail = EncryptorAndCodecUtil.decryptXOR(encryptedEmail,"EMAIL");
            String decryptedVerificationCode = EncryptorAndCodecUtil.decryptXOR(encryptedVerificationCode,"VERIFICATION_CODE");
            String decryptedUsername = EncryptorAndCodecUtil.decryptXOR(encryptedUsername,"USERNAME");
            String decryptedPassword = EncryptorAndCodecUtil.decryptXOR(encryptedPassword,"PASSWORD");

            ((PasswordResettingUserEntity) body).setEmail(decryptedEmail.substring(1,decryptedEmail.length()-1));
            ((PasswordResettingUserEntity) body).setVerificationCode(decryptedVerificationCode.substring(1,decryptedVerificationCode.length()-1));
            ((PasswordResettingUserEntity) body).setUsername(decryptedUsername.substring(1,decryptedUsername.length()-1));
            ((PasswordResettingUserEntity) body).setPassword(decryptedPassword.substring(1,decryptedPassword.length()-1));
        }

        System.out.println("request_body: "+body);
        return body;
    }





    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        System.out.println("空的请求体");
        return body;
    }
}
