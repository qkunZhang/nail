package com.back.controller;


import com.back.entity.UserEntity;
import com.back.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/getUserById")
    public UserEntity getInfo(Integer id){
        return userService.getUserById(id);
    }

}
