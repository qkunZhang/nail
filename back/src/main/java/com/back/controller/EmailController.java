package com.back.controller;

import com.back.service.EmailService;
import com.back.util.redis.RedisUtil;
import com.back.util.captcha.CaptchaEntity;
import com.back.util.captcha.CaptchaUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/email",produces = "application/json; charset=UTF-8")
public class EmailController {
    private final EmailService emailService;
    private final RedisUtil redisUtil;
    private final CaptchaUtil captchaUtil;

    public EmailController(EmailService emailService, RedisUtil redisUtil, CaptchaUtil captchaUtil) {
        this.emailService = emailService;
        this.redisUtil = redisUtil;
        this.captchaUtil = captchaUtil;
    }


    @PostMapping("/send-verification-code")
    public void sendVerificationCode(@RequestBody String email) throws Exception {
        if (email == null || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new IllegalArgumentException("您的邮箱格式错误，系统拒绝发送验证码");
        }

        CaptchaEntity captchaEntity = (CaptchaEntity) redisUtil.get(email);

        //验证发出后一分钟之内又继续请求，则拒绝该请求
        if (captchaEntity != null && Instant.now().toEpochMilli() < captchaEntity.getNextDispatchTime()) {
            throw new AccessDeniedException("请求过于频繁，请等待一会儿");
        }

        try {
            String captchaText = captchaUtil.getText();
            emailService.sendVerificationCodeMail(email, "验证码（有效期约15分钟）", captchaUtil.getBase64ofClearImg(captchaText));
            Long nextDispatchTime = Instant.now().toEpochMilli() + 60000L;//邮箱验证码1分钟后才能重新请求
            //验证码有效时间15分钟
            //邮箱得进行加密
            //若验证码存入缓存失败，则即使用户收到了真实验证码也会验证失败，所以直接告诉用户收到的验证码是错误的
            if(!redisUtil.setWithExpire(emailService.encrypt(email), new CaptchaEntity(captchaText, nextDispatchTime), 900, TimeUnit.SECONDS)){
                throw new RuntimeException("抱歉，系统为你的邮箱发送了错误的验证码。");
            }
            System.out.println(captchaText);
            System.out.println("加密邮箱："+emailService.encrypt(email));
        } catch (Exception e) {
            throw new MailSendException("抱歉，验证码未能发送到您填写的邮箱。\n请确保您的邮箱有效或稍后再试。");
        }
    }



    public static BufferedImage base64StringToImage(String base64String) {
        try {
            // 解码Base64字符串为字节数组
            byte[] imageBytes = Base64.getDecoder().decode(base64String);
            // 创建ByteArrayInputStream对象
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            // 通过ImageIO读取字节数组，转换为BufferedImage
            BufferedImage bufferedImage = ImageIO.read(bis);
            bis.close();
            return bufferedImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping(value = "/captcha")
    public void captcha(HttpServletResponse response) throws IOException {
        //生成图片
        String a = captchaUtil.getText();
        System.out.println(a);
        BufferedImage image = base64StringToImage(captchaUtil.getBase64ofPngImg(a));
        //将图片输出给浏览器
        response.setContentType("image/png");
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(image,"png",outputStream);
        }catch (Exception e){

        }
    }




}

