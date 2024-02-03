package com.back.util.captcha;

import com.back.util.redis.RedisUtil;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Component
public class CaptchaUtil {
    private final RedisUtil redisUtil;

    public CaptchaUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    public String getText(){
        return CaptchaConfig.kaptchaProducer().createText();
    }

    public  String getBase64ofPngImg(String captchaText) throws IOException {
        return getBase64ofCaptchaImg(captchaText,"png");
    }

    public  String getBase64ofJpgImg(String captchaText) throws IOException {
        return getBase64ofCaptchaImg(captchaText,"jpg");
    }

    public  String getBase64ofClearImg(String captchaText) throws IOException {
        return getBase64ofCaptchaImg(captchaText,"png");
    }

    public  String getBase64ofUnClearImg(String captchaText) throws IOException {
        return getBase64ofCaptchaImg(captchaText,"jpg");
    }

    private  String getBase64ofCaptchaImg(String captchaText,String imgType) throws IOException {
        BufferedImage image = CaptchaConfig.kaptchaProducer().createImage(captchaText);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, imgType, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(imageBytes);
    }

    public Boolean verify(String email,String emailVerificationCode){
        CaptchaEntity captchaEntity = (CaptchaEntity) redisUtil.get(email);
        System.out.println("captchaEntity:"+captchaEntity);
        boolean verifyResult = captchaEntity != null && compare(emailVerificationCode,captchaEntity.getCaptchaText());
        if(verifyResult){
            redisUtil.delete(email);
        }
        return verifyResult;
    }

    public Boolean compare(String toBeVerifiedCaptcha,String correctCaptcha){
        if(toBeVerifiedCaptcha==null || correctCaptcha ==null){
            return false;
        }
        return toBeVerifiedCaptcha.equalsIgnoreCase(correctCaptcha.replace(" ", ""));

    }
}
