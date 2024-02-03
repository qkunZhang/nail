package com.back.entity.relationalEntity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
@TableName("user_tbl")
public class UserEntity {
    @TableId(value = "user_id",type = IdType.ASSIGN_ID)
    private Long userId;

    @TableField(value = "username")
    @NotNull(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]{1,16}$", message = "错误格式的用户名")
    private String username;

    @TableField(value = "password")
    @NotNull(message = "密码不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{1,128}$", message = "错误格式的密码")
    private String password;

    @TableField(value = "email")
    @NotNull(message = "邮箱不能为空")
    @Email(message = "错误格式的邮箱")
    private String email;

    @TableField(exist = false)
    @NotNull(message = "邀请码不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{1,128}$", message = "错误格式的邀请码")
    private String invitationCode;

    @TableField(exist = false)
    @NotNull(message = "邮箱验证码不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9]{0,6}$", message = "错误格式的邮箱验证码")
    private String verificationCode;
}
