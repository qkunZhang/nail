package com.back.util.invitaionCode;

import com.back.mapper.InvitationMapper;
import com.back.util.encryptorAndCodec.EncryptorAndCodecUtil;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class InvitationCodeUtil {
    /**
     * 说明：【*】指0-9中的任意一个；【%】指字母、数字、中划线和下划线中的任意一个
     * <p>
     * 邀请码（未加密，32位字符串）：%**********%*******************%
     * 主体由用户id（long，用0扩展为19位字符串）和 秒级时间戳（long，用0扩展为10位字符串）构成
     * 在左右边界和分隔处用随机字符（abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_）填充
     * <p>
     * 邀请码（加密后。64位字符串）：%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     * 加密方式是AES/ECB/PKCS5Padding，加密后的编码方式是BASE64/URL
     * **/
    private final InvitationMapper invitationCodeMapper;

    public InvitationCodeUtil(InvitationMapper invitationCodeMapper) {
        this.invitationCodeMapper = invitationCodeMapper;
    }


    //为一个用户创建一个邀请码，与同一时间创建另一个邀请码（假设是同一用户）碰撞的概率为【1/64^3 = 1/262144】
    //由于概率极小，能够忍受少量的相似邀请码存在导致的复用，故系统不做碰撞检测
    public String createAnInvitationCode(Long userId) throws Exception {
        String userIdString = String.format("%019d", userId);
        String timestampString = String.format("%010d", Instant.now().getEpochSecond());//能用两百多年
        String rawInvitationCode =
                generateRandomChar() +
                        userIdString +
                        generateRandomChar() +
                        timestampString +
                        generateRandomChar();
        return EncryptorAndCodecUtil.encrypt(rawInvitationCode, "AES");
    }

    //一次性最多为一个用户创64个邀请码,否则碰撞概率增大两个数量级
    public List<String> createSeveralInvitationCode(Long userId, Integer num) throws Exception {
        if (num <= 0) {
            return new ArrayList<>();
        } else if (num > 64) {
            num = 64;
        }

        String userIdString = String.format("%019d", userId);
        String timestampString = String.format("%010d", Instant.now().getEpochSecond());//能用两百多年
        String charPool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";
        List<String> invitationCodeList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            String rawInvitationCode =
                    generateRandomChar() +
                            userIdString +
                            charPool.charAt(i) +
                            timestampString +
                            generateRandomChar();
            invitationCodeList.add(EncryptorAndCodecUtil.encrypt(rawInvitationCode, "AES"));
        }

        return invitationCodeList;
    }

    public Long getUidFromInvitationCode(String invitationCode) throws Exception {
        String unEncryptInvitationCode = EncryptorAndCodecUtil.decrypt(invitationCode, "AES");
        return Long.parseLong(unEncryptInvitationCode.substring(1, 20));
    }




    //生成填充邀请码的随机字符
    private static char generateRandomChar() {
        String charPool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";
        Random random = new Random();
        int randomIndex = random.nextInt(charPool.length());
        return charPool.charAt(randomIndex);
    }


}
