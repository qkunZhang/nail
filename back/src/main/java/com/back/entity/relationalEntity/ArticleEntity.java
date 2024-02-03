package com.back.entity.relationalEntity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("article_tbl")
public class ArticleEntity {
    @TableId(value = "article_id",type = IdType.ASSIGN_ID)
    private Long articleId;

    @TableField(value = "title")
    private String title;

    @TableField(value = "content")
    private String content;

    @TableField(value = "author_id")
    private long authorId;

    @TableField(value = "pub_time")
    private long pubTime;

    @TableField(value = "upd_time")
    private long updTime;
}
