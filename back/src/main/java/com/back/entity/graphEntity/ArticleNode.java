package com.back.entity.graphEntity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Data
@Node("Article")
public class ArticleNode {
    @Id
    private Long articleId;
    private String title;
    @Relationship(value = "BELONGS_To",direction = Relationship.Direction.OUTGOING)
    private CategoryNode directCategoryNode;

    public ArticleNode(Long articleId,String title){
        this.articleId = articleId;
        this.title = title;
    }

    public ArticleNode(Long articleId,String title,CategoryNode directCategoryNode){
        this.articleId = articleId;
        this.title = title;
        this.directCategoryNode = directCategoryNode;
    }
}
