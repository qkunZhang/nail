package com.back.util.id;

import com.hy.corecode.idgen.WFGIdGenerator;
import com.hy.properties.IdGeneratorOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdConfig {
    @Bean
    public WFGIdGenerator wFGIdGenerator() {
        //https://gitee.com/lmlx66/yitter-idgenerator-spring-boot-starter
        //WorkerIdBitLength + SeqBitLength + DataCenterIdBitLength <= 22
        IdGeneratorOptions idGeneratorOptions = new IdGeneratorOptions();
        idGeneratorOptions.setMethod((short) 1);
        idGeneratorOptions.setBaseTime(1704038400000L); //设置基础时间为2024-01-1 00:00:00
        idGeneratorOptions.setDataCenterId((short) 0);  //数据中心id
        idGeneratorOptions.setDataCenterIdBitLength((byte) 0);  //数据中心位长,0表示不开启区分数据中心功能
        idGeneratorOptions.setWorkerId((short) 0); //设置机器码为0
        idGeneratorOptions.setWorkerIdBitLength((byte) 6); //设置机器码位长为6,代表理论能有2^6=64台机器
        idGeneratorOptions.setSeqBitLength((byte)6); //设置序列数码位长为6.代表每毫秒理论能生成2^6=64个序列号
        idGeneratorOptions.setMinSeqNumber((short) 5); //每毫秒的前5个序列数对应编号0-4是保留位，其中0是手工插入新值预留位，1-4是时间回拨相应预留位。
        idGeneratorOptions.setMinSeqNumber((short) 63);
        idGeneratorOptions.setTopOverCostCount((short) 2000);

        return new WFGIdGenerator(idGeneratorOptions);
    }
}
