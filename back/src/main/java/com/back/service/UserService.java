package com.back.service;

import com.back.entity.relationalEntity.PasswordResettingUserEntity;
import com.back.entity.relationalEntity.SignInUserEntity;
import com.back.entity.relationalEntity.UserEntity;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<UserEntity> {

    public boolean verifyUsername(String username);
    public boolean isEmailCorrespondToUsername(String email,String username);
    public boolean isUsernameExist(String username);
    public long signIn(String username,String password);
    public boolean verifyEmail(String email);
    public UserEntity encrypt(UserEntity user) throws Exception;
    public SignInUserEntity encrypt(SignInUserEntity user) throws Exception;
    public PasswordResettingUserEntity encrypt(PasswordResettingUserEntity user) throws Exception;
    public boolean signUp(long id,String username,String password,String email);
    public boolean resetPasswordByUsername(String password, String username);


    public String getUsernameFromAccessJWT(String accessJWT) throws Exception;
}
