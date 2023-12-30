package com.back.service.impl;

import com.back.entity.UserEntity;
import com.back.mapper.UserMapper;
import com.back.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {
    private final UserMapper userMapper;
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserEntity getUserById(Integer id) {
        return userMapper.selectById(id);
    }
}
