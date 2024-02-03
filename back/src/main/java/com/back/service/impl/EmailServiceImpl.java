package com.back.service.impl;

import com.back.service.EmailService;
import com.back.util.encryptorAndCodec.EncryptorAndCodecEnum;
import com.back.util.encryptorAndCodec.EncryptorAndCodecUtil;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public EmailServiceImpl(JavaMailSender javaMailSender,TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Value("${spring.mail.username}")
    private String from ;

    @Value("${spring.mail.nickname}")
    private String nickname;

//    退信的情况未处理
    @Override
    public void sendTextMail(String to, String subject, String content) {
        SimpleMailMessage simpMsg = new SimpleMailMessage();
        simpMsg.setFrom(from);//发送者
        simpMsg.setTo(to);//收件人
        simpMsg.setSubject(subject);//邮件主题
        simpMsg.setText(content);//邮件内容
        javaMailSender.send(simpMsg);

    }

    @Override
    public void sendVerificationCodeMail(String to, String subject, String verificationCodeImgBase64) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(new InternetAddress(from, nickname, "UTF-8"));
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        Context context = new Context();
        context.setVariable("verificationCodeImgBase64",verificationCodeImgBase64);
        String content = templateEngine.process("mail",context);
        mimeMessageHelper.setText(content, true);
        javaMailSender.send(mimeMessage);
    }

    @Override
    public void sendAttachmentsMail(String to, String subject, String content, String filePath) {

    }

    @Override
    public String encrypt(String email) throws Exception {
        return EncryptorAndCodecUtil.encrypt(email, EncryptorAndCodecEnum.AES.alg);
    }
}
