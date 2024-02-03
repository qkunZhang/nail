package com.back.entity.relationalEntity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignInUserEntity {
    @NotNull(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]{1,16}$", message = "错误格式的用户名")
    private String username;

    @NotNull(message = "密码不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{1,128}$", message = "错误格式的密码")
    private String password;
}
