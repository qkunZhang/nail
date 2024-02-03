package com.back.controller;


import com.back.service.EmailService;
import com.back.util.redis.RedisUtil;
import com.back.util.captcha.CaptchaUtil;
import com.back.util.id.IdUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping(value = "/test",produces = "application/json; charset=UTF-8")
public class TestController {
    private final EmailService emailService;
    private final RedisUtil redisUtil;
    private final CaptchaUtil captchaUtil;
    private final IdUtil idUtil;

    public TestController(EmailService emailService, RedisUtil redisUtil, CaptchaUtil captchaUtil, IdUtil idUtil) {
        this.emailService = emailService;
        this.redisUtil = redisUtil;
        this.captchaUtil = captchaUtil;
        this.idUtil = idUtil;
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

    @GetMapping(value = "/id")
    public Long id(){
        return idUtil.getId();
    }




}


