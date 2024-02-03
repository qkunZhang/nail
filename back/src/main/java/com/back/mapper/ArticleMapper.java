package com.back.mapper;

import com.back.entity.relationalEntity.ArticleEntity;
import com.back.entity.relationalEntity.InvitationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends BaseMapper<ArticleEntity> {

}
