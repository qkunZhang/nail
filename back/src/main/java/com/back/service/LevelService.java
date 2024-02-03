package com.back.service;

import com.back.entity.relationalEntity.LevelEntity;
import com.baomidou.mybatisplus.extension.service.IService;

public interface LevelService extends IService<LevelEntity> {

    public Boolean insert(long userId);
}
