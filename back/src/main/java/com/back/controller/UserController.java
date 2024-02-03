package com.back.controller;

import com.back.entity.relationalEntity.PasswordResettingUserEntity;
import com.back.entity.relationalEntity.SignInUserEntity;
import com.back.entity.relationalEntity.UserEntity;
import com.back.service.InvitationService;
import com.back.service.LevelService;
import com.back.service.UserService;
import com.back.util.id.IdUtil;
import com.back.util.jsonWebToken.JsonWebTokenEntity;
import com.back.util.redis.RedisUtil;
import com.back.util.captcha.CaptchaUtil;
import com.back.util.exception.CheckedException;
import com.back.util.invitaionCode.InvitationCodeStatusEnum;
import com.back.util.invitaionCode.InvitationCodeUtil;
import com.back.util.jsonWebToken.JsonWebTokenUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;

@RestController
@RequestMapping(value = "/user",produces = "application/json; charset=UTF-8")
public class UserController {
    private final UserService userService;
    private final InvitationService invitationService;
    private final InvitationCodeUtil invitationCodeUtil;
    private final JsonWebTokenUtil jwtUtil;
    private final LevelService levelService;
    private final IdUtil idUtil;
    private final RedisUtil redisUtil;
    private final CaptchaUtil captchaUtil;

    public UserController(UserService userService,
                          JsonWebTokenUtil jwtUtil, RedisUtil redisUtil,
                          InvitationCodeUtil invitationCodeUtil,
                          InvitationService invitationService, LevelService levelService, IdUtil idUtil, CaptchaUtil captchaUtil) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.invitationService = invitationService;
        this.redisUtil = redisUtil;
        this.invitationCodeUtil = invitationCodeUtil;
        this.levelService = levelService;
        this.idUtil = idUtil;
        this.captchaUtil = captchaUtil;
    }


    /**
     * 接收到了邀请码密文、用户名密文、密码密文、邮箱密文、验证码密文
     * 第一步，解密解码，已在RequestBodyAdviceImpl中自动完成
     * 第二步, 格式检查，已在UserEntity中自动完成
     * 第三步，加密，因为数据库中的数据都是加密过的
     * 第三步，有效性校验
     *  1、邮箱验证码正确性校验——缓存数据库中是否存在键为该邮箱的数据项且数据项的值等于该邮箱验证码
     *  2、邀请码有效性校验——数据库中是否有该邀请码存在且（1）已被分发给用户（2）未被使用过
     *  3、用户名唯一性校验——数据库中是否已存在相同的用户名
     *  4、邮箱唯一性校验——数据库中是否已存在相同的邮箱
     * 第四步，存入数据库
     **/
    @PostMapping("/sign-up")
    @Transactional
    public Boolean signUp(@Validated @RequestBody UserEntity user) throws Exception {
        System.out.println("解密过："+user);
        user = userService.encrypt(user);
        String invitationCode = user.getInvitationCode();
        String username = user.getUsername();
        String password = user.getPassword();
        String email = user.getEmail();
        String emailVerificationCode = user.getVerificationCode();
        System.out.println("加密后："+user);

        if (!captchaUtil.verify(email,emailVerificationCode)){
            throw new CheckedException("错误的邮箱验证码");
        }

        if (!invitationService.verify(invitationCode)){
            throw new CheckedException("无效的邀请码");
        }

        if(!userService.verifyUsername(username)){
            throw new CheckedException("用户名已被别人使用");
        }

        if (!userService.verifyEmail(email)){
            throw new CheckedException("邮箱已被别人使用");
        }

        long userId = idUtil.getId();
        if (!userService.signUp(userId,username,password,email)){//用户数据插入用户表
            throw new SQLException("抱歉，用户信息录入，注册失败");
        }else if(!invitationService.updateStatusAndInviteeIdByInvitationCode(invitationCode, InvitationCodeStatusEnum.USED.status,userId)){//将邀请码状态改为USED,将invitee_id改为用户id
            throw new SQLException("抱歉，邀请关系建立异常，注册失败");
        }else if(!levelService.insert(userId)){//用户等级
            throw new SQLException("抱歉，等级初始化异常，注册失败");
        }

        //将邮箱验证码缓存删除
        redisUtil.delete(email);

        return true;
    }


    @PostMapping("/sign-in")
    @Transactional
    public JsonWebTokenEntity signIn(@Validated @RequestBody SignInUserEntity user) throws Exception {
        System.out.println("解密过："+user);
        user = userService.encrypt(user);
        String username = user.getUsername();
        String password = user.getPassword();
        System.out.println("加密后："+user);

        long userId = userService.signIn(username,password);
        if (userId!=-1){
            JsonWebTokenEntity jsonWebTokenEntity = new JsonWebTokenEntity();
            jsonWebTokenEntity.setAccessJWT(jwtUtil.createAccessJWTByUId(userId));
            jsonWebTokenEntity.setRefreshJWT(jwtUtil.createRefreshJWTByUId(userId));
            System.out.println(jsonWebTokenEntity);
            return jsonWebTokenEntity;
        }else {
            throw new AccessDeniedException("用户名或密码错误");
        }
    }

    @PostMapping("/password-reset")
    @Transactional
    public Boolean resetPassword(@Validated @RequestBody PasswordResettingUserEntity user) throws Exception {
        System.out.println("解密过："+user);
        user = userService.encrypt(user);
        String username = user.getUsername();
        String password = user.getPassword();
        String email = user.getEmail();
        String emailVerificationCode = user.getVerificationCode();
        System.out.println("加密后："+user);

        if (!captchaUtil.verify(email,emailVerificationCode)){
            throw new CheckedException("错误的邮箱验证码");
        }

        if(!userService.isUsernameExist(username)){
            throw new CheckedException("不存在的用户名");
        }

        if(!userService.isEmailCorrespondToUsername(email,username)){
            throw new CheckedException("邮箱与用户名不对应");
        }

        if(!userService.resetPasswordByUsername(password,username)){
            throw new SQLException("系统错误，重置密码失败，请稍后重试");
        }else {
            return true;
        }

    }

    @PostMapping("/get-username")
    @Transactional
    public String getUsername(@RequestBody String accessJWT) throws Exception {
        if(accessJWT!=null && !accessJWT.isEmpty()){
            return userService.getUsernameFromAccessJWT(accessJWT);
        }else {
            System.out.println(accessJWT);
            throw new CheckedException("accessJWT传输异常");
        }

    }





}
