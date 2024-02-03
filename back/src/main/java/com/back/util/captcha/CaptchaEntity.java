package com.back.util.captcha;

import lombok.Data;

@Data
public class CaptchaEntity {
    public CaptchaEntity() {
    }

    public CaptchaEntity(String captchaText, Long nextDispatchTime) {
        this.captchaText = captchaText;
        this.nextDispatchTime = nextDispatchTime;
    }

    String captchaText;//验证码内容
    Long nextDispatchTime;//最早下次可生成时间
}
