package com.back.util.neo4j;

import com.back.entity.graphEntity.ArticleNode;
import com.back.entity.graphEntity.CategoryNode;
import org.springframework.data.neo4j.core.Neo4jOperations;
import org.springframework.stereotype.Component;

@Component
public class Neo4jUtil {
    private final Neo4jOperations neo4j;
    public Neo4jUtil(Neo4jOperations neo4j) {
        this.neo4j = neo4j;
    }

    /**
     * 创建独立的类型节点
     * */
    public void createCategoryNode(Long categoryId, String categoryName){
        CategoryNode categoryNode = new CategoryNode(categoryId,categoryName);
        neo4j.save(categoryNode);
    }

    /**
     * 创建类型节点并让其指向自己的父节点
     * */
    public void createCategoryNode(Long categoryId,String categoryName,Long parentCategoryId){
        CategoryNode parentNode = neo4j.findById(parentCategoryId,CategoryNode.class).orElse(new CategoryNode());
        CategoryNode categoryNode = new CategoryNode(categoryId,categoryName,parentNode);
        neo4j.save(categoryNode);
    }

    /**
     * 创建独立的文章节点
     * */
    public void createArticleNode(Long articleId,String title){
        ArticleNode articleNode = new ArticleNode(articleId,title);
        neo4j.save(articleNode);
    }

    /**
     * 创建文章节点并让其指向自己的直属类型节点
     * */
    public void createArticleNode(Long articleId,String title,Long directCategoryId){
        CategoryNode directCategoryNode = neo4j.findById(directCategoryId,CategoryNode.class).orElse(new CategoryNode());
        ArticleNode articleNode = new ArticleNode(articleId,title,directCategoryNode);
        neo4j.save(articleNode);
    }
}
