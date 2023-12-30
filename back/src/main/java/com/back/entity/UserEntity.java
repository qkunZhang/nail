package com.back.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_info_tbl")
public class UserEntity {
    private Integer id;
    private String username;
    private String password;
}
