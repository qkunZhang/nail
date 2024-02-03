package com.back.entity.graphEntity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Data
@Node("Category")
public class CategoryNode {
    public CategoryNode() {}

    @Id
    private Long categoryId;
    private String categoryName;

    @Relationship(value = "BELONGS_To",direction = Relationship.Direction.OUTGOING)
    private CategoryNode parentCategoryNode;

    public CategoryNode(Long categoryId,String categoryName){
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public CategoryNode(Long categoryId,String categoryName,CategoryNode parentCategoryNode){
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.parentCategoryNode = parentCategoryNode;
    }


}
