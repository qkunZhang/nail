package com.back.service;

public interface EmailService {
    /**
     * 发送文本邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendTextMail(String to, String subject, String content);


    void sendVerificationCodeMail(String to, String subject, String verificationCodeImgBase64) throws Exception;

    /**
     * 发送带附件的邮件
     *
     * @param to       收件人
     * @param subject  主题
     * @param content  内容
     * @param filePath 附件
     */
    public void sendAttachmentsMail(String to, String subject, String content, String filePath);

    public String encrypt(String email) throws Exception;
}