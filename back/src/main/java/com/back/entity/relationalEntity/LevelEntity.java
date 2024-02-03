package com.back.entity.relationalEntity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("level_tbl")
public class LevelEntity {

    @TableId(value = "user_id",type = IdType.ASSIGN_ID)
    private Long userId;

    @TableField(value = "lv")
    private Short lv;

    @TableField(value = "exp")
    private Integer exp;

}
