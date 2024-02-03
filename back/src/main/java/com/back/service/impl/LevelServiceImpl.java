package com.back.service.impl;


import com.back.entity.relationalEntity.LevelEntity;
import com.back.mapper.LevelMapper;
import com.back.service.LevelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class LevelServiceImpl extends ServiceImpl<LevelMapper, LevelEntity> implements LevelService {
    private final LevelMapper levelMapper;

    public LevelServiceImpl(LevelMapper levelMapper) {
        this.levelMapper = levelMapper;
    }

    @Override
    public Boolean insert(long userId) {
        return 1==levelMapper.insert(userId);
    }
}
