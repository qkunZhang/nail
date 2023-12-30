package com.back.service;

import com.back.entity.UserEntity;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<UserEntity> {

    public UserEntity getUserById(Integer id);
}
