package com.back.mapper;

import com.back.entity.relationalEntity.LevelEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LevelMapper extends BaseMapper<LevelEntity> {
    @Insert("insert into level_tbl (user_id) values (#{userId})")
    public Integer insert(@Param("userId") long userId);
}
