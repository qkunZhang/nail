package com.back.util.captcha;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class CaptchaConfig {
    public static Producer kaptchaProducer() {
        Properties properties = new Properties();

        //图片的宽高与渐变背景色
        properties.setProperty("kaptcha.image.width", "135");
        properties.setProperty("kaptcha.image.height", "35");
        properties.setProperty("kaptcha.background.clear.from", "green");
        properties.setProperty("kaptcha.background.clear.to", "blue");
        //字体的大小、颜色
        properties.setProperty("kaptcha.textproducer.font.size", "25");
        properties.setProperty("kaptcha.textproducer.font.color", "black");
        //验证码的字符间距、字符池、字符个数
        properties.setProperty("kaptcha.textproducer.char.space", "3");
        properties.setProperty("kaptcha.textproducer.char.string", " aFHJKkLMmNnPpQ R TUVWwXxY357 ");
        properties.setProperty("kaptcha.textproducer.char.length", "6");
        //干扰
//        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy");
        properties.setProperty("kaptcha.obscurificator.impl", "com.back.util.captcha.CaptchaGimpy");

        DefaultKaptcha captcha = new DefaultKaptcha();
        captcha.setConfig(new Config(properties));
        return captcha;
    }
}
