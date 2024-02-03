package com.back.service.impl;


import com.back.entity.relationalEntity.PasswordResettingUserEntity;
import com.back.entity.relationalEntity.SignInUserEntity;
import com.back.entity.relationalEntity.UserEntity;
import com.back.mapper.UserMapper;
import com.back.service.EmailService;
import com.back.service.UserService;
import com.back.util.jsonWebToken.JsonWebTokenUtil;
import com.back.util.redis.RedisUtil;
import com.back.util.captcha.CaptchaUtil;
import com.back.util.encryptorAndCodec.EncryptorAndCodecEnum;
import com.back.util.encryptorAndCodec.EncryptorAndCodecUtil;
import com.back.mapper.InvitationMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final RedisUtil redisUtil;
    private final InvitationMapper invitationCodeMapper;
    private final CaptchaUtil captchaUtil;

    private final JsonWebTokenUtil jsonWebTokenUtil;
    public UserServiceImpl(UserMapper userMapper, EmailService emailService, InvitationMapper invitationCodeMapper, RedisUtil redisUtil, CaptchaUtil captchaUtil, JsonWebTokenUtil jsonWebTokenUtil) {
        this.userMapper = userMapper;
        this.emailService = emailService;
        this.redisUtil = redisUtil;
        this.invitationCodeMapper = invitationCodeMapper;
        this.captchaUtil = captchaUtil;
        this.jsonWebTokenUtil = jsonWebTokenUtil;
    }

    @Override
    public boolean verifyUsername(String username){
        return userMapper.selectByName(username) == null;
    }

    @Override
    public boolean isEmailCorrespondToUsername(String email, String username) {
        String correctEmail = userMapper.selectEmailByName(username);
        return correctEmail!=null && correctEmail.equals(email);
    }

    @Override
    public boolean isUsernameExist(String username) {
        return userMapper.selectByName(username) != null;
    }

    @Override
    public long signIn(String username, String password) {
        UserEntity  user = userMapper.selectByName(username);
        if(user != null && user.getPassword().equals(password)){
            return user.getUserId();
        }else {
            return -1L;
        }
    }

    @Override
    public boolean verifyEmail(String email){
        return userMapper.selectByEmail(email) == null;
    }

    @Override
    public UserEntity encrypt(UserEntity user) throws Exception {
        user.setUsername(EncryptorAndCodecUtil.encrypt(user.getUsername(), EncryptorAndCodecEnum.XOR.alg));
        user.setPassword(EncryptorAndCodecUtil.encrypt(user.getPassword(), EncryptorAndCodecEnum.SHA3.alg));
        user.setEmail(emailService.encrypt(user.getEmail()));
        return user;
    }

    @Override
    public SignInUserEntity encrypt(SignInUserEntity user) throws Exception {
        user.setUsername(EncryptorAndCodecUtil.encrypt(user.getUsername(), EncryptorAndCodecEnum.XOR.alg));
        user.setPassword(EncryptorAndCodecUtil.encrypt(user.getPassword(), EncryptorAndCodecEnum.SHA3.alg));
        return user;
    }

    @Override
    public PasswordResettingUserEntity encrypt(PasswordResettingUserEntity user) throws Exception {
        user.setUsername(EncryptorAndCodecUtil.encrypt(user.getUsername(), EncryptorAndCodecEnum.XOR.alg));
        user.setPassword(EncryptorAndCodecUtil.encrypt(user.getPassword(), EncryptorAndCodecEnum.SHA3.alg));
        user.setEmail(emailService.encrypt(user.getEmail()));
        return user;
    }

    @Override
    public boolean signUp(long userId, String username, String password, String email) {
        try {
            return userMapper.insert(userId,username,password,email) == 1;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean resetPasswordByUsername(String password, String username) {
        try {
            return userMapper.updatePassword(password,username)==1;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getUsernameFromAccessJWT(String accessJWT) throws Exception {
        long userId = jsonWebTokenUtil.getUIdFromJWT(accessJWT);
        return EncryptorAndCodecUtil.decrypt(userMapper.selectNameById(userId),EncryptorAndCodecEnum.XOR.alg);
    }


}
