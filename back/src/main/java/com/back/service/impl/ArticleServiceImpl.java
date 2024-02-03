package com.back.service.impl;

import com.back.entity.relationalEntity.ArticleEntity;
import com.back.mapper.ArticleMapper;
import com.back.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, ArticleEntity> implements ArticleService {


}
