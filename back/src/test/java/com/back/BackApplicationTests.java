package com.back;

import com.back.util.neo4j.Neo4jUtil;
import com.back.util.redis.RedisUtil;
import com.back.util.id.IdUtil;
import com.back.mapper.InvitationMapper;
import com.back.util.invitaionCode.InvitationCodeUtil;
import com.back.util.jsonWebToken.JsonWebTokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.neo4j.core.Neo4jTemplate;

@SpringBootTest
class BackApplicationTests {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    JsonWebTokenUtil jsonWebTokenUtil;

    @Autowired
    InvitationCodeUtil invitationCodeUtil;

    @Autowired
    InvitationMapper invitationCodeMapper;

    @Autowired
    IdUtil idUtil;



    @Autowired
    Neo4jTemplate neo4jTemplate;

    @Autowired
    Neo4jUtil neo4jUtil;

    @Test
    void contextLoads() throws Exception {
        neo4jUtil.createCategoryNode(idUtil.getId(),"xixi",11992636756031L);
        neo4jUtil.createArticleNode(idUtil.getId(),"你好清除",11992636756031L);
    }

}
